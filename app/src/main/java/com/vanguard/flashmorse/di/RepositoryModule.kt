package com.vanguard.flashmorse.di

import com.vanguard.flashmorse.data.flashlight.FlashlightRepositoryImpl
import com.vanguard.flashmorse.domain.repository.FlashlightRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFlashlightRepository(
        flashlightRepositoryImpl: FlashlightRepositoryImpl
    ): FlashlightRepository
}