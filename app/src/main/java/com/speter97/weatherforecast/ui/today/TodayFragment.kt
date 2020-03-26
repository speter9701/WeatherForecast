package com.speter97.weatherforecast.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.speter97.weatherforecast.R
import com.speter97.weatherforecast.data.WeatherApiService
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TodayFragment : Fragment() {

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
        viewModel =
            ViewModelProviders.of(this).get(TodayViewModel::class.java)

        val apiService = WeatherApiService()

        GlobalScope.launch(Dispatchers.Main) {
            val currentWeatherData = apiService.getCurrentWeatherData("London")
            text_today.text = currentWeatherData.toString()
        }
    }



}
