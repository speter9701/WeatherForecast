package com.speter97.weatherforecast.ui.today

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
            text_today.text = it.toString()
        })
    }
}
