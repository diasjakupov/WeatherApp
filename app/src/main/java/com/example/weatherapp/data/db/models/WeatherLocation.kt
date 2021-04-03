package com.example.weatherapp.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.data.db.const.CURRENT_WEATHER_ID
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

@Entity(tableName = "weather_location")
data class WeatherLocation(
        val name:String,
        val time:Long,
        val country:String,
        val timezone:Int,
        val lon:Double,
        val lat:Double
) {
    @PrimaryKey(autoGenerate = false)
    var id:Int= CURRENT_WEATHER_ID

    val zonedTime: ZonedDateTime
        get(){
            val zoneId:Int=timezone
            val time=Instant.ofEpochSecond(time)
            val zonedTime= ZoneId.ofOffset("GMT", ZoneOffset.ofTotalSeconds(zoneId))
            return ZonedDateTime.ofInstant(time, zonedTime)
        }
}