package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.weatherapp.data.db.dao.CurrentWeatherDao
import com.example.weatherapp.data.db.models.CurrentWeather
import com.example.weatherapp.data.network.ApiServiceDataSource
import com.example.weatherapp.data.network.ApiServiceI
import com.example.weatherapp.data.network.CurrentWeatherResponse
import kotlinx.coroutines.*
import org.threeten.bp.ZonedDateTime

class Repository(
        private val currentWeatherDao: CurrentWeatherDao,
        private val dataSource: ApiServiceI
) : RepositoryInterface {

    init {
        dataSource.downloadedCurrentWeather.observeForever{
            saveCurrentWeather(it)
        }
    }

    override suspend fun getCurrentWeather(system: UnitSystem): LiveData<CurrentWeather> {
        return withContext(Dispatchers.IO){
            initCurrentWeather(system)
            return@withContext currentWeatherDao.getCurrentWeather().asLiveData()
        }
    }

    private fun saveCurrentWeather(weatherResponse: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO){
            currentWeatherDao.insert(weatherResponse.currentWeather)
        }
    }

    private suspend fun initCurrentWeather(system: UnitSystem){
        if(isNeededToFetch(ZonedDateTime.now().minusHours(1)))
            fetchCurrentWeather(system)
    }

    private suspend fun fetchCurrentWeather(system: UnitSystem){
        dataSource.fetchCurrentWeather("Pavlodar", checkUnitSystem(system))
    }

    private fun checkUnitSystem(system: UnitSystem): Char{
        return when(system){
            UnitSystem.METRIC->'m'
            UnitSystem.IMPERIAL->'f'
        }
    }

    private fun isNeededToFetch(lastFetchTime: ZonedDateTime):Boolean{
        val thirtyMinutesAge=ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAge)
    }
}
