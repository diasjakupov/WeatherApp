package com.example.weatherapp.ui.weather.current

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment() : Fragment(), KodeinAware {

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private val viewModel: CurrentWeatherViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()
    }

    private fun bindUI()=lifecycleScope.launch{
        val currentWeather=viewModel.weather.await()
        currentWeather.observe(viewLifecycleOwner, {
            if(it != null)
                Log.d("GET DATA", it.toString())
        })
    }

}