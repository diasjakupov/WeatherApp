package com.example.weatherapp.data.repository

import com.example.weatherapp.data.db.models.WeatherLocation

interface LocationProvider {

    fun hasLocationChanged(lastLocation:WeatherLocation):Boolean

    fun getLocation():String
}