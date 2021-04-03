package com.example.weatherapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.db.const.CURRENT_WEATHER_ID
import com.example.weatherapp.data.db.models.current.CurrentWeatherEntry



@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weather: CurrentWeatherEntry)

    @Query("SELECT * FROM current_weather WHERE id= $CURRENT_WEATHER_ID")
    fun getCurrentWeather(): LiveData<CurrentWeatherEntry>

    @Query("SELECT * FROM current_weather WHERE id= $CURRENT_WEATHER_ID")
    fun getCurrentWeatherNonLiveFromDb(): CurrentWeatherEntry
}