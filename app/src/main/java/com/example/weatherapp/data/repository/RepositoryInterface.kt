package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.db.models.current.CurrentWeatherEntry
import com.example.weatherapp.data.db.models.WeatherLocation
import com.example.weatherapp.data.db.models.future.FutureWeatherEntry


interface RepositoryInterface {
    suspend fun getCurrentWeather(system:UnitSystem): LiveData<CurrentWeatherEntry>

    suspend fun getWeatherLocation():LiveData<WeatherLocation>

    suspend fun getFutureWeather(system: UnitSystem): LiveData<List<FutureWeatherEntry>>
}