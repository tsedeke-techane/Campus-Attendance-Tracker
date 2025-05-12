package com.attendance.attendancetracker.di

import com.attendance.attendancetracker.data.remote.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import android.os.Build

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Base URLs for different device types
    private const val EMULATOR_BASE_URL = "http://10.0.2.2:1000/"
    private const val PHYSICAL_DEVICE_BASE_URL = "http://10.5.90.220:1000/"

    // Determine if running on emulator (this is a simplified check)
    private fun isEmulator(): Boolean {
        return Build.PRODUCT.contains("sdk") ||
               Build.MODEL.contains("Emulator") ||
               Build.MODEL.contains("Android SDK")
    }

    // Get the appropriate base URL based on device type
    private fun getBaseUrl(): String {
        return if (isEmulator()) EMULATOR_BASE_URL else PHYSICAL_DEVICE_BASE_URL
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl()) // Use the appropriate BASE_URL based on device type
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
} 