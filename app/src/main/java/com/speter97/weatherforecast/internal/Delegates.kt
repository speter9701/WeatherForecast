package com.speter97.weatherforecast.internal

import android.provider.Settings
import kotlinx.coroutines.*


fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}

// https://medium.com/@sampsonjoliver/lazy-evaluated-coroutines-in-kotlin-bf5be004233