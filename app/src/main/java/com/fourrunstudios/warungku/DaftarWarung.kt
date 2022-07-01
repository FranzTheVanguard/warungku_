package com.fourrunstudios.warungku

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fourrunstudios.warungku.databinding.ActivityDaftarWarungBinding
import com.google.firebase.database.*


class DaftarWarung : AppCompatActivity(), RecyclerViewAdapter.OnWarungListener {
    private lateinit var binding : ActivityDaftarWarungBinding
    private lateinit var database : DatabaseReference
    private lateinit var editWarungIntent : Intent
    private lateinit var detailWarungIntent : Intent
    private lateinit var userkey : String
    private var warungList = ArrayList<Warung>()
    private var testList = ArrayList<Warung>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarWarungBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setOnClick()
        testList.add(Warung("Nama1", 31.0, 21.0, "alamat1"))
        testList.add(Warung("Nama2", 32.0, 22.0, "alamat2"))
        testList.add(Warung("Nama3", 33.0, 23.0, "alamat3"))

        setupWarung()
        Handler().postDelayed({
            val adapter = RecyclerViewAdapter(applicationContext, warungList, this)
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        }, 3000)
    }

    private fun setOnClick() {
        binding.fabAdd.setOnClickListener {
            editWarungIntent.putExtra("intent", "add")
            editWarungIntent.putExtra("userkey", userkey)
            startActivity(editWarungIntent)
        }
    }

    private fun init() {
        editWarungIntent = Intent(this, TambahEditWarung::class.java)
        detailWarungIntent = Intent(this, DetailWarung::class.java)
        userkey = intent.getStringExtra("userkey").toString()
        database = FirebaseDatabase.getInstance().getReference("Users").child(userkey).child("warung")

    }

    private fun setupWarung() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {

                    val warungName = postSnapshot.child("name").value.toString()
                    val latitude : Double = postSnapshot.child("latitude").value as Double
                    val longitude : Double = postSnapshot.child("longitude").value as Double
                    val address = postSnapshot.child("address").value.toString()
                    val warung = Warung(warungName, latitude, longitude, address)

                    warungList.add(warung)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Firebase failed", "onCancelled: ")
            }
        })
    }

    override fun onWarungClick(position: Int) {
        detailWarungIntent.putExtra("edit_name", warungList[position].name)
        detailWarungIntent.putExtra("edit_latitude", warungList[position].latitude)
        detailWarungIntent.putExtra("edit_longitude", warungList[position].longitude)
        detailWarungIntent.putExtra("edit_address", warungList[position].address)
        detailWarungIntent.putExtra("userkey", userkey)
        startActivity(detailWarungIntent)
    }
}