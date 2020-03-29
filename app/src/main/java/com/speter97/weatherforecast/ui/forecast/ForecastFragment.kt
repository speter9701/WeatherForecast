package com.speter97.weatherforecast.ui.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.speter97.weatherforecast.R
import com.speter97.weatherforecast.data.network.response.FutureWeatherItem
import com.speter97.weatherforecast.internal.ScopedFragment
import kotlinx.android.synthetic.main.fragment_forecast.*
import kotlinx.android.synthetic.main.item_forecast.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


// ScopedFragments for coroutines: No nullpointer exceptions when the coroutine finished after the fragment already destroyed
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

        weatherItems.observe(viewLifecycleOwner, Observer { weatherEntries ->
            if (weatherEntries == null) return@Observer
            val manager = LinearLayoutManager(activity)
            recyclerViewParent.layoutManager = manager
            recyclerViewParent.setHasFixedSize(true)

            var toReturn: List<FutureWeatherItem> = emptyList()
            var i = 0

            weatherEntries.forEach {
                if (i % 8 == 0) {
                    toReturn += it
                }
                i++
            }
            val myAdapter = MyAdapter(toReturn.toMutableList())
            recyclerViewParent.adapter = myAdapter
        })
    }



}


class MyAdapter(items: MutableList<FutureWeatherItem>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private val mItems: MutableList<FutureWeatherItem> = items

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val main: TextView = view.textview_main
        val date: TextView = view.textview_date
        val temp: TextView = view.textview_temp

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_forecast, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.temp.text = "${item.main.temp.toInt()}°"
        holder.date.text = "${item.dt}°"
        holder.main.text = item.weather[0].main

    }
}
