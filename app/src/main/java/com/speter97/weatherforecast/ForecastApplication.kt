package com.speter97.weatherforecast

import android.app.ActionBar
import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.speter97.weatherforecast.data.db.CurrentDatabase
import com.speter97.weatherforecast.data.network.ConnectivityInterceptor
import com.speter97.weatherforecast.data.network.WeatherApiService
import com.speter97.weatherforecast.data.network.WeatherNetworkDataSource
import com.speter97.weatherforecast.data.network.WeatherNetworkDataSourceImpl
import com.speter97.weatherforecast.data.repository.CurrentRepository
import com.speter97.weatherforecast.data.repository.CurrentRepositoryImpl
import com.speter97.weatherforecast.ui.today.CurrentWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class ForecastApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        // get context, services etc from system
        import(androidXModule(this@ForecastApplication))

        // my bindings
        bind() from singleton { CurrentDatabase(instance()) }
        bind() from singleton { instance<CurrentDatabase>().currentWeatherDataDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptor(instance()) }
        bind() from singleton { WeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance())  }
        bind<CurrentRepository>() with singleton { CurrentRepositoryImpl(instance(), instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}