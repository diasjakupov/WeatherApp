package com.example.weatherapp.ui.weather.future

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.db.models.future.FutureWeatherEntry
import com.example.weatherapp.data.db.provider.UnitSystemProvider
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.repository.RepositoryInterface
import com.example.weatherapp.data.utils.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FutureDetailViewModel(private val repo: Repository, provider: UnitSystemProvider) : ViewModel() {
    val unitSystem=provider.getUnitSystem()

    val weatherLocation by lazyDeferred {
        repo.getWeatherLocation()
    }

    suspend fun getDetailData(date:Long): LiveData<FutureWeatherEntry> {
        return repo.getFutureWeatherByDate(date, unitSystem)
    }
}

class FutureDetailViewModelFactory(
        private val repo: RepositoryInterface,
        private val provider: UnitSystemProvider
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return FutureDetailViewModel(repo as Repository, provider) as T
    }

}