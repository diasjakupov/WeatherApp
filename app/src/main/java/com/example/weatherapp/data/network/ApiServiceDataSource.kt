package com.example.weatherapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.db.models.CurrentWeather
import com.example.weatherapp.data.exceptions.ConnectivityException

class ApiServiceDataSource(
        private val service: WeatherApi
) :ApiServiceI {
    private val _downloadedCurrentWeather=MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather:LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather


    override suspend fun fetchCurrentWeather(location: String, unitSystem:Char) {
        try {
            val currentWeather=service.getCurrentWeather(location, unitSystem).await()
            _downloadedCurrentWeather.postValue(currentWeather)
        }catch (e:ConnectivityException){
            Log.e("Connectivity", e.toString())
        }
    }
}