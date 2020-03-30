package com.speter97.weatherforecast.ui.forecast

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.speter97.weatherforecast.R
import com.speter97.weatherforecast.data.db.data.FutureWeatherItem
import kotlinx.android.synthetic.main.item_forecast.view.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime


class MyAdapter(items: MutableList<FutureWeatherItem>, minTemp: Double, maxTemp: Double, maxHeight: Double) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private val mItems: MutableList<FutureWeatherItem> = items
    private val maxT = maxTemp
    private val minT = minTemp
    val multiplier = maxHeight / (maxTemp-minTemp)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.textview_date
        val main: TextView = view.textview_main
        val sun: LottieAnimationView = view.animation_sun
        var max: TextView = view.textview_max
        var min: TextView = view.textview_min
        var wind: LottieAnimationView = view.animation_wind
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
        val instant = Instant.ofEpochSecond(item.dt.toLong())
        val zdt: ZonedDateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
        val dow: DayOfWeek = DayOfWeek.from(zdt)
        holder.date.text = "$dow"
        holder.main.text = item.weather[0].main
        holder.max.text = "${item.main.tempMax.toInt()}°"
        holder.min.text = "${item.main.tempMin.toInt()}°"
        holder.wind.speed = (item.wind.speed / 10).toFloat()

        if (item.clouds.all < 20)
            holder.sun.setAnimation("wsunny.json")
        else if (item.clouds.all < 80)
            holder.sun.setAnimation("wpartlycloudy.json")
        else
            holder.sun.setAnimation("wcloudy.json")
        holder.min.setPadding(0,0,0,(multiplier*(item.main.tempMin-minT)).toInt())
        holder.max.setPadding(0,0,0,(multiplier*(item.main.tempMax-minT)).toInt())

    }
}
