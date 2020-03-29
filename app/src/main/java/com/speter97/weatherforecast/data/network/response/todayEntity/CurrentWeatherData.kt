package com.speter97.weatherforecast.data.network.response.todayEntity

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

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

data class Main(
    @SerializedName("feels_like")
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("temp_min")
    val tempMin: Double
)

data class Sys(
    val country: String,
    val sunrise: Int,
    val sunset: Int
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

data class Wind(
    val deg: Int,
    val speed: Double
)

data class Clouds(
    val all: Int
)

class Converters {

    val gson = Gson()

    @TypeConverter
    fun arrayListToJson(list: List<Weather>?): String? {
        return if(list == null) null else gson.toJson(list)
    }

    @TypeConverter
    fun jsonToArrayList(jsonData: String?): List<Weather>? {
        return if (jsonData == null) null else gson.fromJson(jsonData, object : TypeToken<List<Weather>?>() {}.type)
    }

}

data class Coord(
    val lat: Double,
    val lon: Double
)