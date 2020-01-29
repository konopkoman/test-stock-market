package com.konopko.stocktest.model

import com.konopko.stocktest.BaseResponse

object ModelTickerChart {

    data class Result(val chart: Chart?): BaseResponse()

    data class Chart(val result: List<ResultItem>?){
        var error: String? = null
    }

    data class ResultItem(val indicators: Indicators?){
        var timestamp: List<Long>? = null
        var meta: TickerChartMeta? = null
    }

    data class TickerChartMeta(val currency: String?)

    data class Indicators(val adjclose: List<AdjClose>?)

    data class AdjClose(val adjclose: List<Float>?)
}