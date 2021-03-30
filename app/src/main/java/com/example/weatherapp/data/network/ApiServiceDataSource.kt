package com.example.weatherapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.exceptions.ConnectivityException

class ApiServiceDataSource(
        private val service: WeatherApi
) :ApiServiceI {
    private val _downloadedCurrentWeather=MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather:LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather


    override suspend fun fetchCurrentWeather(location: Map<String, String>, unitSystem:String, language:String) {
        val currentWeather:CurrentWeatherResponse
        Log.e("TESTING", location.toString())
        try {
            currentWeather = if(location.size==2){
                service.getCurrentWeather(
                        location["lat"]!!, location["lon"]!!,
                        unitSystem, language).await()
            }else{
                service.getCurrentWeather(location["city"]!!, unitSystem, language).await()
            }
            _downloadedCurrentWeather.postValue(currentWeather)
        }catch (e:ConnectivityException){
            Log.e("Connectivity", e.toString())
        }
    }
}