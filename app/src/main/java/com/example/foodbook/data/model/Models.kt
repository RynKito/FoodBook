package com.example.foodbook.data.model

/** JSON TheMealDB: mỗi món là 1 map key-value (strMeal, strIngredient1...) */
data class MealsResponse(val meals: List<Map<String, String?>>?)

/** Dùng để hiển thị trên danh sách (Home, Favorite) */
data class MealItem(
    val id: String,
    val name: String,
    val thumb: String,
    val dietType: String = "thit" // "thit", "hai_san", "chay", "trang_mieng"
)

/** Dùng cho màn hình chi tiết */
data class MealDetail(
    val id: String,
    val name: String,
    val thumb: String,
    val category: String,
    val area: String,
    val instructions: String,
    val ingredients: List<String>,
    val dietType: String = "thit" // "thit", "hai_san", "chay", "trang_mieng"
)
