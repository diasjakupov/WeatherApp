package com.example.weatherapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.weatherapp.data.db.const.CUSTOM_LOCATION
import com.example.weatherapp.data.db.const.USE_LOCATION
import com.example.weatherapp.data.db.models.WeatherLocation
import com.example.weatherapp.data.exceptions.LocationPermissionNotGrantedException

import com.example.weatherapp.data.utils.asDeferredAsync
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.jar.Manifest
import kotlin.math.abs

class LocationProviderImpl(
        private val context:Context,
        private val fusedLocationProviderClient: FusedLocationProviderClient) :
        PreferenceProvider(context), LocationProvider {

    override suspend fun hasLocationChanged(lastLocation: WeatherLocation): Boolean {
        val deviceLocation=
                try{
                    hasDeviceLocationChanged(lastLocation)
                }
                catch (e:LocationPermissionNotGrantedException){
                    false
                }

        return deviceLocation || hasCustomLocationChanged(lastLocation)
    }

    override suspend fun getLocation():Map<String, String> {
        Log.e("TESTING", "getLocation")
        if (isUsingDeviceLocation()) {
            Log.e("TESTING", "getLocation if")
            try {
                val deviceLocation = getLastDeviceLocation().await()
                        ?: return mapOf<String, String>("city" to "${getCustomLocationName()}")
                Log.e("TESTING", "getLocation $deviceLocation")
                return mapOf<String, String>("lat" to "${deviceLocation.latitude}",
                        "lon" to "${deviceLocation.longitude}")
            } catch (e: LocationPermissionNotGrantedException) {
                Log.e("TESTING", "error")
                return mapOf<String, String>("city" to "${getCustomLocationName()}")
            }
        }
        else{
            Log.e("TESTING", "getLocation not use")
            return mapOf<String, String>("city" to "${getCustomLocationName()}")
        }

    }

    private suspend fun hasDeviceLocationChanged(lastLocation: WeatherLocation):Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
        if(deviceLocation==null){
            withContext(Dispatchers.Main){
                Toast.makeText(context,
                        "Please start Google Map to get data about your location", Toast.LENGTH_SHORT).show()
            }
            return false
        }
        Log.e("TESTING","get device")
        val comparisonThreshold = 0.03
        return abs(deviceLocation.latitude - lastLocation.lat) > comparisonThreshold ||
                abs(deviceLocation.longitude - lastLocation.lon) > comparisonThreshold
    }

    private fun isUsingDeviceLocation():Boolean{
        return preferences.getBoolean(USE_LOCATION, true)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation():Deferred<Location?>{
        return if (hasLocationPermission()){
            fusedLocationProviderClient.lastLocation.asDeferredAsync()
        }
        else{
            Log.e("TESTING", "has no permission")
            throw LocationPermissionNotGrantedException()
        }

    }

    private fun hasLocationPermission():Boolean{
        return ContextCompat.checkSelfPermission(
                appContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, "Moscow")
    }

    private fun hasCustomLocationChanged(lastLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName != lastLocation.name
        }
        return false
    }
}