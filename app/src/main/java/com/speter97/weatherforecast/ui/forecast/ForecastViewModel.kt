package com.speter97.weatherforecast.ui.forecast

import androidx.lifecycle.ViewModel
import com.speter97.weatherforecast.data.repository.CurrentRepository
import com.speter97.weatherforecast.internal.lazyDeferred

class ForecastViewModel(private val currentRepository: CurrentRepository) : ViewModel() {
    val weatherItems by lazyDeferred { currentRepository.getFutureWeatherList() }
}