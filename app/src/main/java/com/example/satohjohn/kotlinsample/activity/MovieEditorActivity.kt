package com.example.satohjohn.kotlinsample.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
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
import kotlinx.android.synthetic.main.activity_movie_editor.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import kotlin.collections.HashMap


class MovieEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MovieEditor", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_editor)
        val musicId = intent.getIntExtra("rawId", R.raw.destiny)
        val resourceId = soundPool.load(this, musicId, 1)
        Log.d("MovieEditor", "${resourceId}")
        stampVideoView.visibility = View.INVISIBLE

        return_home_button.setOnClickListener({
            stampList.clear()
            startActivity(Intent(this, TopActivity::class.java))
        })

        sample_start_button.setOnClickListener({
            stampList.clear()
            musicStartTime = System.currentTimeMillis()
            soundPool.play(resourceId, 1.0f, 1.0f, 0, 0, 1.0f);
        })
        val shardPreferences = this.getSharedPreferences("savedMovie", Context.MODE_PRIVATE)
        var stampUri:Uri? = null

        movie_save_button.setOnClickListener({
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

        videoView1.setOnClickListener({
            stampList.add(Stamp(
                    R.raw.se_maoudamashii_instruments_piano2_1do.toString(),
                    System.currentTimeMillis() - musicStartTime
            ))


            stampVideoView.visibility = View.VISIBLE
            stampVideoView.setVideoPath(filePaths[0])
            stampVideoView.setOnPreparedListener {
                stampVideoView.visibility = View.VISIBLE

                stampVideoView.start()
                stampVideoView.setMediaController(MediaController(this))
            }
            stampVideoView.setOnCompletionListener {
            stampVideoView.visibility = View.INVISIBLE
            }

        })
        videoView2.setOnClickListener({
            stampList.add(Stamp(
                    R.raw.se_maoudamashii_instruments_piano2_2re.toString(),
                    System.currentTimeMillis() - musicStartTime
            ))
            stampVideoView.visibility = View.VISIBLE
            stampVideoView.setVideoPath(filePaths[1])
            stampVideoView.setOnPreparedListener {
                stampVideoView.visibility = View.VISIBLE

                stampVideoView.start()
                stampVideoView.setMediaController(MediaController(this))
            }
            stampVideoView.setOnCompletionListener {
                stampVideoView.visibility = View.INVISIBLE
            }

        })
        videoView3.setOnClickListener({
            stampList.add(Stamp(
                    R.raw.se_maoudamashii_instruments_piano2_3mi.toString(),
                    System.currentTimeMillis() - musicStartTime
            ))
            stampVideoView.visibility = View.VISIBLE
            stampVideoView.setVideoPath(filePaths[2])
            stampVideoView.setOnPreparedListener {
                stampVideoView.visibility = View.VISIBLE

                stampVideoView.start()
                stampVideoView.setMediaController(MediaController(this))
            }
            stampVideoView.setOnCompletionListener {
                stampVideoView.visibility = View.INVISIBLE
            }

        })
        videoView4.setOnClickListener({
            stampList.add(Stamp(
                    R.raw.se_maoudamashii_instruments_piano2_3mi.toString(),
                    System.currentTimeMillis() - musicStartTime
            ))
            stampVideoView.visibility = View.VISIBLE
            stampVideoView.setVideoPath(filePaths[3])
            stampVideoView.setOnPreparedListener {
                stampVideoView.visibility = View.VISIBLE

                stampVideoView.start()
                stampVideoView.setMediaController(MediaController(this))
            }
            stampVideoView.setOnCompletionListener {
                stampVideoView.visibility = View.INVISIBLE
            }

        })
        videoView5.setOnClickListener({
            stampList.add(Stamp(
                    R.raw.se_maoudamashii_instruments_piano2_3mi.toString(),
                    System.currentTimeMillis() - musicStartTime
            ))
            stampVideoView.visibility = View.VISIBLE
            stampVideoView.setVideoPath(filePaths[4])
            stampVideoView.setOnPreparedListener {
                stampVideoView.visibility = View.VISIBLE

                stampVideoView.start()
                stampVideoView.setMediaController(MediaController(this))
            }
            stampVideoView.setOnCompletionListener {
                stampVideoView.visibility = View.INVISIBLE
            }

        })

        Log.d("MovieEditor", "${shardPreferences.all}")

        backgroundImageView.setOnClickListener({
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, 6542)
        })

        LoadStampMovie()
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
            backgroundImageUrl = "${data.data}"
            Log.d(this::class.java.simpleName, "${data.data}")
            backgroundImageView.setImageURI(data.data)

            LoadStampMovie()
        }

    }

    var filePaths: List<String> = emptyList()

    private fun LoadStampMovie(){
        var videoList = listOf(videoView1, videoView2, videoView3, videoView4, videoView5)
        var count = 0
        var files = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path).listFiles()
        filePaths = files.map { it.path }
        for (i in 0 until files.size) {
            if (files[i].isFile && files[i].name.endsWith(".mp4") && count < 4) {
                videoList[count].setVideoPath(files[i].path)
                videoList[count].setOnPreparedListener(OnPreparedListener { mp -> mp.isLooping = true })
                videoList[count].start()
                count++

                videoList[count].setOnCompletionListener(MediaPlayer.OnCompletionListener {
                    // 先頭に戻す -> Repeat
                    videoList[count].seekTo(0)
                    // 再生開始
                    videoList[count].start()
                })
            }
        }
    }

    private val soundPool = SoundPool.Builder().setMaxStreams(7).build()

}