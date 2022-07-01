package com.fourrunstudios.warungku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.fourrunstudios.warungku.databinding.ActivityRegisterPageBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterPage : AppCompatActivity() {
    lateinit var database : DatabaseReference
    lateinit var binding : ActivityRegisterPageBinding
    lateinit var registerIntent : Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPageBinding.inflate(layoutInflater)

        setContentView(binding.root)
        init()
        setOnClick()
    }

    private fun setOnClick() {
        binding.registerButton.setOnClickListener {
            if(validateEmail()){
                if(matchPassword()){
                    if(validatePassword()){
                        register(binding.emailEdittext.text.trim().substring(0,binding.emailEdittext.text.trim().indexOf('@')))
                    }
                    else Toast.makeText(this, "Please limit passwords to 5 to 15 characters", Toast.LENGTH_SHORT).show()
                }
                else Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            }
            else Toast.makeText(this, "Please input a valid email address!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun register(userkey : String) {
        database.get().addOnSuccessListener {
            if(it.child(userkey).exists()) Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show()
            else {
                val userRegister = User(binding.emailEdittext.text.toString(), binding.passwordEdittext.text.toString())
                database.child(userkey).setValue(userRegister)
                startActivity(registerIntent)
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Unable to connect to Firebase!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validatePassword(): Boolean {
        return (binding.passwordEdittext.text.trim().length in 5..15)
    }

    private fun matchPassword(): Boolean {
        return (binding.passwordEdittext.text.trim() == binding.repasswordEdittext.text.trim())
    }

    private fun init() {
        registerIntent = Intent(this, MainActivity::class.java)
        database = FirebaseDatabase.getInstance().getReference("Users")
    }

    private fun validateEmail(): Boolean {
        return !TextUtils.isEmpty(binding.emailEdittext.text) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEdittext.text).matches()
    }
}