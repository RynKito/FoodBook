package com.example.foodbook.data.remote

import com.example.foodbook.data.model.FoodsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/** Server của nhóm: file data/foods.json nằm trong chính repo GitHub của đồ án. */
interface FoodApi {
    @GET("RynKito/FoodBook/main/data/foods.json")
    suspend fun getFoods(): FoodsResponse
}

object FoodClient {
    val api: FoodApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(FoodApi::class.java)
    }
}
