package com.example.satohjohn.kotlinsample.service

import com.example.satohjohn.kotlinsample.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET

interface ConnpassEventsService {
    @GET("api/v1/event/")
    fun events(): Call<EventResponse>
}