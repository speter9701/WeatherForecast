package com.speter97.weatherforecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.speter97.weatherforecast.data.db.data.CurrentWeatherData
import com.speter97.weatherforecast.data.db.data.FutureWeatherData
import java.io.IOException

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherData>
    val downloadedFutureWeather: LiveData<FutureWeatherData>

    suspend fun fetchCurrentWeather(
        location: String
    )
    suspend fun fetchFutureWeather(
        location: String
    )
}

class WeatherNetworkDataSourceImpl(private val weatherApiService: WeatherApiService) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherData>()
    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherData>()

    override val downloadedCurrentWeather: LiveData<CurrentWeatherData>
        get() = _downloadedCurrentWeather
    override val downloadedFutureWeather: LiveData<FutureWeatherData>
        get() = _downloadedFutureWeather

    // TODO ( "WTF??")
    override suspend fun fetchCurrentWeather(location: String) {
        var latlng = location.split(',')
        try {
            val fetchedCurrentWeather = weatherApiService.getCurrentWeatherData(latlng[0],latlng[1])
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: IOException) {
            Log.e("Connectivity", "No Internet Connection!")
        }
    }

    override suspend fun fetchFutureWeather(location: String) {
        var latlng = location.split(',')
        try {
            val fetchedFutureWeather = weatherApiService.getFutureWeatherData(latlng[0],latlng[1])
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        } catch (e: IOException) {
            Log.e("Connectivity", "No Internet Connection!")
        }
    }
}