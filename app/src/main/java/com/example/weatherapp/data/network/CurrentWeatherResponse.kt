package com.example.weatherapp.data.network

import com.example.weatherapp.data.db.models.*


data class CurrentWeatherResponse(
        val cod: Int,
        val coord: Coord,
        val dt: Long,
        val main: Main,
        val name: String,
        val weather: List<Weather>,
        val wind: Wind,
        val sys:GeneralData,
        val timezone:Int
)