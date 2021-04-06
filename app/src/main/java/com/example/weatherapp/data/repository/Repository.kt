package com.example.weatherapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherapp.data.db.dao.CurrentWeatherDao
import com.example.weatherapp.data.db.dao.FutureWeatherDao
import com.example.weatherapp.data.db.dao.WeatherLocationDao
import com.example.weatherapp.data.db.models.current.CurrentWeatherEntry
import com.example.weatherapp.data.db.models.WeatherLocation
import com.example.weatherapp.data.db.models.future.FutureWeatherEntry
import com.example.weatherapp.data.network.ApiServiceI
import com.example.weatherapp.data.network.CurrentWeatherResponse
import com.example.weatherapp.data.network.FutureWeatherResponse
import kotlinx.coroutines.*
import org.threeten.bp.ZonedDateTime
import java.util.*

class Repository(
        private val currentWeatherDao: CurrentWeatherDao,
        private val weatherLocationDao:WeatherLocationDao,
        private val futureWeatherDao: FutureWeatherDao,
        private val dataSource: ApiServiceI,
        private val locationProvider: LocationProvider
) : RepositoryInterface{
    private var LocalUnitSystem=UnitSystem.METRIC

    init {
        dataSource.apply {

            downloadedCurrentWeather.observeForever{
                saveCurrentWeather(it)
            }

            downloadedFutureWeatherWeather.observeForever{
                saveFutureWeatherList(it)
            }
        }
    }

    override suspend fun getCurrentWeather(system: UnitSystem): LiveData<CurrentWeatherEntry> {
        return withContext(Dispatchers.IO){
            LocalUnitSystem=system
            initCurrentWeather(system)
            return@withContext currentWeatherDao.getCurrentWeather()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getCurrentLocationFromDb()
        }
    }

    override suspend fun getFutureWeather(system: UnitSystem): LiveData<List<FutureWeatherEntry>> {
        return withContext(Dispatchers.IO){
            initCurrentWeather(system)
            val weatherLocation=weatherLocationDao.getCurrentLocationNonLiveFromDb()
            LocalUnitSystem=system
            return@withContext futureWeatherDao.getFutureWeatherList(
                    weatherLocation.time+weatherLocation.timezone
            )
        }
    }

    override suspend fun getFutureWeatherByDate(date: Long, system: UnitSystem): LiveData<FutureWeatherEntry> {
        return withContext(Dispatchers.IO){
            initCurrentWeather(system)
            return@withContext futureWeatherDao.getFutureWeatherDetail(date)
        }
    }

    private fun deleteWeather(){
        val weatherLocation=weatherLocationDao.getCurrentLocationNonLiveFromDb()
        if(weatherLocation!=null){
            futureWeatherDao.deleteOldEntries()
        }
    }

    private fun saveFutureWeatherList(response:FutureWeatherResponse){
        GlobalScope.launch(Dispatchers.IO){
            deleteWeather()
            val listOfFutureWeatherEntry= arrayListOf<FutureWeatherEntry>()
            for(day in response.daily){
                listOfFutureWeatherEntry.add(
                        FutureWeatherEntry(
                                desc = day.weather.first().main,
                                ic = day.weather.first().icon,
                                dt=day.time,
                                humidity = day.humidity,
                                pressure = day.pressure,
                                temp_max = day.temp.max,
                                temp_min = day.temp.min,
                                feelsLike = day.feelsLike.day,
                                windSpeed = day.windSpeed,
                                system = LocalUnitSystem.name,
                                day_temp = day.temp.day
                        )
                )
            }
            futureWeatherDao.insert(listOfFutureWeatherEntry.toList())
        }
    }

    private fun saveCurrentWeather(weatherResponse: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO){
            val currentWeather= CurrentWeatherEntry(
                    weatherResponse.wind,
                    weatherResponse.main,
                    weatherResponse.weather.first().description,
                    weatherResponse.weather.first().icon,
                    LocalUnitSystem.name
            )

            currentWeatherDao.insert(currentWeather)
            val weatherLocation=WeatherLocation(
                    name=weatherResponse.name,
                    time=weatherResponse.dt,
                    country = weatherResponse.sys.country,
                    timezone = weatherResponse.timezone,
                    lon = weatherResponse.coord.lon,
                    lat = weatherResponse.coord.lat
                )
            weatherLocationDao.insert(weatherLocation)
        }
    }

    private fun hasUnitSystemChanged(system: UnitSystem, lastWeather: CurrentWeatherEntry?):Boolean{
        if (lastWeather == null) return false
        return system!= UnitSystem.valueOf(lastWeather.system)
    }

    private suspend fun initCurrentWeather(system: UnitSystem){
        val lastLocation=weatherLocationDao.getCurrentLocationNonLiveFromDb()
        val nonLiveWeather=currentWeatherDao.getCurrentWeatherNonLiveFromDb()

        if (lastLocation==null ||
                locationProvider.hasLocationChanged(lastLocation) ||
                hasUnitSystemChanged(system, nonLiveWeather)){
            deleteWeather()
            fetchCurrentWeather(system)
            fetchFutureWeather(system)
            return
        }

        if(isNeededToFetch(lastLocation.zonedTime)){
            fetchCurrentWeather(system)
        }

        if(isNeededToFetchFuture(lastLocation.time)){
            deleteWeather()
            fetchFutureWeather(system)
        }
    }

    private suspend fun fetchCurrentWeather(system: UnitSystem){
            dataSource.fetchCurrentWeather(locationProvider.getLocation(),
                    checkUnitSystem(system),
                    Locale.getDefault().language)
    }

    private suspend fun fetchFutureWeather(system: UnitSystem){
        val location=weatherLocationDao.getCurrentLocationNonLiveFromDb()
        if(location!=null){
            dataSource.fetchFutureWeather(
                    lat = location.lat,lon=location.lon,unitSystem = checkUnitSystem(system),Locale.getDefault().language
            )
        }

    }

    private fun checkUnitSystem(system: UnitSystem): String{
        return when(system){
            UnitSystem.METRIC->"metric"
            UnitSystem.IMPERIAL->"imperial"
        }
    }

    private fun isNeededToFetch(lastFetchTime: ZonedDateTime):Boolean{
        val thirtyMinutesAgo=ZonedDateTime.now().minusMinutes(30)
        Log.e("TESTING", "${lastFetchTime.isBefore(thirtyMinutesAgo)} is needed")
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    private fun isNeededToFetchFuture(date:Long):Boolean{
        return futureWeatherDao.countFutureWeather(date) < 7
    }
}
