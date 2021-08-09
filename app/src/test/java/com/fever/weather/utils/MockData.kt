package com.fever.weather.utils

import com.fever.weather.data.local.entity.Weather

val mockWeather = Weather(
    id = 1,
    city = "Valencia",
    country = "ES",
    latitude = 39.46,
    longitude = -0.37,
    temp = 28,
    tempMin = 25,
    tempMax = 30,
    pressure = 1017.0,
    humidity = 67,
    wind = 2.68,
    main = "Clouds",
    description = "broken clouds",
    icon = "04d",
    timezone = 7200
)

val mockWeather1 = Weather(
    id = 1,
    city = "Alicante",
    country = "ES",
    latitude = 45.98,
    longitude = -1.55,
    temp = 30,
    tempMin = 28,
    tempMax = 33,
    pressure = 1030.0,
    humidity = 45,
    wind = 1.55,
    main = "Clear",
    description = "clear sky",
    icon = "02n",
    timezone = 7200
)