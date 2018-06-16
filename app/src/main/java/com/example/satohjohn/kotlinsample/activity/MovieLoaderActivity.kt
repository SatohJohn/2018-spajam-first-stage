package com.example.satohjohn.kotlinsample.activity
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.MediaController
import com.example.satohjohn.kotlinsample.R
import kotlinx.android.synthetic.main.activity_movie_loader.*

class MovieLoaderActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Activity", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_loader)
        Handler(mainLooper).postDelayed({

            var moviepath =Uri.parse( Environment.getExternalStorageDirectory().toString() + "/DCIM/100ANDRO/MOV_0034.mp4")
            Log.d("Avtivity",moviepath.toString())
            videoView.setVideoURI(moviepath)
            videoView.setOnPreparedListener {
                videoView.start()
                videoView.setMediaController(MediaController(this))
            }
            videoView.setOnCompletionListener {
                finish()
            }
        },200)

    }


}