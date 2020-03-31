package com.speter97.weatherforecast.ui.today

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

class LocationLifecycleManager(
    lifecycleOwner: LifecycleOwner,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationCallback: LocationCallback
) : LifecycleObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private val locationRequest = LocationRequest().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun addLocationListener() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeLocationListener() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}

// https://github.com/googlecodelabs/android-lifecycles/blob/master/app/src/main/java/com/example/android/lifecycles/step4_solution/BoundLocationManager.java