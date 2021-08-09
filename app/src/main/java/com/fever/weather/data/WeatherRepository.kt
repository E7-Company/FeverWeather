package com.fever.weather.data

import arrow.core.Either
import com.fever.weather.data.local.WeatherDao
import com.fever.weather.data.remote.NetworkDataSource
import com.fever.weather.data.local.entity.Weather
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: WeatherDao
) {

    suspend fun getCurrentWeather(params: WeatherParams): Either<Throwable, Weather?> =
        Either.catch {
            if (params.forceRefresh) {
                getNetworkCurrentWeather(params)
            } else {
                localDataSource.getWeather() ?: getNetworkCurrentWeather(params)
            }
    }

    private suspend fun getNetworkCurrentWeather(params: WeatherParams): Weather? =
        networkDataSource.getCurrentWeather(params.lat, params.lon, params.units).fold(
            { localDataSource.getWeather() },
            {
                setCurrentWeather(it)
                it
            }
        )

    private suspend fun setCurrentWeather(weather: Weather) =
        localDataSource.deleteAndInsert(weather)

    data class WeatherParams(
        val lat: Double,
        val lon: Double,
        val forceRefresh: Boolean,
        val units: String
    )
}