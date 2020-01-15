package com.konopko.stocktest.dagger

import com.konopko.stocktest.ViewModelAskTickers
import com.konopko.stocktest.ViewModelTickerDetails
import com.konopko.stocktest.ViewModelTickerList
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, TickerModule::class])
interface AppComponent {

    fun inject(viewModel: ViewModelAskTickers)
    fun inject(viewModel: ViewModelTickerList)
    fun inject(viewModel: ViewModelTickerDetails)
}