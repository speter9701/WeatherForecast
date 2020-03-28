package com.speter97.weatherforecast.data.network

import com.speter97.weatherforecast.data.network.response.todayEntity.CurrentWeatherData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "6054790cbe92dc1b1ddbd86613440ca6"
// api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}
// api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={your api key}

interface WeatherApiService {

    @GET("weather")
    suspend fun getCurrentWeatherData(
        @Query("q") location: String
    ): CurrentWeatherData

    companion object {
        operator fun invoke(
            ConnectivityInterceptor: ConnectivityInterceptor
        ): WeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url().newBuilder()
                    .addQueryParameter("appid",
                        API_KEY
                    )
                    .addQueryParameter("units","metric")
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(ConnectivityInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}


// Stackoverflow: https://stackoverflow.com/questions/48484994/how-to-handle-retrofit-response-kotlin-in-android/50491963