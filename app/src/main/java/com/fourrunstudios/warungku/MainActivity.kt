package com.fourrunstudios.warungku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.fourrunstudios.warungku.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var database : DatabaseReference
    private lateinit var registerIntent : Intent
    private lateinit var loginIntent : Intent
    private lateinit var userkey : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        setOnClick()
    }

    private fun init() {
        loginIntent= Intent(this, DaftarWarung::class.java)
        registerIntent = Intent(this, RegisterPage::class.java)
        database = FirebaseDatabase.getInstance().getReference("Users")

    }

    private fun setOnClick() {
        binding.loginButton.setOnClickListener {
            if(validateEmail()){
                userkey = binding.emailEdittext.text.trim().substring(0,binding.emailEdittext.text.trim().indexOf('@'))
                database.child(userkey).get().addOnSuccessListener {
                    if(it.child("password").value.toString() == binding.passwordEdittext.text.toString()){
                        login()
                    }
                    else Toast.makeText(this, "Password and Email does not match!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Unable to connect to Firebase!", Toast.LENGTH_SHORT).show()
                }
            }
            else Toast.makeText(this, "Please input a valid email!", Toast.LENGTH_SHORT).show()
        }
        binding.registerClick.setOnClickListener {
            startActivity(registerIntent)
        }
    }

    private fun login() {
        binding.emailEdittext.text.clear()
        binding.passwordEdittext.text.clear()
        loginIntent.putExtra("userkey", userkey)
        startActivity(loginIntent)
    }
    private fun validateEmail(): Boolean {
        return !TextUtils.isEmpty(binding.emailEdittext.text) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEdittext.text).matches()
    }


}