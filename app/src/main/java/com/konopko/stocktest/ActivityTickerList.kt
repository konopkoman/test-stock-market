package com.konopko.stocktest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.konopko.stocktest.databinding.ActivityTickerListBinding
import io.reactivex.disposables.CompositeDisposable

class ActivityTickerList : ActivityBase<ActivityTickerListBinding>() {

    private lateinit var viewModel: ViewModelTickerList
    private val adapter = AdapterTickerList()
    private val disposable = CompositeDisposable()

    companion object {
        fun open(context: Context){
            val intent = Intent(context, ActivityTickerList::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId()
            = R.layout.activity_ticker_list

    override fun getPageTitle()
            = getString(R.string.ticker_list_title)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.listTicker.adapter = adapter

        viewModel = ViewModelProvider(this).get(ViewModelTickerList::class.java)

        viewModel.getDataListTickerHolder().observe(this, Observer { list: List<AdapterTickerListHolder> ->
            adapter.setList(list)
        })

        viewModel.dataTickerId.observe(this, Observer { tickerId: String ->
            ActivityTickerDetails.open(this, tickerId)
        })

        viewModel.dataNotification.observe(this, Observer { notification: String ->
            Toast.makeText(this, notification, Toast.LENGTH_SHORT).show()
        })

        disposable.add(
                adapter.subscribeItemClick().subscribe { tickerListHolder: AdapterTickerListHolder ->
                    viewModel.openTickerDetails(tickerListHolder.ticker)
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}