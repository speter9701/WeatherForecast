package com.speter97.weatherforecast.ui.forecast

import android.graphics.drawable.GradientDrawable
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

class ForecastFragment() : ScopedFragment(), KodeinAware {

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

    //MainDispatcher: only change the main UI when on the Main thread, thats why
    private fun bindUI() = launch(Dispatchers.Main) {
        val weatherItems = viewModel.weatherItems.await()

        val arrayOfColors = context?.resources?.getIntArray(R.array.forecasty);
        val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, arrayOfColors)
        background.background = gradient

        weatherItems.observe(viewLifecycleOwner, Observer { weatherEntries ->
            if (weatherEntries == null) return@Observer
            val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewParent.layoutManager = manager
            recyclerViewParent.setHasFixedSize(true)

            var toReturn: List<FutureWeatherItem> = emptyList()
            var i = 0
            var min = +100.0;
            var max = -100.0;
            var bestBefore = weatherEntries[0]
            var bestBeforeMin = weatherEntries[0].main.tempMin
            var bestBeforeWind = weatherEntries[0].wind.speed
            weatherEntries.forEach {
                if (bestBefore.main.tempMax < it.main.tempMax) {
                    bestBefore = it
                }
                if (bestBeforeMin > it.main.tempMin) {
                    bestBeforeMin = it.main.tempMax
                }
                if (bestBeforeWind < it.wind.speed) {
                    bestBeforeWind = it.wind.speed
                }

                if (i % 8 == 0) {
                    var temp = it
                    // temp.main.tempMin = bestBeforeMin
                    toReturn += it
                    
                    if(it.main.tempMax > max) {
                        max = it.main.tempMax
                    }
                    if(it.main.tempMin < min) {
                        min = it.main.tempMin
                    }
                    bestBefore = it
                }
                i++
            }


            val displayMetrics: DisplayMetrics = context!!.getResources().getDisplayMetrics()
            val dpHeight = displayMetrics.heightPixels / displayMetrics.density
            val myAdapter = MyAdapter(toReturn.toMutableList(), min, max, dpHeight*0.4)
            recyclerViewParent.adapter = myAdapter
        })
    }



}

// ViewModelProviders deprecated https://stackoverflow.com/questions/53903762/viewmodelproviders-is-deprecated-in-1-1-0