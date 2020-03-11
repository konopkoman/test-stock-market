package com.konopko.stocktest.model

object ModelTickerDetails {

    data class Result(val summaryProfile: SummaryProfile?): BaseResponse()

    data class SummaryProfile(val website: String?){
        var zip: String? = null
        var sector: String? = null
        var longBusinessSummary: String? = null
        var state: String? = null
        var country: String? = null
    }
}