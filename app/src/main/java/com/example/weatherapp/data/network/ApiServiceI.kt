package com.example.weatherapp.data.network

import androidx.lifecycle.LiveData


interface ApiServiceI {
    val downloadedCurrentWeather:LiveData<CurrentWeatherResponse>
    val downloadedFutureWeatherWeather:LiveData<FutureWeatherResponse>
    val status:LiveData<CodeStatus>

    suspend fun fetchCurrentWeather(location: Map<String, String>, unitSystem:String, language:String)
    suspend fun fetchFutureWeather(lat:Double, lon:Double, unitSystem:String, language:String)
}