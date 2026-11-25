package com.example.foodbook.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.foodbook.data.local.AppDatabase
import com.example.foodbook.data.model.MealItem
import com.example.foodbook.data.repo.MealRepository
import com.example.foodbook.util.CurrentUser

class FavoriteViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = MealRepository(AppDatabase.get(app), app)
    // Room trả LiveData: thêm/xóa yêu thích là UI tự cập nhật
    val favorites: LiveData<List<MealItem>> = repo.observeFavorites(CurrentUser.uid()).switchMap { list ->
        liveData {
            emit(list)
            emit(repo.fillImages(list))   // thay ảnh cũ/sai bằng ảnh đúng món
        }
    }
}
