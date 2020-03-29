package com.speter97.weatherforecast.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.speter97.weatherforecast.data.db.CurrentWeatherDataDao
import com.speter97.weatherforecast.data.network.WeatherNetworkDataSource
import com.speter97.weatherforecast.data.network.response.todayEntity.CurrentWeatherData
import com.speter97.weatherforecast.internal.asDeferred
import kotlinx.coroutines.*
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import java.io.IOException


class CurrentRepositoryImpl(
    private val currentWeatherDataDao: CurrentWeatherDataDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : CurrentRepository {
    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    private val appContext = context.applicationContext

    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherData> {
        // ALWAYS RETURNS SOMETHING, while launch does not
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDataDao.getCurrentWeather()
        }
    }

    // TODO: WTF
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherData) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDataDao.upsert(fetchedWeather)
        }
    }

    private suspend fun initWeatherData() {
        var dateOfLast = currentWeatherDataDao.getCurrentWeatherNonLive()
        if (dateOfLast == null) {
            fetchCurrentWeather()
        } else {
            val i = Instant.ofEpochSecond(dateOfLast.dt.toLong())
            if (isFetchedCurrentNeeded(i)) {
                fetchCurrentWeather()
            }
        }
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(getLocationString())
    }

    private suspend fun getLocationString() : String {
        try {

            val deviceLocation = getLastDeviceLocation().await()
                ?: return "35,120"
            return "${deviceLocation.latitude},${deviceLocation.longitude}"
        } catch (e: IOException) {
            return "35,120"
        }
    }

    private fun getLastDeviceLocation(): Deferred<Location?> {
        if (hasLocationPermission())
           return fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw IOException()
    }
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun isFetchedCurrentNeeded(lastFetchTime: Instant) : Boolean {
        val i = Instant.now()
        val tenSecondsAgo = i.minusSeconds(10)
        return lastFetchTime.isBefore(tenSecondsAgo)
    }

}

