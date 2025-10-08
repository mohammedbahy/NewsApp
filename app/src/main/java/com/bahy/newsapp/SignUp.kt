package com.bahy.newsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

import com.bahy.newsapp.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        // Login Class
        /*binding.haveaccountTv.setOnClickListener {
            startActivity(Intent(this, ::class.java))
            finish()
        }*/

        binding.signUpBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passEt.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            if (email.isBlank() || password.isBlank() || confirmPass.isBlank()){
                Toast.makeText(this, "Missing fildes", Toast.LENGTH_SHORT).show()
            }
            else if (password.length<6){
                Toast.makeText(this, "Short password", Toast.LENGTH_SHORT).show()
            }
            else if (password != confirmPass){
                Toast.makeText(this, "Passwords Don't match", Toast.LENGTH_SHORT).show()
            }
            else{
                binding.loadingPrograss.isVisible = true
                signUp(email,password)
            }

        }


    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    verifyEmail()

                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun verifyEmail() {
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                    binding.loadingPrograss.isVisible = false

                    binding.haveaccountTv.setOnClickListener {
                        startActivity(Intent(this, SignIn ::class.java))
                        finish()
                    }
                }
            }

    }
}