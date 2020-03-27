package com.speter97.weatherforecast.data.network

import androidx.lifecycle.LiveData
import com.speter97.weatherforecast.data.network.response.todayEntity.CurrentWeatherData

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherData>

    suspend fun fetchCurrentWeather(
        location: String
    )


}