package com.example.satohjohn.kotlinsample.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.satohjohn.kotlinsample.R
import kotlinx.android.synthetic.main.fragment_main.*

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }
}