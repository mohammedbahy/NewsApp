package com.bahy.newsapp

import com.bahy.nota.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsCallable {
    @GET("v2/top-headlines")
    @Headers(
        "X-Api-Key: cfa32ee15f3046d4b63d6c34277912f6",
        "User-Agent: NewsApp/1.0 (Android)"
    )
    fun getNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("pageSize") pageSize: Int = 30,
    ): Call<News>

//    @GET("v2/top-headlines?country=us&category=general&apiKey=cfa32ee15f3046d4b63d6c34277912f6&pageSize=30")
//    fun getNews(): Call<News>

}