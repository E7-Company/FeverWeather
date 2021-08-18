package com.fever.weather.di

import android.content.Context
import android.os.Environment
import com.fever.weather.data.WeatherRepository
import com.fever.weather.data.local.AppDatabase
import com.fever.weather.data.local.WeatherDao
import com.fever.weather.data.remote.NetworkDataSource
import com.fever.weather.data.remote.WeatherAPI
import com.fever.weather.utils.Constants
import com.fever.weather.utils.DefaultRequestInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideCache(): Cache =
        Cache(Environment.getDownloadCacheDirectory(), (10 * 1024 * 1024))

    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .addInterceptor(DefaultRequestInterceptor())
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)

    @Provides
    fun provideRetrofit(
        okHttpClientBuilder: OkHttpClient.Builder,
        cache: Cache,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.Network.BASE_URL)
        .client(okHttpClientBuilder.cache(cache).build())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherAPI = retrofit.create(WeatherAPI::class.java)

    @Provides
    fun provideWeatherRemoteDataSource(weatherService: WeatherAPI) = NetworkDataSource(weatherService)

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Provides
    fun provideWeatherDao(db: AppDatabase) = db.weatherDao()

    @Provides
    fun provideRepository(remoteDataSource: NetworkDataSource,
                          localDataSource: WeatherDao) =
        WeatherRepository(remoteDataSource, localDataSource)

}