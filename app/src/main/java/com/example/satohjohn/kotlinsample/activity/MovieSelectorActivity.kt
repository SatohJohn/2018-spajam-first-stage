package com.example.satohjohn.kotlinsample.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import com.example.satohjohn.kotlinsample.R
import com.example.satohjohn.kotlinsample.adapter.MusicListAdapter
import android.content.Intent
import com.example.satohjohn.kotlinsample.adapter.MovieListAdapter
import com.example.satohjohn.kotlinsample.data.Movie
import com.example.satohjohn.kotlinsample.data.Music

class MovieSelectorActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(this::class.java.simpleName, "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_selector)

        val shardPreferences = this.getSharedPreferences("savedMovie", Context.MODE_PRIVATE)
        val savedMovieMap = shardPreferences.all

        Log.d(this::class.java.simpleName, "${savedMovieMap}")
        val movieList = savedMovieMap.entries.sortedBy { it.key }.map {
            // it.valueをjsonからmovieにする.data.CreatedMovieにはできるはず
            Movie("hogehoge", 1, it.value.toString())
        }
        val listView: ListView = findViewById(R.id.movie_list) as ListView
        listView.adapter = MovieListAdapter(this, movieList)

        listView.setOnItemClickListener {parent, view, position, id ->
            val movie = movieList.get(position)
            val intent = Intent(this, MovieLoaderActivity::class.java)

            intent.putExtra("json", movie.json)
            startActivity(intent)
        }
    }


}