package com.example.weatherapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.db.models.future.FutureWeatherEntry

@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeather:List<FutureWeatherEntry>)

    @Query("SELECT * FROM future_weather WHERE dt >= :date")
    fun getFutureWeatherList(date:Long):LiveData<List<FutureWeatherEntry>>

    @Query("select count(id) from future_weather where dt >= :date")
    fun countFutureWeather(date:Long): Int

    @Query("delete from future_weather where dt < :date")
    fun deleteOldEntries(date:Long)
}