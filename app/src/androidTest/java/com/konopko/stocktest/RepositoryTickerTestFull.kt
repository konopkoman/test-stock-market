package com.konopko.stocktest

import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import timber.log.Timber
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class RepositoryTickerTestFull {

    private val apiService: ApiService = ApiClient.getClient().create(ApiService::class.java)
    private val repositoryTicker: RepositoryTicker = RepositoryTicker(apiService)

    private val disposable: CompositeDisposable = CompositeDisposable()
    private val listTicker: List<String> = listOf("TE", "FAKEID12")

    // here +1 to list size because the first call of repository.subscribeMapTickers() is initial with no tickers
    private val lock: CountDownLatch = CountDownLatch(listTicker.size +1)

    private var result: Map<String, Ticker> = emptyMap()

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
        Timber.d("currentValue = %f", ticker?.currentValue)
        assertNotNull(ticker?.currentValue)
        assertNotNull(ticker?.companyDesc)
        assertFalse(ticker?.chartData?.isEmpty() ?: true)
    }

    fun testTickerError(ticker: Ticker?){
        assertNotNull(ticker)
        Timber.d("error = %s", ticker?.error)
        assertNotNull(ticker?.error)
        assertNull(ticker?.currentValue)
        assertTrue(ticker?.chartData?.isEmpty() ?: false)
    }
}