package com.example.weatherapp.data.db.models.future

import androidx.room.*
import com.example.weatherapp.data.db.models.ListConverter

@Entity(tableName = "future_weather", indices = [Index(value = ["dt"], unique = true)])
@TypeConverters(ListConverter::class)
class FutureWeatherEntry(
        val desc:String,
        val ic:String,
        val dt:Long,
        val humidity: Double,
        val pressure: Double,
        val temp_max: Double,
        val temp_min: Double,
        val day_temp: Double,
        val feelsLike: Double,
        val windSpeed: Double,
        val system:String
){
        @PrimaryKey(autoGenerate = true)
        var id:Int=0
}