package com.example.satohjohn.kotlinsample.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class CreatedMovie (
        @JsonProperty("stampList")
        val stampList: List<Stamp>,
        @JsonProperty("imagePath")
        val imagePath: String,
        @JsonProperty("musicId")
        val musicId: Int,
        @JsonProperty("registeredDate")
        val registeredDate: Date = Date()
)

data class Stamp(
        @JsonProperty("id")
        val id: String = "",
        @JsonProperty("time")
        val time: Long = 0, // 開始時間からのmilli sec
        @JsonProperty("x")
        val x: Int = 0,
        @JsonProperty("y")
        val y: Int = 0
)
