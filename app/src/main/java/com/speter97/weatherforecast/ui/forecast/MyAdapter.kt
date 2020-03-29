package com.speter97.weatherforecast.ui.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.speter97.weatherforecast.R
import com.speter97.weatherforecast.data.db.data.FutureWeatherItem
import kotlinx.android.synthetic.main.item_forecast.view.*


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
