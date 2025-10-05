package com.bahy.newsapp

import com.bahy.nota.News
import retrofit2.Call
import retrofit2.http.GET

interface NewsCallable {
    @GET("/v2/top-headlines?country=us&category=general&apiKey=cfa32ee15f3046d4b63d6c34277912f6&pageSize=30")
    fun getNews() : Call<News>

}