package com.speter97.weatherforecast.ui.today

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.speter97.weatherforecast.R
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.lang.String.format
import java.time.LocalDate
import java.time.ZoneId


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
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewmodelfactory
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TodayViewModel::class.java)

        bindUI()
    }

    private fun bindUI()  = launch {
        val currentWeather = viewModel.weather.await()
        currentWeather.observe(this@TodayFragment, Observer {
            if (it == null) return@Observer

            val arrayOfColors = context?.resources?.getIntArray(R.array.sunny);
            val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, arrayOfColors)
            background.background = gradient
            updateLocation(it.name, it.dt, it.weather[0].main)
            updateTemperatures(it.main.temp, it.main.tempMin, it.main.tempMax,it.wind.speed)
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

    @SuppressLint("SetTextI18n")
    private fun updateTemperatures(temperature: Double, min: Double, max: Double, wind: Double) {
        text_temperature.text = "${temperature.toInt()}°"
        text_min.text = "${min.toInt()}°"
        text_max.text = "${max.toInt()}°"
        text_windspeed.text = "Wind: $wind km/h"
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun updateOthers(humidity: Int, pressure: Int, sunrise: Int, sunset: Int) {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

        text_humidity.text = "Humidity: $humidity%"
        text_pressure.text = "Pressure: $pressure hPa"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var date = LocalDate.parse("2018-12-12")
            text_sunrise.text = "Sunrise: ${format(java.time.Instant.ofEpochSecond(sunrise.toLong()).toString()).substring(11,16)}"
            text_sunset.text = "Sunset: ${format(java.time.Instant.ofEpochSecond(sunset.toLong()).toString()).substring(11,16)}"
        } else {
            val date1 = java.util.Date((sunrise * 1000).toString())
            val date2 = java.util.Date((sunset * 1000).toString())
            text_sunrise.text = "Sunrise: ${sdf.format(date1).substring(11,16)}"
            text_sunset.text = "Sunset: ${sdf.format(date2).substring(11,16)}"
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateLocation(city: String, date: Int, main: String) {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            text_date.text = format(java.time.Instant.ofEpochSecond(date.toLong()).toString()).substring(0,10)
        } else {
            val date1 = java.util.Date((date * 1000).toString())
            text_date.text = sdf.format(date1).toString().substring(0,10)
        }
        text_city.text = city
        text_main.text = main
    }
}

// Stack: https://stackoverflow.com/questions/57447027/how-to-refresh-viewmodel-data-using-kotlin-lazy-loading-coroutine