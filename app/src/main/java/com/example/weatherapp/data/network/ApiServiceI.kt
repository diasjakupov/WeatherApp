package com.example.weatherapp.data.network

import androidx.lifecycle.LiveData


interface ApiServiceI {
    val downloadedCurrentWeather:LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(location: String, unitSystem:String, language:String)
}