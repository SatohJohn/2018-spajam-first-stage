package com.example.satohjohn.kotlinsample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.satohjohn.kotlinsample.R
import com.example.satohjohn.kotlinsample.response.Event

class EventAdapter(val context: Context, var events: List<Event>) : BaseAdapter() {

    val inflater: LayoutInflater

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    /**
     * position: listの要素の番号
     * convertView:
     * parent: list自体
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = convertView
        var holder: EventHolder? = null

        v?.let {
            holder = it.tag as EventHolder
        } ?: apply {
            v = inflater.inflate(R.layout.event_list, null)
            holder = v?.run {
                EventHolder(
                        this.findViewById(R.id.event_id) as TextView,
                        this.findViewById(R.id.event_description) as TextView)
            }
            v?.tag = holder
        }
        holder?.let {
            it.idView.text = events.get(position).id.toString()
            it.descriptionView.text = events.get(position).description
        }
        return v as View
    }

    override fun getItem(position: Int): Any {
        return events.get(position)
    }

    override fun getItemId(position: Int): Long {
        return events.get(position).id
    }

    override fun getCount(): Int {
        return events.size
    }

    data class EventHolder(
            val idView: TextView,
            val descriptionView: TextView)
}