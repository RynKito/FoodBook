package com.example.foodbook.data.model

/** Một món ăn trong file JSON trên server. Để nullable vì Gson có thể trả null nếu thiếu trường. */
data class FoodDto(
    val id: String? = null,
    val name: String? = null,
    val category: String? = null,
    val area: String? = null,
    val diet: String? = null,
    val wiki: String? = null,
    val image: String? = null,   // link ảnh riêng (nếu có) sẽ được ưu tiên hơn ảnh Wikipedia
    val time: String? = null,
    val servings: String? = null,
    val level: String? = null,
    val tip: String? = null,
    val ingredients: List<String>? = null,
    val steps: List<String>? = null
)

data class FoodsResponse(val version: Int = 1, val dishes: List<FoodDto>? = null)

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
    val dietType: String = "thit", // "thit", "hai_san", "chay", "trang_mieng"
    val time: String = "",
    val servings: String = "",
    val level: String = "",
    val tip: String = ""
)
