package com.speter97.weatherforecast.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.speter97.weatherforecast.data.network.response.todayEntity.Converters
import com.speter97.weatherforecast.data.network.response.todayEntity.CurrentWeatherData

@Database(
    entities = [CurrentWeatherData::class],
    version = 1)
@TypeConverters(Converters::class)
abstract class CurrentDatabase : RoomDatabase() {
    abstract fun currentWeatherDataDao(): CurrentWeatherDataDao

    companion object {
        @Volatile private var instance: CurrentDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                CurrentDatabase::class.java,
                "current.db").build()
    }
}