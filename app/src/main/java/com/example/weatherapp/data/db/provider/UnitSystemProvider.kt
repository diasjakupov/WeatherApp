package com.example.weatherapp.data.db.provider

import com.example.weatherapp.data.repository.UnitSystem

interface UnitSystemProvider {
    fun getUnitSystem():UnitSystem
}