package com.speter97.weatherforecast.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.speter97.weatherforecast.data.network.response.Converters
import com.speter97.weatherforecast.data.network.response.CurrentWeatherData
import com.speter97.weatherforecast.data.network.response.FutureWeatherItem

@Database(entities = [CurrentWeatherData::class, FutureWeatherItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun currentDatabaseDao(): CurrentDatabaseDao
    abstract fun futureDatabaseDao(): FutureDatabaseDao

    companion object {
        @Volatile private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                WeatherDatabase::class.java,
                "current.db").allowMainThreadQueries().build()
    }
}

// Stackoverflow: https://stackoverflow.com/questions/40398072/singleton-with-parameter-in-kotlin