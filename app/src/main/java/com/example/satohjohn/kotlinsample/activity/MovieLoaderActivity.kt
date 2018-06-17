package com.example.satohjohn.kotlinsample.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.SoundPool
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
import java.util.*

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

        val resourceId = soundPool.load(this, movie.musicId, 1)

        // 個々の処理は適当ですので、後動画に置き換えてください
        val resouces: List<Task> = movie.stampList.map {
            Task(it.time, soundPool.load(this, it.id.toInt(), 1))

        }
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

        movie_loader_button.setOnClickListener({
            resouces.forEach({
                Handler().postDelayed({
                    soundPool.play(it.resource, 1.0f, 1.0f, 0, 0, 1.0f)
                }, it.time)
            })
            soundPool.play(resourceId, 1.0f, 1.0f, 1, 0, 1.0f)
        })

    }

    data class Task(val time:Long, val resource: Int)

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

    private val soundPool = SoundPool.Builder().setMaxStreams(7).build()

}