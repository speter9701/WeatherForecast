package com.speter97.weatherforecast.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.speter97.weatherforecast.data.db.data.WeatherRepository
import com.speter97.weatherforecast.coroutineHelpers.lazyDeferred

class TodayViewModel(private val currentRepository: WeatherRepository) : ViewModel() {
    val weather by lazyDeferred { currentRepository.getCurrentWeather() }
}

class CurrentWeatherViewModelFactory (private val currentRepository: WeatherRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TodayViewModel(currentRepository) as T
    }
}
