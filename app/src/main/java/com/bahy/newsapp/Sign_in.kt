package com.bahy.newsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bahy.newsapp.databinding.ActivitySignInBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignIn : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        binding.newUser.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

        binding.forget.setOnClickListener {
            binding.loadingPrograss.isVisible = true
            val email = binding.emailIn.text.toString()
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        binding.loadingPrograss.isVisible = false
                        Toast.makeText(this, "Email sent!", Toast.LENGTH_SHORT).show();

                    }
                }


        }


        binding.signInBtn.setOnClickListener {
            val email = binding.emailIn.text.toString()
            val password = binding.passIn.text.toString()

            if (email.isBlank() || password.isBlank())
                Toast.makeText(this, "Missing field/s", Toast.LENGTH_SHORT).show()
            else {
                binding.loadingPrograss.isVisible = true

                //Login logic
                signIn(email,password)

            }
        }


        val reader = getSharedPreferences("user_data", MODE_PRIVATE)
        val savedEmail = reader.getString("email",null)
        val savedPassword = reader.getString("password",null)

        binding.emailIn.setText(savedEmail)
        binding.passIn.setText(savedPassword)


        binding.rememberMe.setOnClickListener{
            val email = binding.emailIn.text.toString()
            val pass = binding.passIn.text.toString()
            val writer = getSharedPreferences("user_data", MODE_PRIVATE).edit()

            if(binding.rememberMe.isChecked)
            {
                writer.putString("email",email)
                writer.putString("password",pass)
            }else
                writer.clear()

            writer.apply()
        }


    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.loadingPrograss.isVisible=false
                    if(auth.currentUser!!.isEmailVerified)
                    {
                        startActivity(Intent(this,Home::class.java))
                        finish()
                    }
                    else
                        Toast.makeText(this,"Check your email!!!!",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }

            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified ) {
            startActivity(Intent(this, Home::class.java))
            finish()
        }
    }

}

