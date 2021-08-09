package com.fever.weather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val city: String?,
    val country: String?,
    val latitude: Double?,
    val longitude: Double?,
    val temp: Int?,
    val tempMin: Int?,
    val tempMax: Int?,
    val pressure: Double?,
    val humidity: Int?,
    val wind: Double?,
    val main: String?,
    val description: String?,
    val icon: String?,
    val timezone: Int?
)
