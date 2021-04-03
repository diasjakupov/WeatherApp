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

    private val _downloadedFutureWeatherWeather=MutableLiveData<FutureWeatherResponse>()
    override val downloadedFutureWeatherWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeatherWeather

    override suspend fun fetchCurrentWeather(location: Map<String, String>, unitSystem:String, language:String) {
        val currentWeather:CurrentWeatherResponse
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

    override suspend fun fetchFutureWeather(lat: Double, lon: Double, unitSystem: String, language: String) {
        try {
            val futureWeather=service.getFutureWeatherList(
                    lat = lat.toString(),
                    lon=lon.toString(),
                    system = unitSystem,
                    lang = language
            ).await()
            _downloadedFutureWeatherWeather.postValue(futureWeather)
        }catch (e:ConnectivityException){
            Log.e("Connectivity", e.toString())
        }
    }
}