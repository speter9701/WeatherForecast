package com.speter97.weatherforecast.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.speter97.weatherforecast.data.db.data.FutureWeatherItem
import com.speter97.weatherforecast.data.db.data.Weather
import com.speter97.weatherforecast.data.db.data.Weather1

class Converters {

    val gson = Gson()

    // Current items
    @TypeConverter
    fun arrayListToJson(list: List<Weather>?): String? {
        return if(list == null) null else gson.toJson(list)
    }

    @TypeConverter
    fun jsonToArrayList(jsonData: String?): List<Weather>? {
        return if (jsonData == null) null else gson.fromJson(jsonData, object : TypeToken<List<Weather>?>() {}.type)
    }

    // Future items
    @TypeConverter
    fun arrayListToJson1(list: List<FutureWeatherItem>?): String? {
        return if(list == null) null else gson.toJson(list)
    }

    @TypeConverter
    fun jsonToArrayList1(jsonData: String?): List<FutureWeatherItem>? {
        return if (jsonData == null) null else gson.fromJson(jsonData, object : TypeToken<List<FutureWeatherItem>?>() {}.type)
    }

    @TypeConverter
    fun arrayListToJson2(list: List<Weather1>?): String? {
        return if(list == null) null else gson.toJson(list)
    }

    @TypeConverter
    fun jsonToArrayList2(jsonData: String?): List<Weather1>? {
        return if (jsonData == null) null else gson.fromJson(jsonData, object : TypeToken<List<Weather1>?>() {}.type)
    }
}