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
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import java.lang.String.format
import java.time.LocalDate


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


            val date = Instant.ofEpochSecond(it.dt.toLong()).toString().substring(0,10)
            val sunrise = Instant.ofEpochSecond(it.sys.sunrise.toLong()).toString().substring(11,16)
            val sunset = Instant.ofEpochSecond(it.sys.sunset.toLong()).toString().substring(11,16)
            updateView(it.name, date, it.weather[0].main, it.main.temp, it.main.tempMin, it.main.tempMax,it.wind.speed, it.main.humidity, it.main.pressure, sunrise,sunset)

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
    private fun updateView(city: String, date: String, main: String, temperature: Double, min: Double, max: Double, wind: Double, humidity: Int, pressure: Int, sunrise: String, sunset: String ) {

        text_city.text = city
        text_date.text = date
        text_main.text = main
        text_temperature.text = "${temperature.toInt()}°"
        text_min.text = "${min.toInt()}°"
        text_max.text = "${max.toInt()}°"
        text_windspeed.text = "Wind: $wind km/h"
        text_humidity.text = "Humidity: $humidity%"
        text_pressure.text = "Pressure: $pressure hPa"
        text_sunrise.text = "Sunrise: ${sunrise}"
        text_sunset.text = "Sunset: ${sunset}"
    }
}

// Stack: https://stackoverflow.com/questions/57447027/how-to-refresh-viewmodel-data-using-kotlin-lazy-loading-coroutine