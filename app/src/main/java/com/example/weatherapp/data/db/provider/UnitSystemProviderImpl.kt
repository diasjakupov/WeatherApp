package com.example.weatherapp.data.db.provider

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.weatherapp.data.db.const.UNIT_SYSTEM
import com.example.weatherapp.data.repository.PreferenceProvider
import com.example.weatherapp.data.repository.UnitSystem

class UnitSystemProviderImpl(context: Context) : PreferenceProvider(context), UnitSystemProvider {

    override fun getUnitSystem(): UnitSystem {
        val name=preferences.getString(UNIT_SYSTEM, UnitSystem.METRIC.name)
        if (name != null) {
            Log.e("TESTING", name)
        }
        return UnitSystem.valueOf(name!!)
    }
}