package com.speter97.weatherforecast.data

import com.speter97.weatherforecast.data.response.CurrentWeatherData
import kotlinx.coroutines.Deferred
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
    ):CurrentWeatherData

    companion object {
        operator fun invoke(): WeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url().newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
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