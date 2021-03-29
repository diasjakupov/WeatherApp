package com.example.weatherapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.threeten.bp.*
import java.util.*
import kotlin.system.measureTimeMillis


fun main() {
    val zoneId=21600
    val time=Instant.ofEpochSecond(1617007851)
    val test=ZoneId.ofOffset("GMT", ZoneOffset.ofTotalSeconds(zoneId))
    val test2=ZonedDateTime.ofInstant(time, test)
    println(test2)
}