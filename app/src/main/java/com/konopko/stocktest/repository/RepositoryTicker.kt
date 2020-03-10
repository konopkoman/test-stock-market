package com.konopko.stocktest.repository

import com.google.gson.Gson
import com.konopko.stocktest.ApiService
import com.konopko.stocktest.Ticker
import com.konopko.stocktest.model.ModelTickerChart
import com.konopko.stocktest.model.ModelTickerDetails
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class RepositoryTicker constructor(private val apiService: ApiService): IRepositoryTicker {

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val mapTicker = mutableMapOf<String, Ticker?>()
    private val subject: BehaviorSubject<Map<String, Ticker?>> = BehaviorSubject.create()

    private fun addTicker(tickerId: String, ticker: Ticker){
        mapTicker.put(tickerId, ticker)
        subject.onNext(mapTicker.toMap())
        Timber.d("addTicker ticker = %s", Gson().toJson(ticker))
    }

    override fun getTicker(tickerId: String): Ticker?
        = mapTicker.get(tickerId)

    override fun subscribeMapTickers(): Observable<Map<String, Ticker?>>
        = subject

    override fun findTickers(listTickers: List<String>){
        disposable = CompositeDisposable()

        mapTicker.clear()

        // fill map with tickers' ids as keys
        for (tickerId in listTickers)
            mapTicker.put(tickerId, null)

        // populate data
        subject.onNext(mapTicker)

        for (tickerId in listTickers) {
            apiService.fetchTickerDetails(tickerId).subscribeOn(Schedulers.io())
                    .zipWith(apiService.fetchTickerChart(tickerId).subscribeOn(Schedulers.io()),
                            BiFunction<ModelTickerDetails.Result, ModelTickerChart.Result, Ticker>{ tickerDetails, tickerChart ->
                                return@BiFunction Ticker(tickerId, tickerDetails, tickerChart)
                            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onSuccess = { addTicker(tickerId, it) },
                            onError = {
                                val message = it.message ?: it.toString()
                                addTicker(tickerId, Ticker(tickerId, message))
                            }
                    ).addTo(disposable)
        }
    }
}
