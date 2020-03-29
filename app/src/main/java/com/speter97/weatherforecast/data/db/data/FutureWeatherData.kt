package com.speter97.weatherforecast.data.db.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

data class FutureWeatherData(
    val list: List<FutureWeatherItem>
)

@Entity(tableName = "future_weather")
data class FutureWeatherItem (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @Embedded(prefix = "clouds_")
    val clouds: Clouds,
    val dt: Int,
    @Embedded
    val main: Main,
    val weather: List<Weather1>,
    @Embedded(prefix = "wind_")
    val wind: Wind
)

data class Weather1(
    val main: String
)