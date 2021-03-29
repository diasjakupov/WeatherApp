package com.example.weatherapp.data.db.models


import com.google.gson.annotations.SerializedName

data class GeneralData(
    val country: String,
    val sunrise: Int,
    val sunset: Int,
)