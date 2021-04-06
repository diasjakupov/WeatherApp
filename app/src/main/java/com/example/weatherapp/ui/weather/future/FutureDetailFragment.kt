package com.example.weatherapp.ui.weather.future

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.data.exceptions.DateNotFoundException
import com.example.weatherapp.data.network.GlideApp
import com.example.weatherapp.data.repository.UnitSystem
import com.example.weatherapp.ui.LoadingFragment
import com.example.weatherapp.ui.setNavigartionTitle
import kotlinx.android.synthetic.main.future_detail_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class FutureDetailFragment : Fragment(), KodeinAware {
    override val kodein:Kodein by closestKodein()

    companion object {
        fun newInstance() = FutureDetailFragment()
    }

    private val viewModelFactory:FutureDetailViewModelFactory by instance()
    private val viewModel: FutureDetailViewModel by viewModels {
        viewModelFactory
    }
    private var date by Delegates.notNull<Long>()
    private lateinit var loadingFragment:LoadingFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        date= arguments?.let { FutureDetailFragmentArgs.fromBundle(it) }?.date ?: throw DateNotFoundException()
        loadingFragment= LoadingFragment.newInstance()

        bindUI()
    }

    private fun bindUI()=lifecycleScope.launchWhenCreated{

        val location=viewModel.weatherLocation.await()
        location.observe(viewLifecycleOwner, {
            if(it!=null){
                updateActionBar(it.name, null)
            }
        })

        viewModel.getDetailData(date).observe(viewLifecycleOwner, {
            setLoadingFragment()
            if(it!=null){
                removeLoadingFragment()
                updateDesc(it.desc)
                updateHumidity(it.humidity)
                updatePressure(it.pressure)
                updateTemperature(it.day_temp, it.temp_min, it.temp_max)
                updateWind(it.windSpeed)
                getBitMapIcon("http://openweathermap.org/img/wn/${it.ic}@2x.png")
            }

        })
    }

    private fun getBitMapIcon(url:String){
        GlideApp.with(this@FutureDetailFragment)
                .load(url)
                .into(imageView_condition_icon)
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

    private fun updateActionBar(location:String, subTitle:String?){
        (activity as setNavigartionTitle).changeNavigationtitle(location, subTitle)
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperature(temp:Double, min:Double, max:Double){
        val system=getUnitSystem("°C", "°F")
        textView_temperature.text = "$temp $system"
        textView_min_max_temperature.text="Min: $min $system, Max: $max $system"
    }

    private fun updateDesc(desc:String){
        textView_condition.text = desc.capitalize(Locale.ROOT)
    }

    @SuppressLint("SetTextI18n")
    private fun updateWind(windSpeed: Double){
        val system=getUnitSystem("km/h", "mph")
        textView_wind.text="Wind:$windSpeed$system"
    }

    @SuppressLint("SetTextI18n")
    private fun updateHumidity(humidity: Double){
        textView_humidity.text="Humidity: $humidity%"
    }

    @SuppressLint("SetTextI18n")
    private fun updatePressure(press:Double){
        val calculatedPressure=(press/1.33).roundToInt()
        textView_pressure.text="Pressure: $calculatedPressure mm"
    }
}