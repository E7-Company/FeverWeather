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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCache(): Cache =
        Cache(Environment.getDownloadCacheDirectory(), (10 * 1024 * 1024))

    @Singleton
    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .addInterceptor(DefaultRequestInterceptor())
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)

    @Singleton
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

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherAPI = retrofit.create(WeatherAPI::class.java)

    @Singleton
    @Provides
    fun provideWeatherRemoteDataSource(weatherService: WeatherAPI) = NetworkDataSource(weatherService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideWeatherDao(db: AppDatabase) = db.weatherDao()

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: NetworkDataSource,
                          localDataSource: WeatherDao) =
        WeatherRepository(remoteDataSource, localDataSource)

}