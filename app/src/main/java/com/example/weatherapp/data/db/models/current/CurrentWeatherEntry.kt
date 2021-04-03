package com.example.weatherapp.data.db.models.current


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherapp.data.db.const.CURRENT_WEATHER_ID
import com.example.weatherapp.data.db.models.ListConverter


@Entity(tableName = "current_weather")
@TypeConverters(ListConverter::class)
data class CurrentWeatherEntry(
        @Embedded(prefix = "wind_") val wind: Wind,
        @Embedded(prefix = "main_") val main: Main,
        val description: String,
        val icon: String,
        val system:String
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}