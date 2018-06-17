package com.example.satohjohn.kotlinsample.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import com.example.satohjohn.kotlinsample.R
import android.content.Intent
import android.widget.GridView
import com.example.satohjohn.kotlinsample.adapter.CreatedMovieListAdapter
import com.example.satohjohn.kotlinsample.data.CreatedMovie
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class MovieSelectorActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(this::class.java.simpleName, "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_selector)

        val shardPreferences = this.getSharedPreferences("savedMovie", Context.MODE_PRIVATE)
        val savedMovieMap = shardPreferences.all

        val mapper = jacksonObjectMapper()

        Log.d(this::class.java.simpleName, "${savedMovieMap}")
        val movieList = savedMovieMap.entries.sortedBy { it.key }.map {
            mapper.readValue(it.value.toString()) as CreatedMovie
        }
        val gridView: GridView = findViewById(R.id.movie_list) as GridView
        gridView.adapter = CreatedMovieListAdapter(this, movieList)

        gridView.setOnItemClickListener {parent, view, position, id ->
            val movie = movieList.get(position)
            val intent = Intent(this, MovieLoaderActivity::class.java)
            intent.putExtra("json", mapper.writeValueAsString(movie))
            startActivity(intent)
        }
    }


}