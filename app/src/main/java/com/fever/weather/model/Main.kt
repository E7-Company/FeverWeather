package com.fever.weather.model

import com.google.gson.annotations.SerializedName

data class Main(
        val temp: Double?,
        @SerializedName("feels_like") val feelsLike: Double?,
        @SerializedName("temp_min") var tempMin: Double?,
        @SerializedName("temp_max") var tempMax: Double?,
        val pressure: Double?,
        val humidity: Int?,
        @SerializedName("sea_level") val seaLevel: Double?,
        @SerializedName("grnd_level") val grndLevel: Double?
)
