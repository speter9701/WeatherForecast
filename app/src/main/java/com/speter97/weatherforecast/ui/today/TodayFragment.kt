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


class TodayFragment : Fragment() {

    private lateinit var todayViewModel: TodayViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        todayViewModel =
                ViewModelProviders.of(this).get(TodayViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_today, container, false)
        val textView: TextView = root.findViewById(R.id.text_today)
        val latView: TextView = root.findViewById(R.id.latTextView)
        todayViewModel.textToDisplay.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }
}
