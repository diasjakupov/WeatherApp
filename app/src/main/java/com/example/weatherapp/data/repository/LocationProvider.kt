package com.example.weatherapp.data.repository

import com.example.weatherapp.data.db.models.WeatherLocation

interface LocationProvider {

    suspend fun hasLocationChanged(lastLocation:WeatherLocation):Boolean

    suspend fun getLocation():Map<String, String>
}