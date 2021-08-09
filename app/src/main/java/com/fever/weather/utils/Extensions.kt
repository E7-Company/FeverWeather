package com.fever.weather.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.fever.weather.R
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this, duration).apply { show() }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toDay(): String {
    val dateFormat = DateTimeFormatter.ofPattern("EEEE, MMM dd", Locale.US)
    val offset = ZoneOffset.ofTotalSeconds(this.toInt())
    val odt = OffsetDateTime.now(offset)
    return odt.toZonedDateTime().format(dateFormat)
}

fun String.toWeatherIcon(context: Context): String {
    return when(this) {
        "11d" -> context.getString(R.string.weather_thunderstorm)
        "10d","10n" -> context.getString(R.string.weather_showers)
        "09d" -> context.getString(R.string.weather_rain)
        "13d" -> context.getString(R.string.weather_snow)
        "50d" -> context.getString(R.string.weather_fog)
        "01d" -> context.getString(R.string.weather_day_sunny)
        "01n" -> context.getString(R.string.weather_night_clear)
        "02d" -> context.getString(R.string.weather_day_cloudy)
        "02n" -> context.getString(R.string.weather_night_partly_cloudy)
        "03d" -> context.getString(R.string.weather_day_high_cloudy)
        "03n" -> context.getString(R.string.weather_night_high_cloudy)
        "04d","04n" -> context.getString(R.string.weather_cloudy)
        else -> context.getString(R.string.not_available)
    }
}

fun SharedPreferences.putUnit(unit: Constants.Units.Unit) {
    edit().putString(Constants.Units.UNITS, unit.value).apply()
}

fun SharedPreferences.getUnits(): String {
    return getString(Constants.Units.UNITS, Constants.Units.Unit.STANDARD.value) ?:
        Constants.Units.Unit.STANDARD.value
}