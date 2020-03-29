package com.speter97.weatherforecast.ui.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.speter97.weatherforecast.data.repository.CurrentRepository

class ForecastWeatherViewModelFactory (private val currentRepository: CurrentRepository) : ViewModelProvider.NewInstanceFactory() {
   override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return ForecastViewModel(currentRepository) as T
   }
}