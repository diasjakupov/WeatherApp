package com.example.weatherapp.ui


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private lateinit var navController:NavController
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController=Navigation.findNavController(this, R.id.nav_host_fragment)
        toolbar=findViewById(R.id.toolbar)
        bottomNav=findViewById(R.id.bottom_nav)

        bottomNav.setupWithNavController(navController)
        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }


}