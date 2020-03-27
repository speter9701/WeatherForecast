package com.speter97.weatherforecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.speter97.weatherforecast.data.network.response.todayEntity.CurrentWeatherData
import java.io.IOException

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherData>

    suspend fun fetchCurrentWeather(
        location: String
    )
}

class WeatherNetworkDataSourceImpl(private val weatherApiService: WeatherApiService) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherData>()

    override val downloadedCurrentWeather: LiveData<CurrentWeatherData>
        get() = _downloadedCurrentWeather

    // TODO ( "WTF??")
    override suspend fun fetchCurrentWeather(location: String) {
        try {
            val fetchedCurrentWeather = weatherApiService.getCurrentWeatherData(location)
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: IOException) {
            Log.e("Connectivity", "No Internet Connection!")
        }
    }
}