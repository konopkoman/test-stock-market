package com.konopko.stocktest

import com.google.gson.Gson
import com.konopko.stocktest.repository.IRepositoryTicker
import io.reactivex.disposables.CompositeDisposable
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class RepositoryTickerTestFull: KoinComponent {

    private val repositoryTicker : IRepositoryTicker by inject()

    private val disposable: CompositeDisposable = CompositeDisposable()
    private val listTicker: List<String> = listOf("TE", "FAKEID12")

    // here +1 to list size because the first call of repository.subscribeMapTickers() is initial with no tickers
    private val lock: CountDownLatch = CountDownLatch(listTicker.size +1)

    private var result: Map<String, Ticker?> = emptyMap()

    @Before
    fun init(){
        Timber.d("init")
    }

    @After
    fun finish(){
        Timber.d("finish")
        disposable.dispose()
    }

    @Test
    fun loadTickers(){

        disposable.add(
                repositoryTicker.subscribeMapTickers().subscribe{
                    for (ticker in it.keys)
                        Timber.d("ticker id: %s, body: %s", ticker, Gson().toJson(it.get(ticker)))
                    result = it.toMap()
                    lock.countDown()
                })

        repositoryTicker.findTickers(listTicker)

        lock.await(20, TimeUnit.SECONDS) // wait for api calls

        var ticker: Ticker?

        ticker = result.get(listTicker[0])
        if (ticker?.error == null)
            testTickerValid(ticker)
        else
            testTickerError(ticker)

        ticker = result.get(listTicker[1])
        testTickerError(ticker)
    }

    fun testTickerValid(ticker: Ticker?){
        assertNotNull(ticker)
        Timber.d("currentValue = %f", ticker?.getCurrentValue())
        assertNotNull(ticker?.getCurrentValue())
        assertNotNull(ticker?.getCompanyDesc())
        assertFalse(ticker?.getChartData()?.isEmpty() ?: true)
    }

    fun testTickerError(ticker: Ticker?){
        assertNotNull(ticker)
        Timber.d("error = %s", ticker?.error)
        assertNotNull(ticker?.error)
        assertNull(ticker?.getCurrentValue())
        assertTrue(ticker?.getChartData()?.isEmpty() ?: false)
    }
}