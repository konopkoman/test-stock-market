package com.konopko.stocktest.dagger

import com.konopko.stocktest.ApiClient
import com.konopko.stocktest.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkService(networkClient: Retrofit): ApiService {
        return networkClient.create(ApiService::class.java)
    }

    @Provides
    fun provideRetrofit(): Retrofit {
        return ApiClient.getClient()
    }
}