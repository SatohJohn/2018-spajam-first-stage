package com.example.satohjohn.kotlinsample.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import com.example.satohjohn.kotlinsample.R
import com.example.satohjohn.kotlinsample.R.id.imageView
import com.example.satohjohn.kotlinsample.data.CreatedMovie
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.android.synthetic.main.activity_movie_loader.*

class MovieLoaderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Activity", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_loader)
        val dataJson = intent.getStringExtra("json")
        val mapper = jacksonObjectMapper()

        //val paper = mapper.readValue<Paper>(jsonString)
        val movie: CreatedMovie = mapper.readValue(dataJson)

        Log.d("activity", "${movie}")

//        Handler(mainLooper).postDelayed({
//
//            var moviepath =Uri.parse( Environment.getExternalStorageDirectory().toString() + "/DCIM/100ANDRO/MOV_0034.mp4")
//            Log.d("Activity",moviepath.toString())
//            videoView.setVideoURI(moviepath)
//            videoView.setOnPreparedListener {
//                videoView.start()
//                videoView.setMediaController(MediaController(this))
//            }
//            videoView.setOnCompletionListener {
//                finish()
//            }
//        },200)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 6542) {
            if (resultCode != Activity.RESULT_OK || data == null) {
                return
            }

            imageView.setImageURI(data.data)
            Log.d("Activity", "ここまできた")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}