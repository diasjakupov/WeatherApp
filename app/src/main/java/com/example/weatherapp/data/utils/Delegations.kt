package com.example.weatherapp.data.utils

import android.util.Log
import com.example.weatherapp.data.db.models.WeatherLocation
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*

fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}

fun <T> Task<T>.asDeferredAsync(): Deferred<T> {
    val deferred = CompletableDeferred<T>()

    this.addOnSuccessListener{ result ->
        Log.e("TESTING", "$result result")
        deferred.complete(result)
    }

    this.addOnFailureListener { exception ->
        Log.e("TESTING", "$exception exception")
        deferred.completeExceptionally(exception)
    }

    return deferred
}