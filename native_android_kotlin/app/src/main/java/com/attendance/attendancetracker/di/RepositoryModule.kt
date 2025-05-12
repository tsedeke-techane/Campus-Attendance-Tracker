package com.attendance.attendancetracker.di

import com.attendance.attendancetracker.data.remote.api.AuthApi
import com.attendance.attendancetracker.data.repository.AuthRepository
import com.attendance.attendancetracker.data.repository.AuthRepositoryImpl
import com.attendance.attendancetracker.data.storage.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton // Assuming repository should be a singleton
    fun provideAuthRepository(
        api: AuthApi,
        dataStore: DataStoreManager
    ): AuthRepository = AuthRepositoryImpl(api, dataStore)
} 