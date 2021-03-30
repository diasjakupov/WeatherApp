package com.example.weatherapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.db.const.CURRENT_WEATHER_ID
import com.example.weatherapp.data.db.models.WeatherLocation
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location:WeatherLocation)

    @Query("SELECT * FROM weather_location WHERE id= $CURRENT_WEATHER_ID")
    fun getCurrentLocationFromDb(): LiveData<WeatherLocation>

    @Query("SELECT * FROM weather_location WHERE id= $CURRENT_WEATHER_ID")
    fun getCurrentLocationNonLiveFromDb(): WeatherLocation
}