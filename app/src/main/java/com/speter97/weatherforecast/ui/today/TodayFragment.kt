package com.speter97.weatherforecast.ui.today

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.speter97.weatherforecast.R
import com.speter97.weatherforecast.data.network.ConnectivityInterceptor
import com.speter97.weatherforecast.data.network.WeatherApiService
import com.speter97.weatherforecast.data.network.WeatherNetworkDataSourceImpl
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.lang.String.format


// viewmodelfactory aware
class TodayFragment : ScopedFragment(), KodeinAware {

    // viewmodelfactory
    override val kodein by closestKodein()
    private val viewModelFactory : CurrentWeatherViewModelFactory by instance()


    private lateinit var viewModel: TodayViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewmodelfactory
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TodayViewModel::class.java)

        bindUI()
    }
    // ha korábban megszűnik a fragment, mint ahogy ez véget ér -> crash -> localscope
    private fun bindUI()  = launch {
        val currentWeather = viewModel.weather.await()
        currentWeather.observe(this@TodayFragment, Observer {
            if (it == null) return@Observer
            updateLocation(it.name, it.sys.country, it.weather[0].main)
            updateTemperatures(it.main.temp, it.main.feelsLike, it.main.tempMin, it.main.tempMax,it.wind.speed)
            updateOthers(it.main.humidity, it.main.pressure, it.sys.sunrise, it.sys.sunset)
            if (it.clouds.all < 20) {
                animation_viewSunny.visibility = View.VISIBLE
                animation_viewPartly.visibility = View.GONE
                animation_Cloudy.visibility = View.GONE
            }
            else if (it.clouds.all < 80) {
                animation_Cloudy.visibility = View.GONE
                animation_viewPartly.visibility = View.VISIBLE
                animation_viewSunny.visibility = View.GONE
                }
                else {
                animation_viewPartly.visibility = View.GONE
                animation_viewSunny.visibility = View.GONE
                animation_Cloudy.visibility = View.VISIBLE
            }
        })
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double,  min: Double, max: Double, wind: Double) {
        text_temperature.text = "$temperature°C"
        text_feelsLike.text = "Feels like $feelsLike°C"
        text_min.text = "Minimum $min°C"
        text_max.text = "Maximum $max°C"
        text_windspeed.text = "Wind: $wind km/h"
    }
    @SuppressLint("SetTextI18n")
    private fun updateOthers(humidity: Int, pressure: Int, sunrise: Int, sunset: Int) {
        text_humidity.text = "Humidity: $humidity%"
        text_pressure.text = "Pressure: $pressure hPa"


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            text_sunrise.text = "Sunrise: ${format(java.time.Instant.ofEpochSecond(sunrise.toLong()).toString())}"
            text_sunset.text = "Sunset: ${format(java.time.Instant.ofEpochSecond(sunset.toLong()).toString())}"
        }
        else {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val date1 = java.util.Date((sunrise * 1000).toString())
            val date2 = java.util.Date((sunset * 1000).toString())
            text_sunrise.text = "Sunrise: ${ sdf.format(date1)}"
            text_sunset.text = "Sunset: ${sdf.format(date2)}"
        }

    }

    private fun updateLocation(city: String, country: String, main: String) {
        text_city.text = city
        text_country.text = country
        text_main.text = main
    }
}
