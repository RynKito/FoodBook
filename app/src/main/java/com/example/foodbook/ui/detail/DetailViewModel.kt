package com.example.foodbook.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodbook.data.local.AppDatabase
import com.example.foodbook.data.model.MealDetail
import com.example.foodbook.data.repo.MealRepository
import com.example.foodbook.util.CurrentUser
import com.example.foodbook.util.Resource
import kotlinx.coroutines.launch

class DetailViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = MealRepository(AppDatabase.get(app))
    private val _detail = MutableLiveData<Resource<MealDetail>>()
    val detail: LiveData<Resource<MealDetail>> = _detail

    fun load(id: String) {
        if (_detail.value is Resource.Success) return
        _detail.value = Resource.Loading
        viewModelScope.launch {
            _detail.value = try {
                Resource.Success(repo.getDetail(id))
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Lỗi tải dữ liệu")
            }
        }
    }

    fun isFavorite(id: String): LiveData<Boolean> = repo.observeIsFavorite(CurrentUser.uid(), id)

    fun toggleFavorite(meal: MealDetail, currentlyFavorite: Boolean) {
        viewModelScope.launch {
            if (currentlyFavorite) repo.removeFavorite(CurrentUser.uid(), meal.id)
            else repo.addFavorite(CurrentUser.uid(), meal)
        }
    }
}
