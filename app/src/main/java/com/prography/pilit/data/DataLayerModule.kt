package com.prography.pilit.data

import com.prography.pilit.data.datasource.remote.pill.PillApiService
import com.prography.pilit.data.datasource.remote.user.UserApiService
import com.prography.pilit.data.datasource.remote.user.UserRemoteDataSourceImpl
import com.prography.pilit.data.repository.PillRepositoryImpl
import com.prography.pilit.data.repository.UserRepositoryImpl
import com.prography.pilit.domain.datasource.remote.UserRemoteDataSource
import com.prography.pilit.domain.repository.PillRepository
import com.prography.pilit.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataLayerModule {

    // User
    @Provides
    @Singleton
    fun provideUserRepository(remoteDataSource: UserRemoteDataSource): UserRepository = UserRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesUserRemoteDataSource(userApiService: UserApiService): UserRemoteDataSource =
        UserRemoteDataSourceImpl(userApiService)

    @Provides
    @Singleton
    fun providesPillRepository(pillApiService: PillApiService): PillRepository =
        PillRepositoryImpl(pillApiService)

}