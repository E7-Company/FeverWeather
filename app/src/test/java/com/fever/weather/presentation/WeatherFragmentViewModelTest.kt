package com.fever.weather.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import com.fever.weather.data.WeatherRepository
import com.fever.weather.utils.mockWeather
import com.fever.weather.ui.weather.WeatherFragmentViewModel
import com.fever.weather.ui.weather.WeatherViewState
import com.fever.weather.utils.Constants
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertTrue
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
class WeatherFragmentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    private val repository = mockk< WeatherRepository>()

    @Before
    fun setup() {
        Dispatchers.setMain((testDispatcher))
    }

    @After
    fun clean() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `change state to Loaded when get weather correctly`() {
        coEvery { repository.getCurrentWeather(any()) } returns
                Either.Right(mockWeather)

        testCoroutineScope.runBlockingTest {
            val viewModel = buildViewModel()
            viewModel.getWeather(Constants.Units.Unit.IMPERIAL.value, true, null, null)

            Assert.assertEquals(mockWeather, (viewModel.viewState.value as WeatherViewState.Loaded).weather)
        }
    }

    @Test
    fun `show error when get weather return an exception`() {
        coEvery { repository.getCurrentWeather(any()) } returns
                Either.Left(Exception())

        testCoroutineScope.runBlockingTest {
            val viewModel = buildViewModel()
            viewModel.getWeather(Constants.Units.Unit.IMPERIAL.value, true, null, null)

            val viewEvent = viewModel.viewEvents.poll()
            assertTrue(viewEvent is WeatherFragmentViewModel.WeatherViewEvent.OnShowError)

            Assert.assertEquals(null, (viewModel.viewState.value as WeatherViewState.Loaded).weather)
        }
    }

    private fun buildViewModel() = WeatherFragmentViewModel(
        repository = repository
    ).apply {
        init()
    }
}