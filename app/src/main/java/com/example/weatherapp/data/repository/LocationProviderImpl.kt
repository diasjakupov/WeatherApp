package com.example.weatherapp.data.repository

import com.example.weatherapp.data.db.models.WeatherLocation

class LocationProviderImpl : LocationProvider {
    override fun hasLocationChanged(lastLocation: WeatherLocation): Boolean {
        return true
    }

    override fun getLocation(): String {
        return "Pavlodar"
    }
}