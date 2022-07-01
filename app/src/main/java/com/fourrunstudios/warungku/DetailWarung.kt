package com.fourrunstudios.warungku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fourrunstudios.warungku.databinding.ActivityDetailWarungBinding
import com.google.firebase.database.DatabaseReference

class DetailWarung : AppCompatActivity() {
    private lateinit var binding : ActivityDetailWarungBinding
    private lateinit var database : DatabaseReference
    private lateinit var editWarungIntent : Intent
    private lateinit var userkey : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWarungBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setOnClick()
    }

    private fun init() {
        editWarungIntent = Intent(this, TambahEditWarung::class.java)
        userkey = intent.getStringExtra("userkey").toString()
        binding.namawarungTextview.setText(intent.getStringExtra("edit_name").toString())
        binding.alamatTextview.setText(intent.getStringExtra("edit_address").toString())
        binding.koordinatTextview.setText(intent.getDoubleExtra("edit_latitude", 0.0).toString()+
        ","+intent.getDoubleExtra("edit_longitude", 0.0).toString())
    }

    private fun setOnClick() {
        binding.editFab.setOnClickListener {
            editWarungIntent.putExtra("userkey", userkey)
            editWarungIntent.putExtra("intent", "edit")
            editWarungIntent.putExtra("edit_name", intent.getStringExtra("edit_name"))
            editWarungIntent.putExtra("edit_latitude", intent.getDoubleExtra("edit_latitude", 0.0))
            editWarungIntent.putExtra("edit_longitude", intent.getDoubleExtra("edit_longitude", 0.0))
            editWarungIntent.putExtra("edit_address", intent.getStringExtra("edit_address"))
            startActivity(editWarungIntent)
        }
    }
}