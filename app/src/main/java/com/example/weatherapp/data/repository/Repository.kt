package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.weatherapp.data.db.dao.CurrentWeatherDescDao
import com.example.weatherapp.data.db.models.CurrentWeather
import com.example.weatherapp.data.network.ApiServiceI
import com.example.weatherapp.data.network.CurrentWeatherResponse
import kotlinx.coroutines.*
import org.threeten.bp.ZonedDateTime

class Repository(
        private val currentWeatherDao: CurrentWeatherDescDao,
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
            val currentWeather=CurrentWeather(
                    weatherResponse.wind,
                    weatherResponse.main,
                    weatherResponse.weather.first().description,
                    weatherResponse.weather.first().icon)
            currentWeatherDao.insert(currentWeather)
        }
    }

    private suspend fun initCurrentWeather(system: UnitSystem){
        if(isNeededToFetch(ZonedDateTime.now().minusHours(1)))
            fetchCurrentWeather(system)
    }

    private suspend fun fetchCurrentWeather(system: UnitSystem){
        dataSource.fetchCurrentWeather("Pavlodar", checkUnitSystem(system))
    }

    private fun checkUnitSystem(system: UnitSystem): String{
        return when(system){
            UnitSystem.METRIC->"metric"
            UnitSystem.IMPERIAL->"imperial"
        }
    }

    private fun isNeededToFetch(lastFetchTime: ZonedDateTime):Boolean{
        val thirtyMinutesAge=ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAge)
    }
}
