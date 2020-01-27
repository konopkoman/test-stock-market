package com.konopko.stocktest.dagger

import com.konopko.stocktest.ApiService
import com.konopko.stocktest.repository.IRepositoryTicker
import com.konopko.stocktest.repository.RepositoryTicker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TickerModule {

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService): IRepositoryTicker {
        return RepositoryTicker(apiService)
    }
}