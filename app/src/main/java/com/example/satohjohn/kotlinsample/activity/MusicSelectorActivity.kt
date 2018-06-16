package com.example.satohjohn.kotlinsample.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import com.example.satohjohn.kotlinsample.R
import com.example.satohjohn.kotlinsample.adapter.MusicListAdapter

class MusicSelectorActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Activity", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_selector)

        val musicList = listOf(
                MusicListAdapter.Music("運命"),
                MusicListAdapter.Music("やあ"),
                MusicListAdapter.Music("hoge")
        )
        val listView: ListView = findViewById(R.id.music_list) as ListView
        listView.adapter = MusicListAdapter(this, musicList)
    }


}