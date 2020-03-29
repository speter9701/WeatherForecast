package com.speter97.weatherforecast.data.repository

import com.speter97.weatherforecast.data.network.response.CurrentWeatherData
import com.speter97.weatherforecast.data.network.response.FutureWeatherItem
import androidx.lifecycle.LiveData as LiveData

interface CurrentRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeatherData>
    suspend fun getFutureWeatherList(): LiveData<List<FutureWeatherItem>>
}