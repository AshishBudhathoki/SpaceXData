package com.demo.spacexdata.di

import android.app.Application
import androidx.room.Room
import com.demo.spacexdata.api.SpaceXApi
import com.demo.spacexdata.data.SpaceXDao
import com.demo.spacexdata.data.SpaceXDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = LoggingInterceptor.create()

    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return HttpClient.setupOkHttpClient(httpLoggingInterceptor)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(SpaceXApi.BASE_URL)
            .client(provideOkHttpClient(provideLoggingInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideSpaceXApi(retrofit: Retrofit): SpaceXApi =
        retrofit.create(SpaceXApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): SpaceXDatabase =
        Room.databaseBuilder(app, SpaceXDatabase::class.java, "space_x_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideSpaceXDao(database: SpaceXDatabase): SpaceXDao =
        database.spaceXLaunchDao()
}

object LoggingInterceptor {
    fun create(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

}

object HttpClient {
    fun setupOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }
}