package com.example.weatherapp.data.db.models


import androidx.room.*
import com.google.gson.Gson
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

