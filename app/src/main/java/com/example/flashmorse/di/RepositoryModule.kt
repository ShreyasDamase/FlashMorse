package com.example.flashmorse.di

import com.example.flashmorse.data.flashlight.FlashlightRepositoryImpl
import com.example.flashmorse.domain.repository.FlashlightRepository
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