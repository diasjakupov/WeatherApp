package com.example.weatherapp.data.network

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.exceptions.ConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ApiServiceDataSource(
        private val service: WeatherApi,
        private val context:Context
) :ApiServiceI {
    private val _status=MutableLiveData<CodeStatus>()
    override val status: LiveData<CodeStatus>
        get() = _status

    private val _downloadedCurrentWeather=MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather:LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    private val _downloadedFutureWeatherWeather=MutableLiveData<FutureWeatherResponse>()
    override val downloadedFutureWeatherWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeatherWeather

    override suspend fun fetchCurrentWeather(location: Map<String, String>, unitSystem:String, language:String) {
        val currentWeather:CurrentWeatherResponse
        try {
            currentWeather = if(location.size==2){
                service.getCurrentWeather(
                        location["lat"]!!, location["lon"]!!,
                        unitSystem, language).await()
            }else{
                service.getCurrentWeather(location["city"]!!, unitSystem, language).await()
            }
            _downloadedCurrentWeather.postValue(currentWeather)
            _status.postValue(CodeStatus.SUCCESS)
        }catch (e:ConnectivityException){
            _status.postValue(CodeStatus.ERROR)

        }catch (e:HttpException){
            _status.postValue(CodeStatus.ERROR)
            showToastError(location)
        }
    }

    override suspend fun fetchFutureWeather(lat: Double, lon: Double, unitSystem: String, language: String) {
        try {
            val futureWeather=service.getFutureWeatherList(
                    lat = lat.toString(),
                    lon=lon.toString(),
                    system = unitSystem,
                    lang = language
            ).await()
            _status.postValue(CodeStatus.SUCCESS)
            _downloadedFutureWeatherWeather.postValue(futureWeather)
        }catch (e:ConnectivityException){
            _status.postValue(CodeStatus.ERROR)
        }catch (e:HttpException){
            _status.postValue(CodeStatus.ERROR)
            showToastError()
        }
    }

    suspend fun showToastError(location: Map<String, String>){
        withContext(Dispatchers.Main){
            Toast.makeText(context, "${location["city"]} is not found", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun showToastError(){
        withContext(Dispatchers.Main){
            Toast.makeText(context, "NOT FOUND", Toast.LENGTH_SHORT).show()
        }
    }
}