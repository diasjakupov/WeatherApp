package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.db.models.CurrentWeather


interface RepositoryInterface {
    suspend fun getCurrentWeather(system:UnitSystem): LiveData<CurrentWeather>
}