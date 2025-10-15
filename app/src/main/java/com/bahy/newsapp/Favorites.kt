package com.bahy.newsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bahy.newsapp.databinding.ActivityFavoritesBinding
import com.bahy.nota.Article
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class Favorites : AppCompatActivity() {

    lateinit var binding: ActivityFavoritesBinding
    private lateinit var favoritesAdapter: NewsAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setupRecyclerView()

        loadFavoriteArticles()

        binding.swipeRefresh.setOnRefreshListener {
            loadFavoriteArticles()
        }

    }

    private fun setupRecyclerView() {
        favoritesAdapter = NewsAdapter(this, arrayListOf(), removeOnUnfavorite = true)
        binding.newsList.adapter = favoritesAdapter
    }

    private fun loadFavoriteArticles() {
        binding.progress.isVisible = true
        binding.swipeRefresh.isRefreshing = true

        val db = FirebaseFirestore.getInstance()
        val userId = Firebase.auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "Please log in to see favorites", Toast.LENGTH_LONG).show()
            binding.progress.isVisible = false
            binding.swipeRefresh.isRefreshing = false
            return
        }

        db.collection("users").document(userId).collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                val articlesList = ArrayList<Article>()
                for (document in result) {
                    val article = document.toObject(Article::class.java)
                    articlesList.add(article)
                }

                // تحديث الـ Adapter بالبيانات التي تم جلبها
                favoritesAdapter.updateData(articlesList)

                binding.progress.isVisible = false
                binding.swipeRefresh.isRefreshing = false

                if(articlesList.isEmpty()) {
                    Toast.makeText(this, "No favorites added yet.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                binding.progress.isVisible = false
                binding.swipeRefresh.isRefreshing = false
                Log.e("Favorites", "Error getting documents: ", exception)
                Toast.makeText(this, exception.message ?: "Failed to load favorites.", Toast.LENGTH_SHORT).show()
            }
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings){
            startActivity(Intent(this, Settings::class.java))
        }
        else if(item.itemId == R.id.sign_out){
            Firebase.auth.signOut()
            startActivity(Intent(this, SignIn::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
