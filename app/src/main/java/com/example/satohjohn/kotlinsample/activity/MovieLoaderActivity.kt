package com.example.satohjohn.kotlinsample.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.satohjohn.kotlinsample.R

class MovieLoaderActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Activity", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stamp_creator)
    }


}