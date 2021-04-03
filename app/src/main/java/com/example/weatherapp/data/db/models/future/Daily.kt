package com.example.weatherapp.data.db.models.future


import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class Daily(
        @SerializedName("dt")
        val time: Long,
        @SerializedName("feels_like")
        @Embedded(prefix = "feels_like_") val feelsLike: FeelsLike,
        val humidity: Double,
        val pressure: Double,
        val temp: FutureTemp,
        val weather: List<FutureWeatherDesc>,
        @SerializedName("wind_speed")
        val windSpeed: Double
)