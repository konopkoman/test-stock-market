package com.konopko.stocktest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.konopko.stocktest.repository.IRepositoryTicker
import org.koin.core.KoinComponent
import org.koin.core.inject

class ViewModelTickerDetails constructor() : ViewModel(), KoinComponent {

    private val repositoryTicker : IRepositoryTicker by inject()

    val dataTicker = MutableLiveData<Ticker>()

    constructor(tickerId: String): this(){
        dataTicker.value = repositoryTicker.getTicker(tickerId)
    }

}