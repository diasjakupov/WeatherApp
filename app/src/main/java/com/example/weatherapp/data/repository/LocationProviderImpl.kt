package com.example.weatherapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.weatherapp.data.db.const.CUSTOM_LOCATION
import com.example.weatherapp.data.db.const.USE_LOCATION
import com.example.weatherapp.data.db.models.WeatherLocation
import com.example.weatherapp.data.exceptions.LocationPermissionNotGrantedException
import com.example.weatherapp.data.utils.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import java.util.jar.Manifest
import kotlin.math.abs

class LocationProviderImpl(
        context:Context,
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
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation().await()
                        ?: return mapOf<String, String>("city" to "${getCustomLocationName()}")
                return mapOf<String, String>("lat" to "${deviceLocation.latitude}",
                        "lon" to "${deviceLocation.longitude}")
            } catch (e: LocationPermissionNotGrantedException) {
                return mapOf<String, String>("city" to "${getCustomLocationName()}")
            }
        }
        else
            return mapOf<String, String>("city" to "${getCustomLocationName()}")
    }


    private suspend fun hasDeviceLocationChanged(lastLocation: WeatherLocation):Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
                ?: return false

        val comparisonThreshold = 0.03
        return abs(deviceLocation.latitude - lastLocation.lat) > comparisonThreshold &&
                abs(deviceLocation.longitude - lastLocation.lon) > comparisonThreshold
    }

    private fun isUsingDeviceLocation():Boolean{
        return preferences.getBoolean(USE_LOCATION, true)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation():Deferred<Location?>{
        return if (hasLocationPermission()) fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission():Boolean{
        return ContextCompat.checkSelfPermission(
                appContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, "Pavlodar")
    }

    private fun hasCustomLocationChanged(lastLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName != lastLocation.name
        }
        return false
    }
}