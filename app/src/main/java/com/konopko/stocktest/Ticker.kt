package com.konopko.stocktest

import com.google.gson.Gson
import com.konopko.stocktest.model.ModelTickerChart
import com.konopko.stocktest.model.ModelTickerDetails
import com.konopko.stocktest.model.wrapper.WrapperTickerChart
import com.konopko.stocktest.model.wrapper.WrapperTickerDetails
import timber.log.Timber

class Ticker private constructor(val id: String){

    var error: String? = null
        private set

    private lateinit var tickerChart: WrapperTickerChart
    private lateinit var tickerDetails: WrapperTickerDetails

    constructor(id: String, tickerDetails: ModelTickerDetails.Result, tickerChart: ModelTickerChart.Result) : this(id){
        this.tickerChart = WrapperTickerChart(tickerChart)
        this.tickerDetails = WrapperTickerDetails(tickerDetails)

        // check ticker is empty
        if ((tickerDetails.summaryProfile?.longBusinessSummary == null && tickerDetails.summaryProfile?.country == null)
                || (this.tickerChart.getCurrency() == null && this.tickerChart.getPoints() == null)) {
            this.error = "Empty ticker"
        }
    }

    constructor(id: String, error: String) : this(id){
        this.error = error
    }

    fun getChartData(): Map<Long, Float>
        = (tickerChart.getPoints() ?: emptyMap()).also {
            Timber.d("getChartData = %s", Gson().toJson(it))
        }

    fun getCurrentValue(): Float?
        = tickerChart.getPoints()?.values?.toList()?.last()

    fun getCurrency(): String?
        = tickerChart.getCurrency()

    fun getCompanyDesc(): String?
        = tickerDetails.getBusinessSummary()
}