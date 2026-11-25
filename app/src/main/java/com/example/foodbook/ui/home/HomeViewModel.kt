package com.example.foodbook.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodbook.data.local.AppDatabase
import com.example.foodbook.data.model.MealItem
import com.example.foodbook.data.repo.MealRepository
import com.example.foodbook.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = MealRepository(AppDatabase.get(app), app)

    val categories = listOf("Món Việt", "Món Âu", "Món Nhật", "Món Trung")
    val dietOptions = listOf("Tất cả món", "Món thịt", "Hải sản", "Món chay", "Tráng miệng")

    // Trạng thái đồng bộ với server: Loading / Success(số món) / Error
    private val _sync = MutableLiveData<Resource<Int>>()
    val sync: LiveData<Resource<Int>> = _sync
    var syncErrorShown = false

    private val _meals = MutableLiveData<Resource<List<MealItem>>>()
    val meals: LiveData<Resource<List<MealItem>>> = _meals

    var currentCategory = categories.first()
    var currentDietLabel = dietOptions.first()

    fun load(c: String = currentCategory, dietLabel: String = currentDietLabel) {
        currentCategory = c
        currentDietLabel = dietLabel
        val dietCode = dietCodeOf(dietLabel)
        run { repo.getByCategoryAndDiet(currentCategory, dietCode) }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            load()
        } else {
            val dietCode = dietCodeOf(currentDietLabel)
            run { repo.search(query.trim(), dietCode) }
        }
    }

    private fun dietCodeOf(label: String): String = when (label) {
        "Món thịt" -> "thit"
        "Hải sản" -> "hai_san"
        "Món chay" -> "chay"
        "Tráng miệng" -> "trang_mieng"
        else -> "all"
    }

    // "Hôm nay ăn gì?": chọn ngẫu nhiên một món rồi mở màn hình chi tiết
    private val _random = MutableLiveData<String?>()
    val random: LiveData<String?> = _random

    fun pickRandom() {
        viewModelScope.launch { _random.value = repo.randomMealId() }
    }

    fun randomConsumed() { _random.value = null }

    private var job: Job? = null

    init { syncFromServer() }

    /** Kéo thực đơn mới nhất từ server (Retrofit), lưu vào Room rồi tải lại danh sách. */
    fun syncFromServer() {
        _sync.value = Resource.Loading
        viewModelScope.launch {
            try {
                val count = repo.syncFromServer()
                _sync.value = Resource.Success(count)
                load()
            } catch (e: Exception) {
                _sync.value = Resource.Error("Không có mạng, đang dùng dữ liệu đã lưu trong máy")
            }
        }
    }

    private fun run(block: suspend () -> List<MealItem>) {
        job?.cancel()
        _meals.value = Resource.Loading
        job = viewModelScope.launch {
            val list = try {
                block()
            } catch (e: Exception) {
                _meals.value = Resource.Error("Không tải được dữ liệu. Kiểm tra kết nối mạng.")
                return@launch
            }
            _meals.value = Resource.Success(list)                 // hiện danh sách ngay
            val withImages = repo.fillImages(list)                // rồi điền ảnh đúng món
            if (withImages != list) _meals.value = Resource.Success(withImages)
        }
    }
}
