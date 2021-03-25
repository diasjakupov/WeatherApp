package com.example.weatherapp.data.network

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.db.models.CurrentWeather


interface ApiServiceI {
    val downloadedCurrentWeather:LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(location: String, unitSystem:Char)
}