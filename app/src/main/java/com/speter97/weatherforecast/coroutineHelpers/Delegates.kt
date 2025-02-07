package com.speter97.weatherforecast.coroutineHelpers

import kotlinx.coroutines.*

// Pauses the job to wait for initialization finished
fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}

// https://medium.com/@sampsonjoliver/lazy-evaluated-coroutines-in-kotlin-bf5be004233