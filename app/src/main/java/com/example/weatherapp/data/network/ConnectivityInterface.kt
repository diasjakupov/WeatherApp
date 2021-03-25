package com.example.weatherapp.data.network

import okhttp3.Interceptor
import okhttp3.Response

interface ConnectivityInterface: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response
}