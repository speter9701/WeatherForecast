package com.speter97.weatherforecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.speter97.weatherforecast.data.network.response.todayEntity.CurrentWeatherData
import com.speter97.weatherforecast.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(private val weatherApiService: WeatherApiService) : WeatherNetworkDataSource {

    // live data cannot be change
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherData>()


    override val downloadedCurrentWeather: LiveData<CurrentWeatherData>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String) {

        try {
            val fetchedCurrentWeather = weatherApiService.getCurrentWeatherData(location)
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity","No Internet Connection!")
        }
    }
}