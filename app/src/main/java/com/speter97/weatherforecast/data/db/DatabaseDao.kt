package com.speter97.weatherforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.speter97.weatherforecast.data.db.data.CurrentWeatherData
import com.speter97.weatherforecast.data.db.data.CURRENT_WEATHER_ID
import com.speter97.weatherforecast.data.db.data.FutureWeatherItem

@Dao
interface CurrentDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherData: CurrentWeatherData)

    @Query( "select * from current_weather where id_2 = $CURRENT_WEATHER_ID")
    fun getCurrentWeather(): LiveData<CurrentWeatherData>

    @Query("select * from current_weather where id_2 = $CURRENT_WEATHER_ID")
    fun getCurrentWeatherNonLive(): CurrentWeatherData?
}

@Dao
interface FutureDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherItems: List<FutureWeatherItem>)

    @Query( "select * from future_weather order by dt asc")
    fun getFutureWeather(): LiveData<List<FutureWeatherItem>>


    @Query("delete from future_weather where dt > 0")
    fun deleteOld()
}
