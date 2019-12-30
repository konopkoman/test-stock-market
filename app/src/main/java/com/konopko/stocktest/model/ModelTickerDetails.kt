package com.konopko.stocktest.model

import com.konopko.stocktest.BaseResponse

object ModelTickerDetails {

    data class Result(public val summaryProfile: SummaryProfile? = null): BaseResponse()

    data class SummaryProfile(var website: String){
        var zip: String? = null
        var sector: String? = null
        var longBusinessSummary: String? = null
        var state: String? = null
        var country: String? = null
    }
}