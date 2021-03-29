package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.weatherapp.data.db.dao.CurrentWeatherDescDao
import com.example.weatherapp.data.db.dao.WeatherLocationDao
import com.example.weatherapp.data.db.models.CurrentWeather
import com.example.weatherapp.data.db.models.WeatherLocation
import com.example.weatherapp.data.network.ApiServiceI
import com.example.weatherapp.data.network.CurrentWeatherResponse
import kotlinx.coroutines.*
import org.threeten.bp.ZonedDateTime
import java.util.*

class Repository(
        private val currentWeatherDao: CurrentWeatherDescDao,
        private val weatherLocationDao:WeatherLocationDao,
        private val dataSource: ApiServiceI,
        private val locationProvider: LocationProvider
) : RepositoryInterface {

    init {
        dataSource.downloadedCurrentWeather.observeForever{
            saveCurrentWeather(it)
        }
    }

    override suspend fun getCurrentWeather(system: UnitSystem): LiveData<CurrentWeather> {
        return withContext(Dispatchers.IO){
            initCurrentWeather(system)
            return@withContext currentWeatherDao.getCurrentWeather()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getCurrentLocationFromDb()
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
            val weatherLocation=WeatherLocation(
                    weatherResponse.name,
                    weatherResponse.dt,
                    weatherResponse.sys.country,
                    weatherResponse.sys.sunset,
                    weatherResponse.sys.sunrise,
                    weatherResponse.timezone
                )
            weatherLocationDao.insert(weatherLocation)
        }
    }

    private suspend fun initCurrentWeather(system: UnitSystem){
        val lastLocation=weatherLocationDao.getCurrentLocationFromDb().value

        if (lastLocation==null || locationProvider.hasLocationChanged(lastLocation)){
            fetchCurrentWeather(system)
            return
        }
        if(isNeededToFetch(lastLocation.zonedTime))
            fetchCurrentWeather(system)
    }

    private suspend fun fetchCurrentWeather(system: UnitSystem){
        dataSource.fetchCurrentWeather(locationProvider.getLocation(),
                checkUnitSystem(system),
            Locale.getDefault().language)
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
