package com.example.satohjohn.kotlinsample.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.satohjohn.kotlinsample.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class TopActivity : AppCompatActivity() {

    private val RECORD_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("mainActivity", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stamp_activity_button.setOnClickListener({
            startActivity(Intent(this, StampCreatorActivity::class.java))
        })
        music_selector_activity_button.setOnClickListener({
            startActivity(Intent(this, MusicSelectorActivity::class.java))
        })
        movie_loader_activity_button.setOnClickListener({
            startActivity(Intent(this, MovieSelectorActivity::class.java))
        })

        setupPermissions()
        Log.d("mainActivity", "end on create")
    }

    private fun setupPermissions() {
        val permissionGrantedList: List<String> = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .filter {
                    PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, it)
                }
        if (permissionGrantedList.isNotEmpty()) {
            Log.i(this::class.java.simpleName, "permission set up: ${permissionGrantedList}")
            ActivityCompat.requestPermissions(this, permissionGrantedList.toTypedArray(), RECORD_REQUEST_CODE)
        }
    }
}
