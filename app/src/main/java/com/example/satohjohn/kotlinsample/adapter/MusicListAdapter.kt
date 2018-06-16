package com.example.satohjohn.kotlinsample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.satohjohn.kotlinsample.R

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
                        this.findViewById(R.id.music_name_id) as TextView)
            }
            v?.tag = holder
        }
        holder?.let {
            it.nameView.text = musicList.get(position).name
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

    data class Music(
            val name: String,
            val resourceId: Int
    )

    class MusicHolder(
            val nameView: TextView)
}