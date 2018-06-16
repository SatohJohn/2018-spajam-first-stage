package com.example.satohjohn.kotlinsample.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.satohjohn.kotlinsample.R
import com.example.satohjohn.kotlinsample.adapter.EventAdapter
import com.example.satohjohn.kotlinsample.response.Event
import com.example.satohjohn.kotlinsample.response.EventResponse
import com.example.satohjohn.kotlinsample.service.ConnpassEventsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class TestActivity : AppCompatActivity() {

    val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://connpass.com")
        .addConverterFactory(JacksonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("TestActivity", "on create")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val listView: ListView = findViewById(R.id.event_list)

        val eventsService = retrofit.create(ConnpassEventsService::class.java)

        eventsService.events().enqueue(object : Callback<EventResponse> {
            override fun onFailure(call: Call<EventResponse>?, t: Throwable?) {
                Log.d("TestActivity:request", "fail", t)
                Toast.makeText(this@TestActivity, "エラーだよ", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<EventResponse>?, response: Response<EventResponse>?) {
                Log.d("TestActivity:request", "success")
                response?.let {
                    if (it.isSuccessful) {
                        it.body().apply {
                            Log.d("TestActivity", "event num: ${this?.resultReturned}")
                            this?.events?.forEach { event -> Log.d("TestActivity", "event id: ${event.id}") }

                            listView.adapter = EventAdapter(this@TestActivity, this?.events ?: emptyList())
                        }
                    } else {
                        Toast.makeText(this@TestActivity, "error code: ${it.code()}, body: ${it.errorBody().toString()}", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        })
        Log.d("TestActivity", "end on create")

    }

}