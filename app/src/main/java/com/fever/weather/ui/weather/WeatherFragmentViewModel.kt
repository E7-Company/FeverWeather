package com.fever.weather.ui.weather

import androidx.lifecycle.viewModelScope
import arrow.core.handleError
import com.fever.weather.data.WeatherRepository
import com.fever.weather.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherFragmentViewModel @Inject constructor(
    private val repository: WeatherRepository
) : BaseViewModel<WeatherViewState, WeatherFragmentViewModel.WeatherViewEvent>() {

    override suspend fun getInitialViewState(): WeatherViewState = WeatherViewState.Loading

    fun getWeather(units: String, forceRefresh: Boolean) {
        updateViewState { WeatherViewState.Loading }

        viewModelScope.launch {
            repository.getCurrentWeather(createParams(units, forceRefresh))
                .handleError {
                    sendViewEvent(WeatherViewEvent.OnShowError)
                    null
                }
                .map { weather ->
                    updateViewState {
                        WeatherViewState.Loaded(
                            weather = weather
                        )
                    }
                }
        }
    }

    private fun createParams(units: String, forceRefresh: Boolean): WeatherRepository.WeatherParams {
        return WeatherRepository.WeatherParams(
            lat = getRandomLatitude(),
            lon = getRandomLongitude(),
            forceRefresh = forceRefresh,
            units = units
        )
    }

    private fun getRandomLatitude(): Double {
        return Math.random() * 180.0 - 90.0
    }

    private fun getRandomLongitude(): Double {
        return Math.random() * 360.0 - 180.0
    }

    sealed class WeatherViewEvent {
        object OnShowError: WeatherViewEvent()
    }

}