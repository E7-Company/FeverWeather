package com.fever.weather.data.remote

import arrow.core.Either
import com.fever.weather.data.mappers.WeatherApiResponseMapper
import com.fever.weather.data.local.entity.Weather
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val weatherAPI: WeatherAPI
): WeatherApiResponseMapper {
    suspend fun getCurrentWeather(lat: Double, lon: Double, units: String): Either<Throwable, Weather> =
        Either.catch {
            weatherAPI.getCurrentWeather(lat, lon, units).toWeather()
        }
}