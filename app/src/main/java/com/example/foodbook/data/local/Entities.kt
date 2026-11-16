package com.example.foodbook.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(@PrimaryKey val uid: String, val email: String)

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey val id: String,
    val name: String,
    val thumb: String,
    val category: String,
    val area: String = "",
    val instructions: String = "",
    val ingredients: String = "",   // mỗi nguyên liệu 1 dòng
    val dietType: String = "thit"
)

@Entity(tableName = "favorites", primaryKeys = ["userId", "mealId"])
data class FavoriteEntity(
    val userId: String, val mealId: String,
    val name: String, val thumb: String
)
