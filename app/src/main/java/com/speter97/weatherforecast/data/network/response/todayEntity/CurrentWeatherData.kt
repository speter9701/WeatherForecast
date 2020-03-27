package com.speter97.weatherforecast.data.network.response.todayEntity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherData(

    @Embedded(prefix = "coord_")
    @SerializedName("coord") val coord: Coord,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("base") val base: String,
    @Embedded(prefix = "main_")
    @SerializedName("main") val main: Main,
    @SerializedName("visibility") val visibility: Int,
    @Embedded(prefix = "wind_")
    @SerializedName("wind") val wind: Wind,
    @Embedded(prefix = "clouds_")
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("dt") val dt: Int,
    @Embedded(prefix = "sys_")
    @SerializedName("sys") val sys: Sys,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("cod") val cod: Int
) {
    @PrimaryKey
    var id_2: Int =
        CURRENT_WEATHER_ID
}