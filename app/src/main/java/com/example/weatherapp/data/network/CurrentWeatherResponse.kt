package com.example.weatherapp.data.network

import com.example.weatherapp.data.db.models.CurrentWeather
import com.example.weatherapp.data.db.models.Location
import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(
        @SerializedName("current")
        val currentWeather: CurrentWeather,
        val location: Location
)