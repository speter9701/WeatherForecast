package com.speter97.weatherforecast.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ForecastViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is forecast Fragment"
    }
    val text: LiveData<String> = _text
}