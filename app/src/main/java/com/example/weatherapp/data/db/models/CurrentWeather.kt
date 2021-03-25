package com.example.weatherapp.data.db.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

class ListConverter{
    @TypeConverter
    fun fromList(items: List<String>): String{
        return Gson().toJson(items)
    }

    @TypeConverter
    fun toList(json:String): List<String>{
        return Gson().fromJson(json, object: TypeToken<List<String>>() {}.type)
    }
}

const val CURRENT_WEATHER_ID=0

@Entity(tableName = "current_weather")
@TypeConverters(ListConverter::class)
data class CurrentWeather(
    val feelslike: Double,
    @SerializedName("is_day")
    val isDay: String,
    @SerializedName("observation_time")
    val observationTime: String,
    val precip: Double,
    val pressure: Double,
    val temperature: Double,
    val visibility: Double,
    @SerializedName("weather_descriptions")
    val weatherDescriptions: List<String>,
    @SerializedName("weather_icons")
    val weatherIcons: List<String>,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeed: Double
){
    @PrimaryKey(autoGenerate = false)
    var id:Int=CURRENT_WEATHER_ID
}