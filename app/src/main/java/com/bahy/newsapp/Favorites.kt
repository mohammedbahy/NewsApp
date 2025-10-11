package com.bahy.newsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bahy.newsapp.databinding.ActivityFavoritesBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class Favorites : AppCompatActivity() {

    lateinit var binding: ActivityFavoritesBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


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
        else if(item.itemId == R.id.favorites){
            startActivity(Intent(this, Favorites::class.java))
        }
        else if(item.itemId == R.id.sign_out){
            Firebase.auth.signOut()
            startActivity(Intent(this, SignIn::class.java))
        }

        return super.onOptionsItemSelected(item)
    }
}