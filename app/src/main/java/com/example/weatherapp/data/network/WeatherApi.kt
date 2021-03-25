package com.example.weatherapp.data.network

import com.example.weatherapp.data.network.CurrentWeatherResponse
import kotlinx.coroutines.Deferred

import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY="4213808a1c1691a14e761c3791de666f"

interface WeatherApi {

    @GET("current")
    fun getCurrentWeather(@Query("query") city:String,
                            @Query("units") metric:Char='m'):
            Deferred<CurrentWeatherResponse>
}