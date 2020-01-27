package com.konopko.stocktest.repository

import com.konopko.stocktest.Ticker
import io.reactivex.Observable

interface IRepositoryTicker {

    fun getTicker(tickerId: String): Ticker?

    fun subscribeMapTickers(): Observable<Map<String, Ticker?>>

    fun findTickers(listTickers: List<String>)
}