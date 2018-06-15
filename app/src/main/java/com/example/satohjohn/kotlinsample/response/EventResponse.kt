package com.example.satohjohn.kotlinsample.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown=true)
data class EventResponse(
        @JsonProperty("results_returned")
        val resultReturned: Long,
        @JsonProperty("events")
        val events: List<Event>
) {
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Event(
        @JsonProperty("event_id")
        val id: Long,
        @JsonProperty("description")
        val description: String
)