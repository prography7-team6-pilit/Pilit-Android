package com.prography.pilit.data.datasource.remote

import com.prography.pilit.AuthInterceptorClient
import com.prography.pilit.OtherInterceptorClient
import com.prography.pilit.data.datasource.remote.pill.PillApiService
import com.prography.pilit.data.datasource.remote.user.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideUserApiService(
        @OtherInterceptorClient retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun providesPillApiService(
        @AuthInterceptorClient retrofit: Retrofit): PillApiService =
        retrofit.create(PillApiService::class.java)
}