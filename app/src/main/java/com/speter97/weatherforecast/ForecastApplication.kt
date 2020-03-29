package com.speter97.weatherforecast


import android.app.Application
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.speter97.weatherforecast.data.db.WeatherDatabase
import com.speter97.weatherforecast.data.network.ConnectivityInterceptor
import com.speter97.weatherforecast.data.network.WeatherApiService
import com.speter97.weatherforecast.data.network.WeatherNetworkDataSource
import com.speter97.weatherforecast.data.network.WeatherNetworkDataSourceImpl
import com.speter97.weatherforecast.data.repository.CurrentRepository
import com.speter97.weatherforecast.data.repository.CurrentRepositoryImpl
import com.speter97.weatherforecast.ui.forecast.ForecastWeatherViewModelFactory
import com.speter97.weatherforecast.ui.today.CurrentWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*


class ForecastApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        // get context, services etc from system
        import(androidXModule(this@ForecastApplication))

        // my bindings
        bind() from singleton { WeatherDatabase(instance()) }
        bind() from singleton { instance<WeatherDatabase>().currentDatabaseDao() }
        bind() from singleton { instance<WeatherDatabase>().futureDatabaseDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptor(instance()) }
        bind() from singleton { WeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance())  }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<CurrentRepository>() with singleton { CurrentRepositoryImpl(instance(), instance(),instance(),instance(),instance())}
        bind() from provider { CurrentWeatherViewModelFactory(instance()) }
        bind() from provider { ForecastWeatherViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}