package com.example.satohjohn.kotlinsample.adapter

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.satohjohn.kotlinsample.R
import com.example.satohjohn.kotlinsample.data.CreatedMovie
import java.io.File

class CreatedMovieListAdapter(context: Context, var createdMovieList: List<CreatedMovie>) : BaseAdapter() {

    val inflater: LayoutInflater
    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = convertView
        var holder: MovieHolder? = null

        v?.let {
            holder = it.tag as MovieHolder
        } ?: apply {
            v = inflater.inflate(R.layout.movie_list, null)
            holder = v?.run {
                MovieHolder(
                        this.findViewById(R.id.movie_name_id) as TextView,
                        this.findViewById(R.id.movie_thumbnail) as ImageView)
            }
            v?.tag = holder
        }
        holder?.let {
            it.name.text = createdMovieList.get(position).musicId.toString()
            Log.d(this::class.java.simpleName, "${createdMovieList.get(position).imagePath}")
            it.thumbnail.setImageURI(Uri.fromFile(File(createdMovieList.get(position).imagePath)))
        }
        return v as View
    }

    override fun getItem(position: Int): Any {
        return createdMovieList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return createdMovieList.size
    }

    class MovieHolder(
            val name: TextView,
            val thumbnail: ImageView)
}