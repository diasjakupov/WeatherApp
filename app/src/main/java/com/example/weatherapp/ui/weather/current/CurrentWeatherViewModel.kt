package com.example.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.db.provider.UnitSystemProvider
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.repository.RepositoryInterface
import com.example.weatherapp.data.repository.UnitSystem
import com.example.weatherapp.data.utils.lazyDeferred

class CurrentWeatherViewModel(private val repo:Repository, provider:UnitSystemProvider) : ViewModel() {
    val unitSystem=provider.getUnitSystem()

    val weather by lazyDeferred {
        repo.getCurrentWeather(unitSystem)
    }

    val location by lazyDeferred {
        repo.getWeatherLocation()
    }
}

class CurrentWeatherViewModelFactory(
        private val repo:RepositoryInterface,
        private val provider:UnitSystemProvider
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CurrentWeatherViewModel(repo as Repository, provider) as T
    }

}