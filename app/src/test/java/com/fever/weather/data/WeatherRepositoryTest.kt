package com.fever.weather.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import com.fever.weather.data.local.WeatherDao
import com.fever.weather.data.remote.NetworkDataSource
import com.fever.weather.utils.Constants
import com.fever.weather.utils.mockWeather
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert
import java.lang.Exception

@ExperimentalCoroutinesApi
class WeatherRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    private val networkDataSource = mockk<NetworkDataSource>()
    private val weatherDao = mockk<WeatherDao>(relaxed = true)

    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setup() {
        Dispatchers.setMain((testDispatcher))
        weatherRepository = WeatherRepository(
            networkDataSource,
            weatherDao
        )
    }

    @After
    fun clean() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `get current weather from database`() {
        coEvery { weatherDao.getWeather() } returns mockWeather

        testCoroutineScope.runBlockingTest {
            val weather = weatherRepository.getCurrentWeather(buildParams(false))
            Assert.assertEquals(mockWeather, weather.orNull())
        }
    }

    @Test
    fun `get current weather from network`() {
        coEvery { networkDataSource.getCurrentWeather(any(), any(), any()) } returns
                Either.Right(mockWeather)

        testCoroutineScope.runBlockingTest {
            val weather = weatherRepository.getCurrentWeather(buildParams(true))
            Assert.assertEquals(mockWeather, weather.orNull())
        }
    }

    @Test
    fun `error to get current weather from database`() {
        coEvery { weatherDao.getWeather() } returns null

        testCoroutineScope.runBlockingTest {
            val weather = weatherRepository.getCurrentWeather(buildParams(false))
            Assert.assertTrue(weather.isLeft())
        }
    }

    @Test
    fun `error to get current weather from network`() {
        coEvery { networkDataSource.getCurrentWeather(any(), any(), any()) } returns
                Either.Left(Exception())

        testCoroutineScope.runBlockingTest {
            val weather = weatherRepository.getCurrentWeather(buildParams(true))
            Assert.assertTrue(weather.isLeft())
        }
    }

    @Test
    fun `error to get current weather from database and get it from network`() {
        coEvery { weatherDao.getWeather() } returns null
        coEvery { networkDataSource.getCurrentWeather(any(), any(), any()) } returns
                Either.Right(mockWeather)

        testCoroutineScope.runBlockingTest {
            val weather = weatherRepository.getCurrentWeather(buildParams(false))
            Assert.assertEquals(mockWeather, weather.orNull())
        }
    }

    private fun buildParams(forceRefresh: Boolean) = WeatherRepository.WeatherParams(
        lat = 35.0,
        lon = 138.0,
        forceRefresh = forceRefresh,
        units = Constants.Units.Unit.IMPERIAL.value
    )

}