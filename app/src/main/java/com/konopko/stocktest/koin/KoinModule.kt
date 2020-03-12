package com.konopko.stocktest.koin

import com.konopko.stocktest.app.App
import com.konopko.stocktest.app.AppHelper
import com.konopko.stocktest.ext.chart.ChartHelper
import com.konopko.stocktest.ext.chart.MPAndroidChartHelper
import com.konopko.stocktest.network.ApiClient
import com.konopko.stocktest.network.ApiService
import com.konopko.stocktest.repository.IRepositoryTicker
import com.konopko.stocktest.repository.RepositoryTicker
import org.koin.dsl.module

val myModule = module {
    factory { ApiClient.getClient(get()).create(ApiService::class.java) }
    single<IRepositoryTicker> { RepositoryTicker(get()) }
    single<AppHelper> { App.instance }
    single<ChartHelper> { MPAndroidChartHelper.Companion }
}