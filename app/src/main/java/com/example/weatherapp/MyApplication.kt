package com.example.weatherapp


import androidx.multidex.MultiDexApplication
import com.example.weatherapp.data.db.RoomDb
import com.example.weatherapp.data.network.*
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.repository.RepositoryInterface
import com.example.weatherapp.ui.weather.current.CurrentWeatherViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MyApplication() : MultiDexApplication(), KodeinAware {
    override val kodein= Kodein.lazy {
        import(androidXModule(this@MyApplication))

        bind() from singleton { RoomDb.getDatabase(instance()) }
        bind() from singleton { instance<RoomDb>().currentDao() }
        bind<ConnectivityInterface>() with singleton {ConnectivityInterceptor(instance())}
        bind() from singleton { WeatherApiBuilder.createRetrofit(instance()) }
        bind<ApiServiceI>() with singleton { ApiServiceDataSource(instance()) }
        bind<RepositoryInterface>() with singleton { Repository(instance(), instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}