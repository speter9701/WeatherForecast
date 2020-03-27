package com.speter97.weatherforecast.ui.today

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.speter97.weatherforecast.data.repository.CurrentRepository
import com.speter97.weatherforecast.internal.lazyDeferred

class TodayViewModel(private val currentRepository: CurrentRepository) : ViewModel() {


    val weather by lazyDeferred { currentRepository.getCurrentWeather() }

}