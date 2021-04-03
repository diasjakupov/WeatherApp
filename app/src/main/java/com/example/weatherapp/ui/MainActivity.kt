package com.example.weatherapp.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.R
import com.example.weatherapp.data.db.const.MY_PERMISSION_ACCESS_COARSE_LOCATION
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class MainActivity() : AppCompatActivity(), setNavigartionTitle, KodeinAware{
    private lateinit var navController:NavController
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private val locationRequest= LocationRequest.create().apply {
        interval=1000
        fastestInterval=1000
        priority= LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }
    override val kodein: Kodein by closestKodein()
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    private val locationCallback=object: LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        navController=Navigation.findNavController(this, R.id.nav_host_fragment)
        toolbar=findViewById(R.id.toolbar)
        bottomNav=findViewById(R.id.bottom_nav)

        bottomNav.setupWithNavController(navController)
        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController)

    }

    override fun onStart() {
        super.onStart()
        requestLocationPermission()
        if(hasLocationPermission()){
            bindLocationManager()
        }else {
            requestLocationPermission()
        }
    }


    private fun bindLocationManager() {
        LocationLifecycleObserver(this, fusedLocationProviderClient, locationCallback)
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                MY_PERMISSION_ACCESS_COARSE_LOCATION)
    }

    private fun hasLocationPermission():Boolean{
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
        )==PackageManager.PERMISSION_GRANTED
    }


    override fun changeNavigationtitle(title:String, subTitle:String?) {
        supportActionBar?.title = title
        supportActionBar?.subtitle = subTitle
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                bindLocationManager()
            }
            else{
                Toast.makeText(
                        this,
                        "Please, set location manually in settings",
                        Toast.LENGTH_LONG).show()
            }

        }
    }
}