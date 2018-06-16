package com.example.satohjohn.kotlinsample.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import com.example.satohjohn.kotlinsample.R
import com.example.satohjohn.kotlinsample.adapter.MusicListAdapter
import android.content.Intent
import com.example.satohjohn.kotlinsample.data.Music

class MusicSelectorActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Activity", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_selector)

        val musicList = listOf(
                Music("運命", R.raw.destiny),
                Music("やあ", R.raw.destiny),
                Music("hoge", R.raw.destiny)
        )
        val listView: ListView = findViewById(R.id.music_list) as ListView
        listView.adapter = MusicListAdapter(this, musicList)

        listView.setOnItemClickListener {parent, view, position, id ->
            val music = musicList.get(position)
            val intent = Intent(this, MovieEditorActivity::class.java)

            intent.putExtra("rawId", music.resourceId)
            startActivity(intent)
        }
    }


}