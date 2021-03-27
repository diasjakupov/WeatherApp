package com.example.weatherapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

suspend fun test1(){
    println("start test1")
    delay(1000L)
    println("finish test1")
}

suspend fun test2(){
    println("start test2")
    delay(2000L)
    println("finish test2")
}

fun main() {
    for(i in (1..10)){
        println(i)
    }
    runBlocking {
        val time = measureTimeMillis {
            test1()
            test2()

        }
        println("Completed in $time ms")
    }
}