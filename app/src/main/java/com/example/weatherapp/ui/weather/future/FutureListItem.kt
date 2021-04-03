package com.example.weatherapp.ui.weather.future

import android.widget.TextView
import com.example.weatherapp.R
import com.example.weatherapp.data.db.models.future.FutureWeatherEntry
import com.example.weatherapp.data.network.GlideApp
import com.example.weatherapp.data.repository.UnitSystem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.future_list_item.*
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureListItem(
        val weatherEntry: FutureWeatherEntry
): Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            updateDate()
            updateTemp()
            textView_condition.text=weatherEntry.desc
            updateImage()
        }
    }
    fun ViewHolder.updateDate(){
        val dtformatter=DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        val date=Instant.ofEpochSecond(weatherEntry.dt).atZone(ZoneId.systemDefault()).toLocalDate()
        textView_date.text= date.format(dtformatter)
    }

    fun ViewHolder.updateTemp(){
        val system=when(UnitSystem.valueOf(weatherEntry.system)){
            UnitSystem.METRIC->"°C"
            UnitSystem.IMPERIAL->"°F"
        }
        val text=weatherEntry.day_temp.toString()+system
        textView_temperature.text=text
    }

    fun ViewHolder.updateImage(){
        GlideApp.with(this.containerView)
                .load("http://openweathermap.org/img/wn/${weatherEntry.ic}@2x.png")
                .into(imageView_condition_icon)
    }


    override fun getLayout(): Int = R.layout.future_list_item

}
