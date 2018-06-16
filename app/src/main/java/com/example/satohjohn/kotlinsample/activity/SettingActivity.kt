package com.example.satohjohn.kotlinsample.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.satohjohn.kotlinsample.R
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.Spinner

class SettingActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("mainActivity", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val spinner:Spinner = findViewById<View>(R.id.main_melody_spinner) as Spinner
        val spinnerItems = listOf("Spinner", "Spinner 1", "Spinner 2", "Spinner 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.setAdapter(adapter)

        spinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val spinner = parent as Spinner
                val item = spinner.getSelectedItem() as String
                Log.d("SettingActivity", item)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })

        // 画面遷移するよう
        val button = findViewById<View>(R.id.create_button) as Button
        button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }


}