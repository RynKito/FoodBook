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
    private val repo = MealRepository(AppDatabase.get(app))

    val categories = listOf("Món Việt", "Món Âu", "Món Nhật", "Món Trung")
    val dietOptions = listOf("Tất cả món", "Món thịt", "Hải sản", "Món chay", "Tráng miệng")

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

    private var job: Job? = null

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
