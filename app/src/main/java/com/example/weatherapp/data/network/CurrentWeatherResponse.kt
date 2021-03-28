package com.example.weatherapp.data.network

import com.example.weatherapp.data.db.models.CurrentWeather
import com.example.weatherapp.data.db.models.Main
import com.example.weatherapp.data.db.models.Weather
import com.example.weatherapp.data.db.models.Wind


data class CurrentWeatherResponse(
        val cod: Int,
        val dt: Int,
        val main: Main,
        val name: String,
        val visibility: Int,
        val weather: List<Weather>,
        val wind: Wind
)