package com.bahy.newsapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bahy.newsapp.databinding.ActivityHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class Home : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val country = "us"

        binding.btnGeneral.setOnClickListener {
           val intent= Intent(this, MainActivity::class.java)
            intent.putExtra("category", "general")
            intent.putExtra("country", country)
            startActivity(intent)
        }
        binding.btnBusiness.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            intent.putExtra("category", "business")
            intent.putExtra("country", country)
            startActivity(intent)

        }
        binding.btnEntertainment.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            intent.putExtra("category", "entertainment")
            intent.putExtra("country", country)
            startActivity(intent)
        }
        binding.btnHealth.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            intent.putExtra("category", "health")
            intent.putExtra("country", country)
            startActivity(intent)
        }
        binding.btnScience.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            intent.putExtra("category", "science")
            intent.putExtra("country", country)
            startActivity(intent)
        }
        binding.btnSports.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            intent.putExtra("category", "sports")
            intent.putExtra("country", country)
            startActivity(intent)
        }
        binding.btnTechnology.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            intent.putExtra("category", "technology")
            intent.putExtra("country", country)
            startActivity(intent)
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