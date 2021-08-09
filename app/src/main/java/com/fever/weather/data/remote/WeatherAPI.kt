package com.fever.weather.data.remote

import com.fever.weather.model.CurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String
    ): CurrentWeather

}