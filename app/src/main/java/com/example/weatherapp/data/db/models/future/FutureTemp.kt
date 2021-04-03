package com.example.weatherapp.data.db.models.future


import com.google.gson.annotations.SerializedName

data class FutureTemp(
    val max: Double,
    val min: Double,
    val day:Double
)