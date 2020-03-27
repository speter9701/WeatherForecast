package com.speter97.weatherforecast.data.repository

import com.speter97.weatherforecast.data.network.response.todayEntity.CurrentWeatherData
import androidx.lifecycle.LiveData as LiveData

interface CurrentRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeatherData>
}