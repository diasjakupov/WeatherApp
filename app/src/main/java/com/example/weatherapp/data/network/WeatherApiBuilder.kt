package com.example.weatherapp.data.network


import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object WeatherApiBuilder {

    fun createRetrofit(connectivityInterceptor: ConnectivityInterface, context:Context):WeatherApi{
        val interceptor= Interceptor{
            val url=it.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()

            val request=it.request()
                    .newBuilder()
                    .url(url)
                    .build()
            val response=it.proceed(request)
            if(response.code()==404) {
                Log.e("TESTING", "${response.code()}")
            }

            return@Interceptor response
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