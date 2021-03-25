package com.example.weatherapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherapp.data.exceptions.ConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(var context: Context): ConnectivityInterface {
    private val appContext=context.applicationContext
    @RequiresApi(Build.VERSION_CODES.M)
    override fun intercept(chain: Interceptor.Chain): Response {
        val connectivityManager=appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo= connectivityManager.activeNetwork ?: throw ConnectivityException()
        return chain.proceed(chain.request())
    }
}