package com.bahy.newsapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val categories = mapOf(
            R.id.btnGeneral to "general",
            R.id.btnBusiness to "business",
            R.id.btnEntertainment to "entertainment",
            R.id.btnHealth to "health",
            R.id.btnScience to "science",
            R.id.btnSports to "sports",
            R.id.btnTechnology to "technology"
        )

        categories.forEach { (id, name) ->
            findViewById<MaterialButton>(id).setOnClickListener {
                //  val intent = Intent(this, NewsListActivity::class.java)
                //  intent.putExtra("category", name)
                startActivity(intent)
            }
        }
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId.equals(R.id.settings)){
            startActivity(Intent(this, Settings::class.java))
        }
        else if(item.itemId.equals(R.id.favorites)){
            startActivity(Intent(this, Favorites::class.java))
        }
        else if(item.itemId.equals(R.id.sign_out)){
            startActivity(Intent(this, SignIn::class.java))
        }

        return super.onOptionsItemSelected(item)
    }
}