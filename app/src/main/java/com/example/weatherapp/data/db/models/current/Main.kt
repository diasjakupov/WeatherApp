package com.example.weatherapp.data.db.models.current


import androidx.room.TypeConverters
import com.example.weatherapp.data.db.models.ListConverter
import com.google.gson.annotations.SerializedName


@TypeConverters(ListConverter::class)
data class Main(
    @SerializedName("feels_like")
    val feelsLike: Double,
    val humidity: Double,
    val pressure: Double,
    val temp: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("temp_min")
    val tempMin: Double
)