package com.speter97.weatherforecast.data.repository

import androidx.lifecycle.LiveData
import com.speter97.weatherforecast.data.db.CurrentWeatherDataDao
import com.speter97.weatherforecast.data.network.WeatherNetworkDataSource
import com.speter97.weatherforecast.data.network.response.todayEntity.CurrentWeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZonedDateTime

class CurrentRepositoryImpl(
    private val currentWeatherDataDao: CurrentWeatherDataDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : CurrentRepository {
    // IF Fethced -> persist!
    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }
    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherData> {
        // ALWAYS RETURNS SOMETHING, while launch does not (?)
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDataDao.getCurrentWeather()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherData) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDataDao.upsert(fetchedWeather)
        }
    }

    private suspend fun initWeatherData() {
        if (isFetchedCurrentNeeded(ZonedDateTime.now().minusHours(1))) {
            fetchCurrentWeather()
        }
    }

    private suspend fun  fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather("Los Angeles")
    }

    private fun isFetchedCurrentNeeded(lastFetchTime: ZonedDateTime) : Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}