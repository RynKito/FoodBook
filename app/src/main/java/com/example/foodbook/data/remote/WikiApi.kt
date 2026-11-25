package com.example.foodbook.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class WikiImage(val source: String?, val width: Int? = null, val height: Int? = null)
data class WikiSummary(val thumbnail: WikiImage?, val originalimage: WikiImage? = null)

/** Lấy ảnh đại diện của bài viết Wikipedia về món ăn (đúng món, miễn phí) */
interface WikiApi {
    @GET("api/rest_v1/page/summary/{title}")
    suspend fun summary(@Path("title") title: String): WikiSummary
}

object WikiClient {
    val api: WikiApi by lazy {
        val http = OkHttpClient.Builder().addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .header("User-Agent", "FoodBookStudentApp/1.0 (student project)").build()
            )
        }.build()
        Retrofit.Builder()
            .baseUrl("https://en.wikipedia.org/")
            .client(http)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WikiApi::class.java)
    }
}
