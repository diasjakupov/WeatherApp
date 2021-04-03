package com.example.weatherapp.data.network


import com.example.weatherapp.data.db.models.future.Daily
import com.google.gson.annotations.SerializedName

data class FutureWeatherResponse(
        val daily: List<Daily>,
        val lat: Double,
        val lon: Double,
        @SerializedName("timezone_offset")
        val timezoneOffset: Int
)