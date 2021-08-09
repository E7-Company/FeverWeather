package com.fever.weather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.fever.weather.data.local.entity.Weather

@Dao
interface WeatherDao {

    @Query("SELECT * from weather")
    suspend fun getWeather(): Weather?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeatherEntity: Weather)

    @Transaction
    suspend fun deleteAndInsert(currentWeather: Weather) {
        deleteCurrentWeather()
        insertCurrentWeather(currentWeather)
    }

    @Query("DELETE FROM weather")
    suspend fun deleteCurrentWeather()

    @Query("Select count(*) from weather")
    suspend fun getCount(): Int

}