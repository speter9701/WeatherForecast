package com.speter97.weatherforecast.coroutineHelpers

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

// Fixing the nullPointer exception when the coroutine finished after the fragment destruction (nowwhere to return)
abstract class ScopedFragment : Fragment(), CoroutineScope {
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
    get () = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}

// https://github.com/dmytrodanylyk/coroutine-recipes