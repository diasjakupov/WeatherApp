package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.db.models.CurrentWeather
import com.example.weatherapp.data.db.models.WeatherLocation


interface RepositoryInterface {
    suspend fun getCurrentWeather(system:UnitSystem): LiveData<CurrentWeather>

    suspend fun getWeatherLocation():LiveData<WeatherLocation>
}