package com.bahy.newsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bahy.newsapp.databinding.ActivityMainBinding
import com.bahy.nota.Article
import com.bahy.nota.News
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadNews()

        binding.swipeRefresh.setOnRefreshListener {
            loadNews()
        }
    }

    private fun loadNews(){
        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val c= retrofit.create(NewsCallable::class.java)
        c.getNews().enqueue(object : retrofit2.Callback<News>{
            override fun onResponse(call: Call<News?>, response: Response<News?>) {
                val news = response.body()
                val articals = news?.articles!!
                articals.removeAll{
                    it.title == "[Removed]"
                }


                //Log.d("trace","Articles : $articals")
                showNews(articals)
                binding.progress.isVisible = false
                binding.swipeRefresh.isRefreshing = false

            }

            override fun onFailure(call: Call<News?>, t: Throwable) {
                Log.d("trace","Error : ${t.message}")
                binding.progress.isVisible=false
            }

        })
    }

    private fun showNews(articles: ArrayList<Article>){
        val adapter = NewsAdapter(this, articles)
        binding.newsList.adapter=adapter
    }
}

