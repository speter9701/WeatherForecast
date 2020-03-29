package com.speter97.weatherforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.speter97.weatherforecast.data.network.response.todayEntity.CurrentWeatherData
import com.speter97.weatherforecast.data.network.response.todayEntity.CURRENT_WEATHER_ID

@Dao
interface CurrentWeatherDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherData: CurrentWeatherData)

    @Query( "select * from current_weather where id_2 = $CURRENT_WEATHER_ID")
    fun getCurrentWeather(): LiveData<CurrentWeatherData>

    @Query("select * from current_weather where id_2 = $CURRENT_WEATHER_ID")
    fun getCurrentWeatherNonLive(): CurrentWeatherData?
}