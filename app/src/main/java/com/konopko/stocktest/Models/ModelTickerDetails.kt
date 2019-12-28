package com.konopko.stocktest.Models

import com.konopko.stocktest.BaseResponse

object ModelTickerDetails {

    data class Result(val summaryProfile: SummaryProfile): BaseResponse()
    data class SummaryProfile(val website: String){
        lateinit var zip: String
        lateinit var sector: String
        lateinit var longBusinessSummary: String
        lateinit var state: String
        lateinit var country: String
    }
}