package com.example.weatherapp.data.db.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName



@TypeConverters(ListConverter::class)
data class Wind(
    val deg: Double,
    val speed: Double
)