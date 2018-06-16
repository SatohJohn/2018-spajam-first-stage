package com.example.satohjohn.kotlinsample.activity

import android.media.SoundPool
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.satohjohn.kotlinsample.R

class MovieEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MovieEditor", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_editor)

        val resourceId = soundPool.load(this, intent.getIntExtra("rawId", R.raw.destiny), 1)
        Log.d("MovieEditor", "${resourceId}")

        (findViewById<View>(R.id.sample_start_button) as Button).setOnClickListener({
            soundPool.play(resourceId, 1.0f, 1.0f, 0, 0, 1.0f);
        })
    }

    private val soundPool = SoundPool.Builder().setMaxStreams(7).build()
}