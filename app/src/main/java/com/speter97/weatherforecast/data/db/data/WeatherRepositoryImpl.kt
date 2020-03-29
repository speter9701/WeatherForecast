package com.speter97.weatherforecast.data.db.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.speter97.weatherforecast.data.db.CurrentDatabaseDao
import com.speter97.weatherforecast.data.db.FutureDatabaseDao
import com.speter97.weatherforecast.data.network.WeatherNetworkDataSource
import com.speter97.weatherforecast.coroutineHelpers.asDeferred
import kotlinx.coroutines.*
import org.threeten.bp.Instant
import java.io.IOException

interface WeatherRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeatherData>
    suspend fun getFutureWeatherList(): LiveData<List<FutureWeatherItem>>
}

class WeatherRepositoryImpl(
    private val currentDatabaseDao: CurrentDatabaseDao,
    private val futureDatabaseDao: FutureDatabaseDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : WeatherRepository {
    private val appContext = context.applicationContext

    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather -> persistFetchedCurrentWeather(newCurrentWeather)}
            downloadedFutureWeather.observeForever { newFutureWeather -> persistFetchedFutureWeather(newFutureWeather)}
        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherData> {
        // ALWAYS RETURNS SOMETHING, while launch does not
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentDatabaseDao.getCurrentWeather()
        }
    }

    override suspend fun getFutureWeatherList(): LiveData<List<FutureWeatherItem>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext futureDatabaseDao.getFutureWeather()
        }
    }

    // TODO: WTF
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherData) {
        GlobalScope.launch(Dispatchers.IO) {
            currentDatabaseDao.upsert(fetchedWeather)
        }
    }
    private fun persistFetchedFutureWeather(fetchedWeather: FutureWeatherData) {
        futureDatabaseDao.deleteOld()
        GlobalScope.launch(Dispatchers.IO) {
            futureDatabaseDao.deleteOld()
            val futureWeatherList = fetchedWeather.list
            futureDatabaseDao.insert(futureWeatherList)
        }
    }

    private suspend fun initWeatherData() {
        var dateOfLast = currentDatabaseDao.getCurrentWeatherNonLive()
        if (dateOfLast == null) {
            fetchAllWeather()
        } else {
            val i = Instant.ofEpochSecond(dateOfLast.dt.toLong())
            if (isFetchNeeded(i)) {
                fetchAllWeather()
            }
        }
    }

    private suspend fun fetchAllWeather() {
        val location = getLocationString()
        weatherNetworkDataSource.fetchCurrentWeather(location)
        weatherNetworkDataSource.fetchFutureWeather(location)
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
    private fun isFetchNeeded(lastFetchTime: Instant) : Boolean {
        val i = Instant.now()
        val tenSecondsAgo = i.minusSeconds(20)
        return lastFetchTime.isBefore(tenSecondsAgo)
    }

}

