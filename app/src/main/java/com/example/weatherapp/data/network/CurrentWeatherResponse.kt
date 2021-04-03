package com.example.weatherapp.data.network

import com.example.weatherapp.data.db.models.*
import com.example.weatherapp.data.db.models.current.GeneralData
import com.example.weatherapp.data.db.models.current.Main
import com.example.weatherapp.data.db.models.current.Weather
import com.example.weatherapp.data.db.models.current.Wind


data class CurrentWeatherResponse(
        val coord: Coord,
        val dt: Long,
        val main: Main,
        val name: String,
        val weather: List<Weather>,
        val wind: Wind,
        val sys: GeneralData,
        val timezone:Int
)