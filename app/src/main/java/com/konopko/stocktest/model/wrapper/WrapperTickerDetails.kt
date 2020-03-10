package com.konopko.stocktest.model.wrapper

import com.konopko.stocktest.model.ModelTickerDetails

class WrapperTickerDetails(private val model: ModelTickerDetails.Result) {

    fun getBusinessSummary(): String?
        = model.summaryProfile?.longBusinessSummary
}