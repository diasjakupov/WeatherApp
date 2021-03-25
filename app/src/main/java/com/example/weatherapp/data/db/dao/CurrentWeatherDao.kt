package com.example.weatherapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.db.models.CURRENT_WEATHER_ID
import com.example.weatherapp.data.db.models.CurrentWeather
import kotlinx.coroutines.flow.Flow


@Dao
interface CurrentWeatherDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(currentWeather: CurrentWeather)

     @Query("SELECT * FROM current_weather WHERE id = $CURRENT_WEATHER_ID")
     fun getCurrentWeather(): Flow<CurrentWeather>
}