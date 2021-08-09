package com.fever.weather.utils

object Constants {

    object Network {
        const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        const val API_KEY = "0bc9bc2a73fd9644f664cf5f5c5be8d7"
    }

    object Database {
        const val NAME = "fever"
    }

    object Font {
        const val ICON_FONT = "fonts/weathericons.ttf"
    }

    object Units {
        const val UNITS = "UNITS"

        enum class Unit (val value: String) {
            STANDARD("standard"),
            METRIC("metric"),
            IMPERIAL("imperial")
        }
    }

}