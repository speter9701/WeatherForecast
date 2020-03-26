package com.speter97.weatherforecast

import androidx.lifecycle.LiveData

class LocationRepository(private val locationDao: LocationDao) {
    val lastLocation: LiveData<LocationString> = locationDao.getLocation()

    fun insert(latlng: LocationString) {
        locationDao.setLocation(latlng)
    }
}