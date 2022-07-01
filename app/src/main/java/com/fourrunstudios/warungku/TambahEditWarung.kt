package com.fourrunstudios.warungku

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.fourrunstudios.warungku.databinding.ActivityTambahEditWarungBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class TambahEditWarung : AppCompatActivity(), LocationListener{
    private lateinit var database : DatabaseReference
    private lateinit var binding : ActivityTambahEditWarungBinding
    private lateinit var userkey : String
    private lateinit var locationManager: LocationManager
    private lateinit var daftarWarungIntent : Intent
    private lateinit var imageUri : Uri
    private lateinit var pictureView : ImageView
    private lateinit var currentPhotoPath : String
    private lateinit var purpose : String
    private lateinit var lastName : String
    private val CAMERA_REQUEST = 1888
    private val MY_CAMERA_PERMISSION_CODE = 100
    private val RESULT_LOAD_IMG = 12
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private val locationPermissionCode = 2
    private val decimalFormat = DecimalFormat("###.#########")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahEditWarungBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setOnClick()
    }

    private fun setOnClick() {
        binding.clickablePic.setOnClickListener {

        }
        binding.coordButton.setOnClickListener {
            getCoords()
        }
        binding.submitButton.setOnClickListener {
            val coordString = binding.koordinatEdittext.text.toString()
            val index = coordString.indexOf(',')
            latitude = decimalFormat.format(coordString.substring(0,index).toDouble()).toDouble()
            longitude = decimalFormat.format(coordString.substring(index+1).toDouble()).toDouble()
            val warung = Warung(binding.namawarungEdittext.text.toString(), latitude, longitude,
                binding.alamatEdittext.text.toString())
            Log.i("", "lastName : $lastName")
            if(purpose=="edit"){
                database.child(userkey).child("warung").child(lastName)
                    .removeValue()
            }
            database.child(userkey).child("warung").child(binding.namawarungEdittext.text.toString())
                .setValue(warung)
            daftarWarungIntent.putExtra("userkey", userkey)
            startActivity(daftarWarungIntent)
        }
    }

    private fun init() {
        database = FirebaseDatabase.getInstance().getReference("Users")
        userkey = intent.getStringExtra("userkey").toString()
        purpose = intent.getStringExtra("intent").toString()
        daftarWarungIntent = Intent(this, DaftarWarung::class.java)
        pictureView = binding.pictureView
        Log.i("purpose ", purpose)
        if(purpose == "edit"){
            lastName = intent.getStringExtra("edit_name").toString()
            binding.namawarungEdittext.setText(lastName)
            binding.koordinatEdittext.text = intent.getDoubleExtra("edit_latitude", 0.0).toString()+","+
                    intent.getDoubleExtra("edit_longitude", 0.0).toString()
            binding.alamatEdittext.setText(intent.getStringExtra("edit_address").toString())
        }
        else lastName = ""
    }
    private fun getCoords() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        else locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(p0: Location) {
        latitude = decimalFormat.format(p0.latitude).toDouble()
        longitude = decimalFormat.format(p0.longitude).toDouble()
        var temp = "$latitude,$longitude"
        binding.koordinatEdittext.text = temp
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}