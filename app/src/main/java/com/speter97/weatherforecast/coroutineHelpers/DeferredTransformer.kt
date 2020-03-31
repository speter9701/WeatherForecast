package com.speter97.weatherforecast.coroutineHelpers

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

// Task to kotlin coroutine deferred (task for suspend methods)
@Suppress("DeferredIsResult")
fun <T> Task<T>.asDeferred(): Deferred<T> {
    val deferred = CompletableDeferred<T>()

    this.addOnSuccessListener { result ->
        deferred.complete(result)
    }

    this.addOnFailureListener { exception ->
        deferred.completeExceptionally(exception)
    }

    return deferred
}

// https://stackoverflow.com/questions/50473637/how-to-transform-an-android-task-to-a-kotlin-deferred