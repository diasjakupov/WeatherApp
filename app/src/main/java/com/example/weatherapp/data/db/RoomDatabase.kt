package com.example.weatherapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.db.dao.CurrentWeatherDao
import com.example.weatherapp.data.db.dao.FutureWeatherDao
import com.example.weatherapp.data.db.dao.WeatherLocationDao
import com.example.weatherapp.data.db.models.current.CurrentWeatherEntry
import com.example.weatherapp.data.db.models.WeatherLocation
import com.example.weatherapp.data.db.models.future.FutureWeatherEntry


@Database(entities = [CurrentWeatherEntry::class, WeatherLocation::class, FutureWeatherEntry::class], version = 1, exportSchema = false)
abstract class RoomDb: RoomDatabase() {
    abstract fun currentDao(): CurrentWeatherDao
    abstract fun weatherLocationDao(): WeatherLocationDao
    abstract fun futureLocationDao(): FutureWeatherDao

    companion object{
        @Volatile private var instance: RoomDb?=null

         fun getDatabase(context: Context):RoomDb{
             return instance ?: synchronized(this){
                 val db= Room.databaseBuilder(context.applicationContext, RoomDb::class.java,
                         "weather").fallbackToDestructiveMigration().build()
                 instance=db
                 db
             }
         }
    }
}