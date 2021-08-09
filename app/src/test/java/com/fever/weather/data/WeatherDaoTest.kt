package com.fever.weather.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fever.weather.utils.mockWeather
import com.fever.weather.utils.mockWeather1
import com.fever.weather.data.local.AppDatabase
import com.fever.weather.data.local.WeatherDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.concurrent.Executors

@Config(manifest=Config.NONE)
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class WeatherDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var weatherDao: WeatherDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()

        weatherDao = appDatabase.weatherDao()
    }

    @After
    fun closeDatabase() {
        appDatabase.close()
    }

    @Test
    fun `empty database count must be zero`() {
        runBlocking {
            val count = weatherDao.getCount()
            Assert.assertEquals(0, count)
        }
    }

    @Test
    fun `insert one entity and count must be one`() {
        runBlocking {
            weatherDao.insertCurrentWeather(mockWeather)
            val count = weatherDao.getCount()
            Assert.assertEquals(1, count)
        }
    }

    @Test
    fun `insert one entity and test if it is saved`() {
        runBlocking {
            weatherDao.insertCurrentWeather(mockWeather)
            val entity = weatherDao.getWeather()
            Assert.assertEquals(mockWeather.city, entity?.city)
        }
    }

    @Test
    fun `delete and insert a weather`() {
        runBlocking {
            weatherDao.deleteAndInsert(mockWeather)
            var count = weatherDao.getCount()
            Assert.assertEquals(1, count)

            weatherDao.deleteAndInsert(mockWeather1)
            count = weatherDao.getCount()
            val entity = weatherDao.getWeather()
            Assert.assertEquals(1, count)
            Assert.assertEquals(mockWeather1.city, entity?.city)
        }
    }

    @Test
    fun `first insert a weather then delete and count must be zero`() {
        runBlocking {
            weatherDao.deleteAndInsert(mockWeather)
            var count = weatherDao.getCount()
            Assert.assertEquals(1, count)

            weatherDao.deleteCurrentWeather()
            count = weatherDao.getCount()
            Assert.assertEquals(0, count)
        }
    }
}