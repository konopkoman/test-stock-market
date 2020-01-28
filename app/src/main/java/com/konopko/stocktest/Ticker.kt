package com.konopko.stocktest

import com.google.gson.Gson
import com.konopko.stocktest.model.ModelTickerChart
import com.konopko.stocktest.model.ModelTickerDetails
import com.konopko.stocktest.model.wrapper.WrapperTickerChart
import timber.log.Timber

class Ticker private constructor(val id: String){

    var error: String? = null
        private set

    private var tickerChart: WrapperTickerChart? = null
    private var tickerDetails: ModelTickerDetails.Result? = null //todo use wrapper

    constructor(id: String, tickerDetails: ModelTickerDetails.Result, tickerChart: ModelTickerChart.Result) : this(id){
        this.tickerChart = WrapperTickerChart(tickerChart)
        this.tickerDetails = tickerDetails

        // check ticker is empty
        if ((tickerDetails.summaryProfile?.longBusinessSummary == null && tickerDetails.summaryProfile?.country == null)
                || this.tickerChart == null
                || (this.tickerChart?.getCurrency() == null && this.tickerChart?.getPoints() == null)) {
            this.error = "Empty ticker"
        }
    }

    constructor(id: String, error: String) : this(id){
        this.error = error
    }

    fun getChartData(): Map<Long, Float> {
        val result = tickerChart?.getPoints() ?: emptyMap()
        Timber.d("getChartData = %s", Gson().toJson(result))
        return result
    }

    fun getCurrentValue(): Float? {
        return tickerChart?.getPoints()?.values?.toList()?.last()
    }

    fun getCurrency(): String? {
        return tickerChart?.getCurrency()
    }

    fun getCompanyDesc(): String? {
        return tickerDetails?.summaryProfile?.longBusinessSummary
    }
}