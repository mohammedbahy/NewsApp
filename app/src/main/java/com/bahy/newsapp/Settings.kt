package com.bahy.newsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bahy.newsapp.databinding.ActivitySettingsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class Settings : AppCompatActivity() {

    private var selectedCountryCode: String = "us"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        loadSavedCountry()
        updateRadioButtonSelection(binding)

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedCountryCode = when (checkedId) {
                R.id.rb_USA -> "us"
                R.id.rb_Germany -> "de"
                R.id.rb_France -> "fr"
                R.id.rb_Egypt -> "eg"
                else -> "us"
            }
        }

        binding.saveBtn.setOnClickListener {
            saveCountry(selectedCountryCode)
            Toast.makeText(this, "Settings Saved!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Home::class.java)
            intent.putExtra("country", selectedCountryCode)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }

    private fun saveCountry(countryCode: String) {
        val sharedPref = getSharedPreferences("NewsAppPrefs", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString("SELECTED_COUNTRY", countryCode)
            apply()
        }
    }

    private fun loadSavedCountry() {
        val sharedPref = getSharedPreferences("NewsAppPrefs", Context.MODE_PRIVATE)
        selectedCountryCode = sharedPref.getString("SELECTED_COUNTRY", "us") ?: "us"
    }

    private fun updateRadioButtonSelection(binding: ActivitySettingsBinding) {
        val radioButtonId = when (selectedCountryCode) {
            "us" -> R.id.rb_USA
            "de" -> R.id.rb_Germany
            "fr" -> R.id.rb_France
            "eg" -> R.id.rb_Egypt
            else -> R.id.rb_USA
        }
        binding.radioGroup.check(radioButtonId)
    }


    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.favorites){
            startActivity(Intent(this, Favorites::class.java))
        }
        else if(item.itemId == R.id.sign_out){
            Firebase.auth.signOut()
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}