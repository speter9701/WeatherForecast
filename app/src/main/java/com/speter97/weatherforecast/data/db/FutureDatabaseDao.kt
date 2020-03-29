package com.speter97.weatherforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.speter97.weatherforecast.data.network.response.FutureWeatherItem

@Dao
interface FutureDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherItems: List<FutureWeatherItem>)

    @Query( "select * from future_weather order by dt asc")
    fun getFutureWeather(): LiveData<List<FutureWeatherItem>>

    @Query("delete from future_weather where dt > 0")
    fun deleteOld()
}