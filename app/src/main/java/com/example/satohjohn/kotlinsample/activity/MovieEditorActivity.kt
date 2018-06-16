package com.example.satohjohn.kotlinsample.activity

import android.content.Context
import android.media.SoundPool
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.satohjohn.kotlinsample.R
import com.example.satohjohn.kotlinsample.data.CreatedMovie
import com.example.satohjohn.kotlinsample.data.Stamp
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*

class MovieEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MovieEditor", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_editor)

        val musicId = intent.getIntExtra("rawId", R.raw.destiny)
        val resourceId = soundPool.load(this, musicId, 1)
        Log.d("MovieEditor", "${resourceId}")

        (findViewById<View>(R.id.sample_start_button) as Button).setOnClickListener({
            soundPool.play(resourceId, 1.0f, 1.0f, 0, 0, 1.0f);
        })
        val shardPreferences = this.getPreferences(Context.MODE_PRIVATE)

        (findViewById<View>(R.id.movie_save_button) as Button).setOnClickListener({
            // jsonを保存する
            val mapper     = jacksonObjectMapper()
            val createdMovie = CreatedMovie(
                    listOf(
                            Stamp("", 400, 199, 200),
                            Stamp("", 1300, 199, 200)
                    ),
                    "",
                    musicId)
            val jsonString = mapper.writeValueAsString(createdMovie)
            Log.d("movieEditor", jsonString)

            val shardPrefEditor = shardPreferences.edit()
            shardPrefEditor.putString("${Date()}", jsonString)
            shardPrefEditor.apply()
        })

        Log.d("MovieEditor", "${shardPreferences.all}")
    }

    private val soundPool = SoundPool.Builder().setMaxStreams(7).build()
}