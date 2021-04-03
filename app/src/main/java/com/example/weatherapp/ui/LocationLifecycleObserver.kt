package com.example.weatherapp.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

class LocationLifecycleObserver(
        owner: LifecycleOwner,
        private val client:FusedLocationProviderClient,
        private val callback: LocationCallback
):LifecycleObserver {

    init {
        owner.lifecycle.addObserver(this)
    }

    private val locationRequest=LocationRequest.create().apply {
        interval=1000
        fastestInterval=1000
        priority=LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        Log.e("TESTING", "starting update")
        client.requestLocationUpdates(locationRequest, callback, null)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeLocationUpdates() {
        client.removeLocationUpdates(callback)
    }
}