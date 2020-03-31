package com.speter97.weatherforecast.ui.forecast

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.speter97.weatherforecast.R
import com.speter97.weatherforecast.data.db.data.FutureWeatherItem
import com.speter97.weatherforecast.coroutineHelpers.ScopedFragment
import kotlinx.android.synthetic.main.fragment_forecast.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ForecastFragment : ScopedFragment(), KodeinAware {

    // Default start
    override val kodein by closestKodein()
    private val viewModelFactory: ForecastWeatherViewModelFactory by instance()
    private lateinit var viewModel: ForecastViewModel
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ForecastViewModel::class.java)
        bindUI()
    }
    // Default end

    //MainDispatcher: only change the main UI when on the Main thread
    private fun bindUI() = launch(Dispatchers.Main) {
        val weatherItems = viewModel.weatherItems.await()

        weatherItems.observe(viewLifecycleOwner, Observer { weatherEntries ->
            if (weatherEntries == null || weatherEntries.isEmpty()) return@Observer

            val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewParent.layoutManager = manager
            recyclerViewParent.setHasFixedSize(true)

            val weather5DaysList = transform3HoursListTo5DaysList(weatherEntries.toList())
            val minMaxTemperatures = getMinMaxTemperature(weather5DaysList.toList())

            val displayMetrics: DisplayMetrics = context!!.resources.displayMetrics
            val dpHeight = displayMetrics.heightPixels / displayMetrics.density
            val myAdapter = MyAdapter(weather5DaysList.toMutableList(), minMaxTemperatures[0], minMaxTemperatures[1], dpHeight*1.4)
            recyclerViewParent.adapter = myAdapter
        })
    }
}

fun transform3HoursListTo5DaysList(weatherEntriesList: List<FutureWeatherItem>) : List<FutureWeatherItem>{
    var toReturn: List<FutureWeatherItem> = emptyList()
    var i = 0
    var bestBeforeId = 0
    var bestBeforeMin = weatherEntriesList[0].main.tempMin
    var bestBeforeWind = weatherEntriesList[0].wind.speed
    weatherEntriesList.forEach {
        if (weatherEntriesList[bestBeforeId].main.tempMax < it.main.tempMax) {
            bestBeforeId = i
        }
        if (bestBeforeMin > it.main.tempMin) {
            bestBeforeMin = it.main.tempMax
        }
        if (bestBeforeWind < it.wind.speed) {
            bestBeforeWind = it.wind.speed
        }
        if (i % 8 == 7) {
            weatherEntriesList[bestBeforeId].main.tempMin = bestBeforeMin
            weatherEntriesList[bestBeforeId].wind.speed = bestBeforeWind
            toReturn += weatherEntriesList[bestBeforeId]

            bestBeforeMin = 100.0
            bestBeforeWind = -1.0
            bestBeforeId = i+1
        }
        i++
    }
    return toReturn
}

fun getMinMaxTemperature(weatherEntriesList: List<FutureWeatherItem>) : Array<Double> {
    var min = +100.0
    var max = -100.0
    weatherEntriesList.forEach {
        if(it.main.tempMax > max)
            max = it.main.tempMax
        if(it.main.tempMin < min)
            min = it.main.tempMin
    }
    return arrayOf(min, max)
}

// ViewModelProviders deprecated https://stackoverflow.com/questions/53903762/viewmodelproviders-is-deprecated-in-1-1-0