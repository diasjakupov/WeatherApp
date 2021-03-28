package com.example.weatherapp.ui.weather.current

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.weatherapp.R
import com.example.weatherapp.data.network.GlideApp
import com.example.weatherapp.data.repository.UnitSystem
import com.example.weatherapp.ui.LoadingFragment
import com.example.weatherapp.ui.setNavigartionTitle
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.math.roundToInt

class CurrentWeatherFragment() : Fragment(), KodeinAware {
    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private val viewModel: CurrentWeatherViewModel by viewModels {
        viewModelFactory
    }
    private lateinit var weatherDesc:TextView
    private lateinit var temperature:TextView
    private lateinit var feelsLike:TextView
    private lateinit var wind:TextView
    private lateinit var precipitation:TextView
    private lateinit var pressure:TextView
    private lateinit var loadingFragment: LoadingFragment
    private lateinit var icon:ImageView
    private lateinit var bgCurrent: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        temperature=view.findViewById(R.id.textView_temperature)
        feelsLike=view.findViewById(R.id.textView_feels_like_temperature)
        weatherDesc=view.findViewById(R.id.textView_condition)
        wind=view.findViewById(R.id.textView_wind)
        precipitation=view.findViewById(R.id.textView_precipitation)
        pressure=view.findViewById(R.id.textView_pressure)
        loadingFragment= LoadingFragment.newInstance()
        icon=view.findViewById(R.id.imageView_condition_icon)
        bgCurrent=view.findViewById(R.id.bgCurrent)


        bindUI()

    }

    private fun bindUI()=lifecycleScope.launch{
        setLoadingFragment()

        val currentWeather=viewModel.weather.await()

        updateActionBar("Pavlodar", "Today")
        currentWeather.observe(viewLifecycleOwner, {
            if(it != null){
                removeLoadingFragment()
                updateDesc(it.description)
                updateHumidity(it.main.humidity)
                updatePressure(it.main.pressure)
                updateTemperature(it.main.temp, it.main.feelsLike)
                updateWind(it.wind.speed)
                getBitMapIcon("http://openweathermap.org/img/wn/${it.icon}@2x.png")
            }
        })
    }
    private fun getBitMapIcon(url:String){
        GlideApp.with(this@CurrentWeatherFragment)
                .load(url)
                .into(icon)
    }


    private fun setLoadingFragment(){
        childFragmentManager
                .beginTransaction()
                .replace(R.id.loadingFragment, loadingFragment)
                .commit()
    }
    private fun getUnitSystem(metric:String, imperial:String):String{
        return when(viewModel.unitSystem){
            UnitSystem.METRIC->metric
            UnitSystem.IMPERIAL->imperial
        }
    }

    private fun removeLoadingFragment(){
        childFragmentManager
                .beginTransaction()
                .remove(loadingFragment)
                .commit()
    }

    private fun updateActionBar(location:String, subTitle:String){
        (activity as setNavigartionTitle).changeNavigationtitle(location, subTitle)
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperature(temp:Double, feels:Double){
        val system=getUnitSystem("°C", "°F")
        temperature.text = "$temp $system"
        feelsLike.text="Feels like $feels $system"
    }

    private fun updateDesc(desc:String){
        weatherDesc.text = desc.capitalize(Locale.ROOT)
    }

    @SuppressLint("SetTextI18n")
    private fun updateWind(windSpeed: Double){
        val system=getUnitSystem("km/h", "mph")
        wind.text="Wind:$windSpeed$system"
    }

    @SuppressLint("SetTextI18n")
    private fun updateHumidity(humidity: Double){
        precipitation.text="Humidity: $humidity%"
    }

    @SuppressLint("SetTextI18n")
    private fun updatePressure(press:Double){
        val calculatedPressure=(press/1.33).roundToInt()
        pressure.text="Pressure: $calculatedPressure mm"
    }
}