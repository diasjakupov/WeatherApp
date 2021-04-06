package com.example.weatherapp.ui.weather.future

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.db.provider.UnitSystemProvider
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.repository.RepositoryInterface
import com.example.weatherapp.data.utils.lazyDeferred
import com.example.weatherapp.ui.weather.current.CurrentWeatherViewModel

class FutureListViewModel(private val repo:Repository, provider:UnitSystemProvider) : ViewModel() {
    val unitSystem=provider.getUnitSystem()

    val futureWeather by lazyDeferred {
        repo.getFutureWeather(unitSystem)
    }

    val weatherLocation by lazyDeferred {
        repo.getWeatherLocation()
    }
}

class FutureListViewModelFactory(
        private val repo: RepositoryInterface,
        private val provider: UnitSystemProvider
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return FutureListViewModel(repo as Repository, provider) as T
    }

}