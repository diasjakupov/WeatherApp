package com.example.weatherapp.data.db.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.weatherapp.data.db.const.UNIT_SYSTEM
import com.example.weatherapp.data.repository.UnitSystem

class UnitSystemProviderImpl(private val context: Context) : UnitSystemProvider {
    private val appContext=context.applicationContext

    private val preferences:SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)
    override fun getUnitSystem(): UnitSystem {
        val name=preferences.getString(UNIT_SYSTEM, UnitSystem.METRIC.name)
        return UnitSystem.valueOf(name!!)
    }
}