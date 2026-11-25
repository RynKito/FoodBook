package com.example.foodbook.data.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.foodbook.data.local.AppDatabase
import com.example.foodbook.data.local.FavoriteEntity
import com.example.foodbook.data.local.MealEntity
import com.example.foodbook.data.model.FoodDto
import com.example.foodbook.data.model.FoodsResponse
import com.example.foodbook.data.model.MealDetail
import com.example.foodbook.data.model.MealItem
import com.example.foodbook.data.remote.FoodClient
import com.example.foodbook.data.remote.WikiClient
import com.example.foodbook.util.noAccent
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * Nguồn dữ liệu duy nhất cho UI:
 *  1) Server (GitHub raw JSON) --Retrofit--> Room  (đồng bộ thực đơn mới nhất)
 *  2) UI chỉ đọc từ Room nên mất mạng vẫn dùng bình thường (offline-first)
 */
class MealRepository(db: AppDatabase, private val context: Context) {
    private val mealDao = db.mealDao()
    private val favDao = db.favoriteDao()

    companion object {
        @Volatile private var assetsLoaded = false

        /** Muốn tự chọn ảnh cho món nào thì dán link vào đây: "id món" to "https://..." (hoặc ghi vào trường image trong foods.json) */
        val MANUAL_IMAGES = mapOf<String, String>()
    }

    private fun String.isGoodImage() = isNotBlank() && !contains("unsplash.com") && !contains("/320px-")

    // ===== Đồng bộ dữ liệu =====

    /** Nạp bản dự phòng assets/foods.json (chỉ thêm món còn thiếu) để app chạy được cả khi chưa có mạng. */
    suspend fun ensureData() {
        if (assetsLoaded) return
        try {
            val text = withContext(Dispatchers.IO) {
                context.assets.open("foods.json").bufferedReader().use { it.readText() }
            }
            val res = Gson().fromJson(text, FoodsResponse::class.java)
            for (dto in res.dishes.orEmpty()) {
                val id = dto.id.orEmpty()
                if (id.isNotBlank() && mealDao.byId(id) == null) mealDao.insert(dto.toEntity(""))
            }
            assetsLoaded = true
        } catch (_: Exception) {}
    }

    /** Kéo thực đơn mới nhất từ server bằng Retrofit rồi lưu vào Room. Mất mạng sẽ ném lỗi. */
    suspend fun syncFromServer(): Int {
        val list = FoodClient.api.getFoods().dishes.orEmpty()
        if (list.isEmpty()) throw IllegalStateException("Server không có dữ liệu")
        saveFoods(list)
        return list.size
    }

    private suspend fun saveFoods(list: List<FoodDto>) {
        val ids = mutableListOf<String>()
        for (dto in list) {
            val id = dto.id.orEmpty()
            if (id.isBlank()) continue
            ids.add(id)
            val old = mealDao.byId(id)
            val image = dto.image.orEmpty()
            val thumb = if (image.isNotBlank()) image else old?.thumb.orEmpty()
            mealDao.insert(dto.toEntity(thumb))
        }
        if (ids.isNotEmpty()) mealDao.deleteNotIn(ids)   // món bị xóa trên server thì xóa luôn trong máy
    }

    // ===== Danh sách, tìm kiếm, chi tiết (đọc từ Room) =====

    suspend fun getByCategoryAndDiet(category: String, diet: String): List<MealItem> {
        ensureData()
        return mealDao.byCategory(category)
            .filter { diet == "all" || it.dietType == diet }
            .sortedBy { it.id }
            .map { it.toItem() }
    }

    /** Tìm món theo tên, không phân biệt dấu ("pho" ra "Phở"). */
    suspend fun search(query: String, diet: String = "all"): List<MealItem> {
        ensureData()
        val key = query.trim().noAccent()
        return mealDao.all()
            .filter { (diet == "all" || it.dietType == diet) && it.name.noAccent().contains(key) }
            .sortedBy { it.id }
            .map { it.toItem() }
    }

    suspend fun getDetail(id: String): MealDetail {
        ensureData()
        val row = mealDao.byId(id) ?: throw IllegalStateException("Không tìm thấy món ăn này")
        val image = resolveImage(id)
        return row.copy(thumb = image.ifBlank { row.thumb }).toDetail()
    }

    /** Chọn ngẫu nhiên một món cho chức năng "Hôm nay ăn gì?" */
    suspend fun randomMealId(): String? {
        ensureData()
        return mealDao.all().randomOrNull()?.id
    }

    // ===== Ảnh món ăn =====

    /** Ảnh đúng của món: ảnh đã lưu trong Room, nếu chưa có thì lấy ảnh đại diện bài Wikipedia của món rồi lưu lại. */
    suspend fun resolveImage(id: String): String {
        MANUAL_IMAGES[id]?.let { return it }
        val row = mealDao.byId(id) ?: return ""
        if (row.thumb.isGoodImage()) return row.thumb
        val title = row.wiki
        if (title.isBlank()) return ""
        val url = try {
            val sum = WikiClient.api.summary(title.replace(' ', '_'))
            val original = sum.originalimage
            val thumb = sum.thumbnail?.source
            when {
                // Ảnh gốc nhỏ hơn 960px thì dùng luôn ảnh gốc, tránh bị phóng to gây mờ
                original?.source != null && (original.width ?: 0) < 960 -> original.source.orEmpty()
                // Ngược lại xin bản 960px (rõ nét hơn bản 320px mặc định)
                thumb != null -> thumb.replace(Regex("/\\d+px-"), "/960px-")
                else -> original?.source.orEmpty()
            }
        } catch (e: Exception) { "" }
        if (url.isNotBlank()) mealDao.insert(row.copy(thumb = url))
        return url
    }

    /** Điền ảnh cho các món chưa có ảnh (chạy song song; chưa có ảnh thì hiện ô màu). */
    suspend fun fillImages(items: List<MealItem>): List<MealItem> = coroutineScope {
        items.map { item ->
            async { if (item.thumb.isGoodImage()) item else item.copy(thumb = resolveImage(item.id)) }
        }.awaitAll()
    }

    // ===== Yêu thích (Room) =====
    fun observeFavorites(uid: String): LiveData<List<MealItem>> =
        favDao.observe(uid).map { list -> list.map { MealItem(it.mealId, it.name, it.thumb) } }

    fun observeIsFavorite(uid: String, id: String): LiveData<Boolean> =
        favDao.observeCount(uid, id).map { it > 0 }

    suspend fun addFavorite(uid: String, d: MealDetail) = favDao.add(FavoriteEntity(uid, d.id, d.name, d.thumb))
    suspend fun removeFavorite(uid: String, id: String) = favDao.remove(uid, id)

    // ===== Chuyển đổi dữ liệu =====
    private fun FoodDto.toEntity(thumb: String) = MealEntity(
        id = id.orEmpty(),
        name = name.orEmpty(),
        thumb = thumb,
        category = category.orEmpty(),
        area = area.orEmpty(),
        instructions = steps.orEmpty().mapIndexed { i, s -> "${i + 1}. $s" }.joinToString("\n\n"),
        ingredients = ingredients.orEmpty().joinToString("\n"),
        dietType = diet ?: "thit",
        wiki = wiki.orEmpty(),
        time = time.orEmpty(),
        servings = servings.orEmpty(),
        level = level.orEmpty(),
        tip = tip.orEmpty()
    )

    private fun MealEntity.toItem() = MealItem(id, name, thumb, dietType)

    private fun MealEntity.toDetail() = MealDetail(
        id = id, name = name, thumb = thumb, category = category, area = area,
        instructions = instructions,
        ingredients = ingredients.split("\n").filter { it.isNotBlank() },
        dietType = dietType, time = time, servings = servings, level = level, tip = tip
    )
}
