package com.example.satohjohn.kotlinsample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.satohjohn.kotlinsample.R
import com.example.satohjohn.kotlinsample.data.Music

class MusicListAdapter(context: Context, var musicList: List<Music>) : BaseAdapter() {

    val inflater: LayoutInflater
    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = convertView
        var holder: MusicHolder? = null

        v?.let {
            holder = it.tag as MusicHolder
        } ?: apply {
            v = inflater.inflate(R.layout.music_list, null)
            holder = v?.run {
                MusicHolder(
                        this.findViewById(R.id.music_icon_image) as ImageView)
            }
            v?.tag = holder
        }
        holder?.let {
            it.iconView.setImageResource(musicList.get(position).imageResource)
        }
        return v as View
    }

    override fun getItem(position: Int): Any {
        return musicList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return musicList.size
    }

    class MusicHolder(
            val iconView: ImageView)
}