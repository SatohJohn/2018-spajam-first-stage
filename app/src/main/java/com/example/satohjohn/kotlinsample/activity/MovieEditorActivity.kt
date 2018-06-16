package com.example.satohjohn.kotlinsample.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.satohjohn.kotlinsample.R
import com.example.satohjohn.kotlinsample.data.CreatedMovie
import com.example.satohjohn.kotlinsample.data.Stamp
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.android.synthetic.main.activity_movie_editor.*
import java.util.*
import kotlin.collections.ArrayList

class MovieEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MovieEditor", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_editor)
        val musicId = intent.getIntExtra("rawId", R.raw.destiny)
        val resourceId = soundPool.load(this, musicId, 1)
        Log.d("MovieEditor", "${resourceId}")

        (findViewById<View>(R.id.sample_start_button) as Button).setOnClickListener({
            stampList.clear()
            musicStartTime = System.currentTimeMillis()
            soundPool.play(resourceId, 1.0f, 1.0f, 0, 0, 1.0f);
        })
        val shardPreferences = this.getSharedPreferences("savedMovie", Context.MODE_PRIVATE)

        (findViewById<View>(R.id.movie_save_button) as Button).setOnClickListener({
            if (backgroundImageUrl.isEmpty()) {
                return@setOnClickListener
            }

            val mapper     = jacksonObjectMapper()
            val createdMovie = CreatedMovie(
                    stampList.map {
                        Stamp(it.id, it.time)
                    },
                    backgroundImageUrl,
                    musicId)
            val jsonString = mapper.writeValueAsString(createdMovie)
            Log.d("movieEditor", jsonString)

            val shardPrefEditor = shardPreferences.edit()
            shardPrefEditor.putString("${Date()}", jsonString)
            shardPrefEditor.apply()
        })

        testButton1.setOnClickListener({
            stampList.add(Stamp(
                    "3mi",
                    System.currentTimeMillis() - musicStartTime
            ))
        })
        testButton2.setOnClickListener({
            stampList.add(Stamp(
                    "2re",
                    System.currentTimeMillis() - musicStartTime
            ))
        })
        testButton3.setOnClickListener({
            stampList.add(Stamp(
                    "1do",
                    System.currentTimeMillis() - musicStartTime
            ))
        })

        Log.d("MovieEditor", "${shardPreferences.all}")

        backgroundImageView.setOnClickListener({
            // 最初はカメラロールから持ってくる
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, 6542)
        })
    }

    var musicStartTime: Long = System.currentTimeMillis()
    val stampList: ArrayList<Stamp> = ArrayList()
    var backgroundImageUrl: String = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 6542) {
            Log.d("movieEditor", "${data}")
            if (resultCode != Activity.RESULT_OK || data == null) {
                return
            }
            backgroundImageUrl = data.data.path
            backgroundImageView.setImageURI(data.data)
        }

    }

    private val soundPool = SoundPool.Builder().setMaxStreams(7).build()

}