package com.example.weatherapp.data.network

import com.example.weatherapp.data.network.CurrentWeatherResponse
import kotlinx.coroutines.Deferred

import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY="25de1065f8ece06bb89af046000effb4"

interface WeatherApi {

    @GET("weather")
    fun getCurrentWeather(@Query("q") city:String,
                            @Query("units") metric:String="metric",
    @Query("lang") lang:String="en"):
            Deferred<CurrentWeatherResponse>
}