package com.example.foodbook.data.remote

import com.example.foodbook.BuildConfig
import com.example.foodbook.data.model.MealsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("filter.php") suspend fun byCategory(@Query("c") category: String): MealsResponse
    @GET("search.php") suspend fun search(@Query("s") name: String): MealsResponse
    @GET("lookup.php") suspend fun lookup(@Query("i") id: String): MealsResponse
}

object RetrofitClient {
    // API key đọc từ local.properties (không đưa lên GitHub)
    private const val HOST = "https://www.themealdb.com/api/json/v1/"
    val api: MealApi by lazy {
        Retrofit.Builder()
            .baseUrl(HOST + BuildConfig.MEALDB_KEY + "/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MealApi::class.java)
    }
}
