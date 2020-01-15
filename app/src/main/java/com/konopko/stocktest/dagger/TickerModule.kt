package com.konopko.stocktest.dagger

import com.konopko.stocktest.ApiService
import com.konopko.stocktest.RepositoryTicker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TickerModule {

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService): RepositoryTicker {
        return RepositoryTicker(apiService)
    }
}