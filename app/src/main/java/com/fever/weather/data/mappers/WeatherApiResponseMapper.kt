package com.fever.weather.data.mappers

import com.fever.weather.model.CurrentWeather
import com.fever.weather.data.local.entity.Weather
import kotlin.math.roundToInt

interface WeatherApiResponseMapper {
    fun CurrentWeather.toWeather(): Weather = Weather(
        id = this.id,
        city = this.name,
        country = this.sys?.country,
        latitude = this.coord?.lat,
        longitude = this.coord?.lon,
        temp = this.main?.temp?.roundToInt(),
        tempMin = this.main?.tempMin?.roundToInt(),
        tempMax = this.main?.tempMax?.roundToInt(),
        pressure = this.main?.pressure,
        humidity = this.main?.humidity,
        wind = this.wind?.speed,
        main = this.weather?.get(0)?.main,
        description = this.weather?.get(0)?.description,
        icon = this.weather?.get(0)?.icon,
        timezone = this.timezone
    )
}