package com.speter97.weatherforecast.data.network.response.todayEntity


import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("feels_like")
    val feelsLike: Double? = 0.0,
    val humidity: Int? = 0,
    val pressure: Int? = 0,
    val temp: Double? = 0.0,
    @SerializedName("temp_max")
    val tempMax: Double? = 0.0,
    @SerializedName("temp_min")
    val tempMin: Double? = 0.0
)