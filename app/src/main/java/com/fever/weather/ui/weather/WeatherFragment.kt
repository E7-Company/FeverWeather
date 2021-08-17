package com.fever.weather.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.fever.weather.R
import com.fever.weather.databinding.FragmentWeatherBinding
import com.fever.weather.data.local.entity.Weather
import com.fever.weather.ui.base.BaseViewModelFragment
import com.fever.weather.utils.Constants
import com.fever.weather.utils.toWeatherIcon
import com.fever.weather.utils.toast
import com.fever.weather.utils.toDay
import com.fever.weather.utils.getUnits
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class WeatherFragment : BaseViewModelFragment<
        FragmentWeatherBinding,
        WeatherViewState,
        WeatherFragmentViewModel.WeatherViewEvent,
        WeatherFragmentViewModel>() {

    override val viewBinding: (LayoutInflater, ViewGroup?) -> FragmentWeatherBinding = { layoutInflater, viewGroup ->
            FragmentWeatherBinding.inflate(layoutInflater, viewGroup, false)
    }

    override val viewModel: WeatherFragmentViewModel by viewModels()

    override fun setupUI() {
        val font = Typeface.createFromAsset(activity?.assets, Constants.Font.ICON_FONT)
        binding.tvIconWeather.typeface = font
        binding.viewData.tvIconHumidity.typeface = font
        binding.viewData.tvIconPressure.typeface = font
        binding.viewData.tvIconWind.typeface = font

        binding.buttonRefresh.setOnClickListener {
            viewModel.getWeather(getUnits(), true)
        }

        viewModel.getWeather(getUnits(), false)
    }

    override fun renderViewState(viewState: WeatherViewState) {
        when (viewState) {
            is WeatherViewState.Loading -> {
                showLoading(true)
            }
            is WeatherViewState.Loaded -> {
                showLoading(false)
                viewState.weather?.let { showWeather(it) }
            }
        }
    }

    override fun handleViewEvent(viewEvent: WeatherFragmentViewModel.WeatherViewEvent) {
        when (viewEvent) {
            is WeatherFragmentViewModel.WeatherViewEvent.OnShowError ->
                getString(R.string.weather_error).toast(requireContext())
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
            binding.weatherView.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.weatherView.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showWeather(weather: Weather) {
        binding.let {
            it.tvCountry.text = if (weather.city.isNullOrEmpty())
                "Lat: ${weather.latitude}, Lon: ${weather.longitude}"
            else
                "${weather.city} (${weather.country})"

            it.tvIconWeather.text = weather.icon?.toWeatherIcon(requireContext())
            it.viewTemp.tvTemp.text = "${weather.temp}\u00B0"
            it.viewTemp.tvTempMin.text = "/${weather.tempMin}\u00B0"
            it.tvClimate.text = weather.main?.uppercase()
            it.tvDescription.text = weather.description?.replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase(
                    Locale.getDefault()
                ) else char.toString()
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                it.tvDay.text = weather.timezone.toString().toDay()
            }

            it.viewData.tvHumidity.text = "${weather.humidity} %"
            it.viewData.tvPressure.text = "${weather.pressure} hPa"

            it.viewData.tvWind.text = if (getUnits() == Constants.Units.Unit.IMPERIAL.value)
                "${weather.wind} mph"
            else
                "${weather.wind} mps"
        }
    }

    private fun getUnits(): String {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        return prefs?.getUnits() ?: Constants.Units.Unit.STANDARD.value
    }

}