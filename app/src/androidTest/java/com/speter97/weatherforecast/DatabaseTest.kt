package com.speter97.weatherforecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.speter97.weatherforecast.data.db.DatabaseObj
import com.speter97.weatherforecast.data.db.data.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class DatabaseTests {
    private var db: DatabaseObj? = null
    var testData: List<FutureWeatherItem> = listOf(
        FutureWeatherItem(id=6281, clouds= Clouds(all=0), dt=1585677600, main= Main(feelsLike=-2.92, humidity=66, pressure=1024, temp=1.72, tempMax=2.43, tempMin=1.72), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=3.07)
        ),
        FutureWeatherItem(id=6282, clouds= Clouds(all=0), dt=1585688400, main= Main(feelsLike=-4.8, humidity=75, pressure=1025, temp=-0.3, tempMax=0.23, tempMin=-0.3), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=2.83)
        ),
        FutureWeatherItem(id=6283, clouds= Clouds(all=0), dt=1585699200, main= Main(feelsLike=-5.21, humidity=79, pressure=1025, temp=-1.24, tempMax=-0.88, tempMin=-1.24), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=2.04)
        ),
        FutureWeatherItem(id=6284, clouds= Clouds(all=0), dt=1585710000, main= Main(feelsLike=-5.32, humidity=79, pressure=1024, temp=-1.34, tempMax=-1.16, tempMin=-1.34), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=2.03)
        ),
        FutureWeatherItem(id=6285, clouds= Clouds(all=0), dt=1585720800, main= Main(feelsLike=-2.3, humidity=68, pressure=1024, temp=1.49, tempMax=1.49, tempMin=1.49), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=1.88)
        ),
        FutureWeatherItem(id=6286, clouds= Clouds(all=11), dt=1585731600, main= Main(feelsLike=2.21, humidity=48, pressure=1022, temp=6.09, tempMax=6.09, tempMin=6.09), weather=listOf(
            Weather1(main="Clouds")
        ), wind= Wind(speed=1.95)
        ),
        FutureWeatherItem(id=6287, clouds= Clouds(all=8), dt=1585742400, main= Main(feelsLike=3.98, humidity=42, pressure=1021, temp=7.84, tempMax=7.84, tempMin=7.84), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=1.9)
        ),
        FutureWeatherItem(id=6288, clouds= Clouds(all=5), dt=1585753200, main= Main(feelsLike=4.35, humidity=43, pressure=1018, temp=7.59, tempMax=7.59, tempMin=7.59), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=1.03)
        ),
        FutureWeatherItem(id=6289, clouds= Clouds(all=32), dt=1585764000, main= Main(feelsLike=0.94, humidity=57, pressure=1019, temp=3.67, tempMax=3.67, tempMin=3.67), weather=listOf(
            Weather1(main="Clouds")
        ), wind= Wind(speed=0.32)
        ),
        FutureWeatherItem(id=6290, clouds= Clouds(all=8), dt=1585774800, main= Main(feelsLike=-1.08, humidity=64, pressure=1019, temp=2.23, tempMax=2.23, tempMin=2.23), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=1.17)
        ),
        FutureWeatherItem(id=6291, clouds= Clouds(all=15), dt=1585785600, main= Main(feelsLike=-2.7, humidity=71, pressure=1019, temp=0.87, tempMax=0.87, tempMin=0.87), weather=listOf(
            Weather1(main="Clouds")
        ), wind= Wind(speed=1.56)
        ),
        FutureWeatherItem(id=6292, clouds= Clouds(all=4), dt=1585796400, main= Main(feelsLike=-3.71, humidity=75, pressure=1019, temp=0.14, tempMax=0.14, tempMin=0.14), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=1.96)
        ),
        FutureWeatherItem(id=6293, clouds= Clouds(all=2), dt=1585807200, main= Main(feelsLike=-1.12, humidity=64, pressure=1019, temp=2.86, tempMax=2.86, tempMin=2.86), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=2.24)
        ),
        FutureWeatherItem(id=6294, clouds= Clouds(all=0), dt=1585818000, main= Main(feelsLike=3.31, humidity=47, pressure=1017, temp=7.93, tempMax=7.93, tempMin=7.93), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=3.25)
        ),
        FutureWeatherItem(id=6295, clouds= Clouds(all=0), dt=1585828800, main= Main(feelsLike=5.1, humidity=42, pressure=1016, temp=10.01, tempMax=10.01, tempMin=10.01), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=3.73)
        ),
        FutureWeatherItem(id=6297, clouds= Clouds(all=0), dt=1585850400, main= Main(feelsLike=1.49, humidity=59, pressure=1015, temp=5.3, tempMax=5.3, tempMin=5.3), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=2.21)
        ),
        FutureWeatherItem(id=6296, clouds= Clouds(all=0), dt=1585839600, main= Main(feelsLike=5.27, humidity=43, pressure=1014, temp=9.85, tempMax=9.85, tempMin=9.85), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=3.29)
        ),
        FutureWeatherItem(id=6298, clouds= Clouds(all=0), dt=1585861200, main= Main(feelsLike=-0.3, humidity=66, pressure=1015, temp=3.78, tempMax=3.78, tempMin=3.78), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=2.6)
        ),
        FutureWeatherItem(id=6299, clouds= Clouds(all=0), dt=1585872000, main= Main(feelsLike=-1.85, humidity=74, pressure=1015, temp=2.45, tempMax=2.45, tempMin=2.45), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=2.97)
        ),
        FutureWeatherItem(id=6300, clouds= Clouds(all=0), dt=1585882800, main= Main(feelsLike=-2.69, humidity=80, pressure=1014, temp=1.55, tempMax=1.55, tempMin=1.55), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=2.92)
        ),
        FutureWeatherItem(id=6301, clouds= Clouds(all=0), dt=1585893600, main= Main(feelsLike=-0.5, humidity=68, pressure=1015, temp=4.11, tempMax=4.11, tempMin=4.11), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=3.49)
        ),
        FutureWeatherItem(id=6302, clouds= Clouds(all=0), dt=1585904400, main= Main(feelsLike=4.77, humidity=51, pressure=1014, temp=10.21, tempMax=10.21, tempMin=10.21), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=5.05)
        ),
        FutureWeatherItem(id=6303, clouds= Clouds(all=0), dt=1585915200, main= Main(feelsLike=7.1, humidity=46, pressure=1014, temp=12.84, tempMax=12.84, tempMin=12.84), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=5.69)
        ),
        FutureWeatherItem(id=6304, clouds= Clouds(all=28), dt=1585926000, main= Main(feelsLike=8.06, humidity=47, pressure=1014, temp=12.97, tempMax=12.97, tempMin=12.97), weather=listOf(
            Weather1(main="Clouds")
        ), wind= Wind(speed=4.6)
        ),
        FutureWeatherItem(id=6305, clouds= Clouds(all=46), dt=1585936800, main= Main(feelsLike=4.39, humidity=62, pressure=1016, temp=8.37, tempMax=8.37, tempMin=8.37), weather=listOf(
            Weather1(main="Clouds")
        ), wind= Wind(speed=3.18)
        ),
        FutureWeatherItem(id=6306, clouds= Clouds(all=31), dt=1585947600, main= Main(feelsLike=3.67, humidity=67, pressure=1018, temp=6.49, tempMax=6.49, tempMin=6.49), weather=listOf(
            Weather1(main="Clouds")
        ), wind= Wind(speed=1.37)
        ),
        FutureWeatherItem(id=6307, clouds= Clouds(all=22), dt=1585958400, main= Main(feelsLike=1.85, humidity=79, pressure=1019, temp=4.96, tempMax=4.96, tempMin=4.96), weather=listOf(
            Weather1(main="Clouds")
        ), wind= Wind(speed=1.97)
        ),
        FutureWeatherItem(id=6308, clouds= Clouds(all=73), dt=1585969200, main= Main(feelsLike=0.7, humidity=77, pressure=1021, temp=4.67, tempMax=4.67, tempMin=4.67), weather=listOf(
            Weather1(main="Clouds")
        ), wind= Wind(speed=3.05)
        ),
        FutureWeatherItem(id=6309, clouds= Clouds(all=55), dt=1585980000, main= Main(feelsLike=2.95, humidity=74, pressure=1023, temp=6.48, tempMax=6.48, tempMin=6.48), weather=listOf(
            Weather1(main="Clouds")
        ), wind= Wind(speed=2.7)
        ),
        FutureWeatherItem(id=6310, clouds= Clouds(all=0), dt=1585990800, main= Main(feelsLike=7.7, humidity=52, pressure=1023, temp=11.55, tempMax=11.55, tempMin=11.55), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=3.12)
        ),
        FutureWeatherItem(id=6311, clouds= Clouds(all=2), dt=1586001600, main= Main(feelsLike=9.74, humidity=41, pressure=1023, temp=13.73, tempMax=13.73, tempMin=13.73), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=3.02)
        ),
        FutureWeatherItem(id=6312, clouds= Clouds(all=13), dt=1586012400, main= Main(feelsLike=9.5, humidity=43, pressure=1023, temp=13.6, tempMax=13.6, tempMin=13.6), weather=listOf(
            Weather1(main="Clouds")
        ), wind= Wind(speed=3.29)
        ),
        FutureWeatherItem(id=6313, clouds= Clouds(all=8), dt=1586023200, main= Main(feelsLike=5.16, humidity=58, pressure=1026, temp=9.01, tempMax=9.01, tempMin=9.01), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=2.92)
        ),
        FutureWeatherItem(id=6314, clouds= Clouds(all=0), dt=1586034000, main= Main(feelsLike=2.3, humidity=61, pressure=1029, temp=7.16, tempMax=7.16, tempMin=7.16), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=4.14)
        ),
        FutureWeatherItem(id=6315, clouds= Clouds(all=0), dt=1586044800, main= Main(feelsLike=0.77, humidity=68, pressure=1029, temp=5.36, tempMax=5.36, tempMin=5.36), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=3.71)
        ),
        FutureWeatherItem(id=6316, clouds= Clouds(all=0), dt=1586055600, main= Main(feelsLike=-0.69, humidity=72, pressure=1030, temp=3.88, tempMax=3.88, tempMin=3.88), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=3.55)
        ),
        FutureWeatherItem(id=6317, clouds= Clouds(all=0), dt=1586066400, main= Main(feelsLike=1.17, humidity=66, pressure=1030, temp=6.18, tempMax=6.18, tempMin=6.18), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=4.39)
        ),
        FutureWeatherItem(id=6318, clouds= Clouds(all=0), dt=1586077200, main= Main(feelsLike=7.79, humidity=53, pressure=1030, temp=12.33, tempMax=12.33, tempMin=12.33), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=4.34)
        ),
        FutureWeatherItem(id=6319, clouds= Clouds(all=0), dt=1586088000, main= Main(feelsLike=11.95, humidity=42, pressure=1029, temp=16.26, tempMax=16.26, tempMin=16.26), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=4.09)
        ),
        FutureWeatherItem(id=6320, clouds= Clouds(all=0), dt=1586098800, main= Main(feelsLike=12.06, humidity=43, pressure=1028, temp=16.41, tempMax=16.41, tempMin=16.41), weather=listOf(
            Weather1(main="Clear")
        ), wind= Wind(speed=4.27)
        )
    )

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        db = DatabaseObj.invoke(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun should_Insert_List() {
        db?.futureDatabaseDao()?.insert(testData)
        val testDataFromDB = db?.futureDatabaseDao()?.getFutureWeather()?.let { getValue(it) }!!
        assertEquals(testData.size, testDataFromDB.size)
    }

    @Test
    fun should_Flush_All_Data(){
        db?.futureDatabaseDao()?.deleteOld()
        val testDataFromDB = db?.futureDatabaseDao()?.getFutureWeather()?.let { getValue(it) }!!
        assertEquals(testDataFromDB.size,0)
    }

    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(t: T?) {
                data[0] = t
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        return data[0] as T
    }
}

// StackOverflow