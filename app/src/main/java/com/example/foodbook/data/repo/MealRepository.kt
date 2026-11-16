package com.example.foodbook.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.foodbook.data.local.AppDatabase
import com.example.foodbook.data.local.FavoriteEntity
import com.example.foodbook.data.local.MealEntity
import com.example.foodbook.data.model.MealDetail
import com.example.foodbook.data.model.MealItem
import com.example.foodbook.data.local.FoodData
import com.example.foodbook.data.remote.RetrofitClient
import com.example.foodbook.data.remote.WikiClient
import com.example.foodbook.util.noAccent
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class MealRepository(db: AppDatabase) {
    private val api = RetrofitClient.api
    private val mealDao = db.mealDao()
    private val favDao = db.favoriteDao()

    companion object {
        @Volatile private var seeded = false

        /** Muốn tự chọn ảnh cho món nào thì dán link ảnh trực tiếp vào đây: "id món" to "https://..." */
        val MANUAL_IMAGES = mapOf<String, String>(
            // "v01" to "https://....jpg",
        )

        /** id món -> tên bài Wikipedia; ảnh đại diện của bài viết chính là ảnh món đó */
        val WIKI_TITLES: Map<String, String> = FoodData.wikiTitles

        /** 40 món ăn: Việt, Âu, Nhật, Trung (mỗi loại 10 món) */
        val ALL_MEALS: List<MealDetail> = FoodData.all
    }

    private fun String.isGoodImage() = isNotBlank() && !contains("unsplash.com")

    private suspend fun saveAllMealsToDb() {
        if (seeded) return
        try {
            for (m in ALL_MEALS) {
                val row = mealDao.byId(m.id)
                if (row == null) mealDao.insert(m.toEntity())
            }
            seeded = true
        } catch (_: Exception) {}
    }

    /** Trả về link ảnh đúng của món: ưu tiên ảnh đã lưu trong Room, chưa có thì hỏi Wikipedia rồi lưu lại. */
    suspend fun resolveImage(id: String): String {
        MANUAL_IMAGES[id]?.let { return it }
        val row = mealDao.byId(id)
        val saved = row?.thumb.orEmpty()
        if (saved.isGoodImage()) return saved
        val title = WIKI_TITLES[id] ?: return ""
        val url = try {
            WikiClient.api.summary(title.replace(' ', '_')).thumbnail?.source.orEmpty()
        } catch (e: Exception) { "" }
        if (url.isNotBlank()) {
            val base = row ?: ALL_MEALS.find { it.id == id }?.toEntity()
            if (base != null) mealDao.insert(base.copy(thumb = url))
        }
        return url
    }

    /** Điền ảnh cho các món chưa có ảnh (chạy song song; món chưa có ảnh thì hiện ô màu) */
    suspend fun fillImages(items: List<MealItem>): List<MealItem> = coroutineScope {
        items.map { item ->
            async {
                if (item.thumb.isGoodImage()) item else item.copy(thumb = resolveImage(item.id))
            }
        }.awaitAll()
    }

    /** Danh sách món theo trang (Việt/Âu/Nhật/Trung) và bộ lọc loại món. Chỉ dùng 40 món có sẵn. */
    suspend fun getByCategoryAndDiet(category: String, diet: String): List<MealItem> {
        saveAllMealsToDb()
        return ALL_MEALS.filter { m ->
            m.category == category && (diet == "all" || m.dietType == diet)
        }.map { MealItem(it.id, it.name, "", it.dietType) }
    }

    /** Tìm món theo tên (không phân biệt dấu) trong 40 món. */
    suspend fun search(query: String, diet: String = "all"): List<MealItem> {
        saveAllMealsToDb()
        val key = query.trim().noAccent()
        return ALL_MEALS.filter { m ->
            (m.name.noAccent().contains(key) || m.category.noAccent().contains(key)) &&
                    (diet == "all" || m.dietType == diet)
        }.map { MealItem(it.id, it.name, "", it.dietType) }
    }

    suspend fun getDetail(id: String): MealDetail {
        val found = ALL_MEALS.find { it.id == id }
            ?: throw IllegalStateException("Không tìm thấy món ăn này")
        val withImage = found.copy(thumb = resolveImage(found.id))
        mealDao.insert(withImage.toEntity())
        return withImage
    }

    // ===== Yêu thích (Room) =====
    fun observeFavorites(uid: String): LiveData<List<MealItem>> =
        favDao.observe(uid).map { list -> list.map { MealItem(it.mealId, it.name, it.thumb) } }

    fun observeIsFavorite(uid: String, id: String): LiveData<Boolean> =
        favDao.observeCount(uid, id).map { it > 0 }

    suspend fun addFavorite(uid: String, d: MealDetail) = favDao.add(FavoriteEntity(uid, d.id, d.name, d.thumb))
    suspend fun removeFavorite(uid: String, id: String) = favDao.remove(uid, id)

    // ===== Chuyển đổi dữ liệu =====
    private fun MealDetail.toEntity() = MealEntity(
        id = id, name = name, thumb = thumb, category = category, area = area,
        instructions = instructions, ingredients = ingredients.joinToString("\n"),
        dietType = dietType
    )

    private fun Map<String, String?>.toEntity() = MealEntity(
        id = this["idMeal"].orEmpty(),
        name = this["strMeal"].orEmpty(),
        thumb = this["strMealThumb"].orEmpty(),
        category = this["strCategory"].orEmpty(),
        area = this["strArea"].orEmpty(),
        instructions = this["strInstructions"].orEmpty(),
        ingredients = (1..20).mapNotNull { i ->
            val name = this["strIngredient$i"]?.trim()
            if (name.isNullOrEmpty()) null else "${this["strMeasure$i"]?.trim().orEmpty()} $name".trim()
        }.joinToString("\n")
    )

    private fun MealEntity.toItem() = MealItem(id, name, thumb, dietType)
    private fun MealEntity.toDetail() = MealDetail(
        id, name, thumb, category, area, instructions,
        ingredients.split("\n").filter { it.isNotBlank() }, dietType
    )
}
