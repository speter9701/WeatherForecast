package com.speter97.weatherforecast.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.speter97.weatherforecast.data.repository.CurrentRepository

// viewmodelfactory
class CurrentWeatherViewModelFactory (private val currentRepository: CurrentRepository) : ViewModelProvider.NewInstanceFactory() {
   override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return TodayViewModel(currentRepository) as T
   }
}