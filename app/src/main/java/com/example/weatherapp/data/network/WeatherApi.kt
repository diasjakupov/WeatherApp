package com.example.weatherapp.data.network

import com.example.weatherapp.data.network.CurrentWeatherResponse
import kotlinx.coroutines.Deferred

import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY="25de1065f8ece06bb89af046000effb4"

interface WeatherApi {

    @GET("weather")
    fun getCurrentWeather(@Query("q") city:String,
                          @Query("units") system:String="metric",
                          @Query("lang") lang:String="en"):
            Deferred<CurrentWeatherResponse>

    @GET("weather")
    fun getCurrentWeather(@Query("lat") lat:String,
                          @Query("lon") lon:String,
                          @Query("units") system:String="metric",
                          @Query("lang") lang:String="en"):
            Deferred<CurrentWeatherResponse>

    //https://api.openweathermap.org/data/2.5/onecall?lat=52.3&lon=76.95&exclude=current,minutely,hourly,alerts&appid=25de1065f8ece06bb89af046000effb4

    @GET("onecall?exclude=current,minutely,hourly,alerts")
    fun getFutureWeatherList(@Query("lat") lat:String,
                             @Query("lon") lon:String,
                             @Query("units") system:String="metric",
                             @Query("lang") lang:String="en"): Deferred<FutureWeatherResponse>
}