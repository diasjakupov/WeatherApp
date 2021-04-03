package com.example.weatherapp.data.db.models.current


import androidx.room.TypeConverters
import com.example.weatherapp.data.db.models.ListConverter


@TypeConverters(ListConverter::class)
data class Wind(
    val deg: Double,
    val speed: Double
)