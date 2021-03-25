package com.example.weatherapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.db.dao.CurrentWeatherDao
import com.example.weatherapp.data.db.models.CurrentWeather


@Database(entities = [CurrentWeather::class], version = 1, exportSchema = false)
abstract class RoomDb: RoomDatabase() {
    abstract fun currentDao(): CurrentWeatherDao

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