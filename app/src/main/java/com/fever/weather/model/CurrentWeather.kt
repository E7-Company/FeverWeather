package com.fever.weather.model

data class CurrentWeather(
    val id: Int? = null,
    val base: String? = null,
    val main: Main? = null,
    val visibility: Int? = null,
    val dt: Int? = null,
    val sys: Sys? = null,
    val timezone: Int? = null,
    val name: String? = null,
    val cod: Int? = null,
    val clouds: Clouds? = null,
    val coord: Coord? = null,
    val weather: List<WeatherItem?>? = null,
    val wind: Wind? = null
)
