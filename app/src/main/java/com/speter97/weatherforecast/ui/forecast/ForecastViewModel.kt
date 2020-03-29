package com.speter97.weatherforecast.ui.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.speter97.weatherforecast.data.db.data.WeatherRepository
import com.speter97.weatherforecast.coroutineHelpers.lazyDeferred

class ForecastViewModel(private val currentRepository: WeatherRepository) : ViewModel() {
    val weatherItems by lazyDeferred { currentRepository.getFutureWeatherList() }
}

class ForecastWeatherViewModelFactory (private val currentRepository: WeatherRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForecastViewModel(currentRepository) as T
    }
}