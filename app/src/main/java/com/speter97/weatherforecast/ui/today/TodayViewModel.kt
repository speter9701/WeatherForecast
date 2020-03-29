package com.speter97.weatherforecast.ui.today

import androidx.lifecycle.ViewModel
import com.speter97.weatherforecast.data.repository.CurrentRepository
import com.speter97.weatherforecast.internal.lazyDeferred

class TodayViewModel(private val currentRepository: CurrentRepository) : ViewModel() {
    val weather by lazyDeferred { currentRepository.getCurrentWeather() }
}