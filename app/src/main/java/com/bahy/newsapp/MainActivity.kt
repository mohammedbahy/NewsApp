package com.bahy.newsapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        val country = intent.getStringExtra("country")?:"us"
        val category = intent.getStringExtra("category")?:"general"

       loadNews(country, category)

        binding.swipeRefresh.setOnRefreshListener {
            loadNews(country, category)
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(this, arrayListOf())
        binding.newsList.adapter = newsAdapter
    }

    private fun loadNews(country: String, category: String){
        binding.progress.isVisible = true
        binding.swipeRefresh.isRefreshing = true

        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val c= retrofit.create(NewsCallable::class.java)
        c.getNews(country, category).enqueue(object : retrofit2.Callback<News>{
            override fun onResponse(call: Call<News>, response: Response<News>) {
                try {
                    if (response.isSuccessful && response.body() != null) {
                        val articles = response.body()!!.articles.toMutableList()
                        articles.removeAll { it.title == "[Removed]" }
                        showNews(ArrayList(articles))
                    } else {
                        Log.e("MainActivity", "API Error: Code ${response.code()}, Message: ${response.message()}")
                        Toast.makeText(this@MainActivity, "Failed to load news: ${response.code()}", Toast.LENGTH_LONG).show()
                        showNews(arrayListOf())
                    }
                } finally {
                    binding.progress.isVisible = false
                    binding.swipeRefresh.isRefreshing = false
                }

            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                binding.progress.isVisible = false
                binding.swipeRefresh.isRefreshing = false
                Log.e("MainActivity", "Network Failure: ${t.message}", t)
                Toast.makeText(this@MainActivity, "Network Error. Please check your connection.", Toast.LENGTH_LONG).show()
            }

        })
    }
    private fun showNews(articles: ArrayList<Article>){
        newsAdapter.updateData(articles)
    }
}