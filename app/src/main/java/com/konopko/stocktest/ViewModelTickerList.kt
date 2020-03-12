package com.konopko.stocktest

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.konopko.stocktest.ext.mvvm.LiveEvent
import com.konopko.stocktest.repository.IRepositoryTicker
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class ViewModelTickerList : ViewModel(), KoinComponent {

    private val repositoryTicker : IRepositoryTicker by inject()
    private val context: Context by inject()

    private val disposable = CompositeDisposable()
    private var dataListTickerHolder = MutableLiveData<List<AdapterTickerListHolder>>()
    val dataTickerId = LiveEvent<String>()
    val dataNotification = LiveEvent<String>()


    override fun onCleared() {
        disposable.dispose()
    }

    fun getDataListTickerHolder(): LiveData<List<AdapterTickerListHolder>> {
        loadTickerList(repositoryTicker)
        return dataListTickerHolder
    }

    fun openTickerDetails(ticker: Ticker?) {
        if (ticker != null && ticker.error == null)
            dataTickerId.setValue(ticker.id)
        else
            dataNotification.setValue(context.getString(R.string.ticker_error))
    }

    private fun loadTickerList(repositoryTicker: IRepositoryTicker) {
        disposable.add(
                repositoryTicker.subscribeMapTickers().subscribeBy(
                        onNext = { dataListTickerHolder.setValue(getTickerListHolder(it)) }
        ))
    }

    private fun getTickerListHolder(map: Map<String, Ticker?>): List<AdapterTickerListHolder> {
        val result: MutableList<AdapterTickerListHolder> = ArrayList()
        for ((key, value) in map)
            result.add(AdapterTickerListHolder(key, value))
        return result
    }
}