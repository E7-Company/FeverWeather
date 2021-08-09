package com.fever.weather.ui.weather

import com.fever.weather.data.local.entity.Weather

sealed class WeatherViewState {
    object Loading : WeatherViewState()
    data class Loaded(
        val weather: Weather?
    ) : WeatherViewState()
}
