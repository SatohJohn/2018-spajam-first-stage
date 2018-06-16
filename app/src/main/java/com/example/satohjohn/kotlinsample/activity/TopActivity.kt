package com.example.satohjohn.kotlinsample.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.satohjohn.kotlinsample.R

class TopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("mainActivity", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (findViewById<View>(R.id.stamp_activity_button) as Button).setOnClickListener({
            startActivity(Intent(this, StampCreatorActivity::class.java))
        })
        (findViewById<View>(R.id.music_selector_activity_button) as Button).setOnClickListener({
            startActivity(Intent(this, MusicSelectorActivity::class.java))
        })
        (findViewById<View>(R.id.movie_loader_activity_button) as Button).setOnClickListener({
            startActivity(Intent(this, MovieLoaderActivity::class.java))
        })

        Log.d("mainActivity", "end on create")
    }

}
