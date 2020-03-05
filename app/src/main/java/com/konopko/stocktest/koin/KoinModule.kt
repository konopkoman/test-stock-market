package com.konopko.stocktest.koin

import com.konopko.stocktest.ApiClient
import com.konopko.stocktest.ApiService
import com.konopko.stocktest.app.AppHelper
import com.konopko.stocktest.app.App
import com.konopko.stocktest.repository.IRepositoryTicker
import com.konopko.stocktest.repository.RepositoryTicker
import org.koin.dsl.module

val myModule = module {
    factory { ApiClient.getClient(get()).create(ApiService::class.java) }
    single<IRepositoryTicker> { RepositoryTicker(get()) }
    single<AppHelper> { App.instance }
}