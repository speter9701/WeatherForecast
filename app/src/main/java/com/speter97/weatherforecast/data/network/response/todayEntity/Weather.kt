package com.speter97.weatherforecast.data.network.response.todayEntity


data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)