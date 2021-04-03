package com.example.weatherapp.data.network


import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


object WeatherApiBuilder {

    fun createRetrofit(connectivityInterceptor: ConnectivityInterface):WeatherApi{
        val interceptor= Interceptor{
            val url=it.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()
            Log.e("TESTING", url.toString())
            val request=it.request()
                    .newBuilder()
                    .url(url)
                    .build()
            return@Interceptor it.proceed(request)
        }
        val client= OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

        return Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(WeatherApi::class.java)
    }
}