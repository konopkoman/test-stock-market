package com.konopko.stocktest

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.konopko.stocktest.ext.LiveEvent
import com.konopko.stocktest.repository.IRepositoryTicker
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class ViewModelAskTickers constructor() : ViewModel(), KoinComponent {

    private val repositoryTicker : IRepositoryTicker by inject()

    private lateinit var state : SavedStateHandle //todo use this to save the view state

    val dataNext = LiveEvent<Boolean>()

    constructor(savedStateHandle: SavedStateHandle): this(){
        this.state = savedStateHandle
    }

    fun next(tickers : List<String>){
        Timber.d("clickNext tags = %s", Gson().toJson(tickers))
        if (!tickers.isEmpty()) {
            searchTickers(tickers)
            dataNext.setValue(true)
        }
    }

    private fun searchTickers(tickers: List<String>) {
        repositoryTicker.findTickers(tickers)
    }

}