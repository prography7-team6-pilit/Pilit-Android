package com.prography.pilit.data

import com.prography.pilit.data.datasource.remote.user.UserRemoteDataSourceImpl
import com.prography.pilit.data.repository.PillRepositoryImpl
import com.prography.pilit.data.repository.UserRepositoryImpl
import com.prography.pilit.domain.datasource.remote.UserRemoteDataSource
import com.prography.pilit.domain.repository.PillRepository
import com.prography.pilit.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataLayerModule {

    // User
    @Binds
    @Singleton
    abstract fun bindsUserRepository(repositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindsUserRemoteDataSource(dataSourceImpl: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsPillRepository(pillRepositoryImpl: PillRepositoryImpl): PillRepository

}