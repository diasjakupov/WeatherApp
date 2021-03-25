package com.example.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.weatherapp.R
import com.example.weatherapp.data.network.ApiServiceDataSource
import com.example.weatherapp.data.network.ConnectivityInterceptor
import com.example.weatherapp.data.network.WeatherApiBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class currentWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = currentWeatherFragment()
    }

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var txt=view.findViewById<TextView>(R.id.txt)
        val connectivityInterceptor=ConnectivityInterceptor(this.requireContext())
        val service=WeatherApiBuilder.createRetrofit(connectivityInterceptor)
        val dataSource=ApiServiceDataSource(service)

        dataSource.downloadedCurrentWeather.observe(viewLifecycleOwner, {
            txt.setText(it.toString())
        })

        GlobalScope.launch(Dispatchers.Main){
            dataSource.fetchCurrentWeather("Pavlodar")
        }

    }

}