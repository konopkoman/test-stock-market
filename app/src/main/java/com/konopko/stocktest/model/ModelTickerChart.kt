package com.konopko.stocktest.model

import com.konopko.stocktest.BaseResponse

object ModelTickerChart {

    data class Result(val chart: Chart? = null): BaseResponse()

    data class Chart(var result: List<ResultItem>? = null){
        var error: String? = null
    }

    data class ResultItem(var indicators: Indicators? = null){
        var timestamp: List<Long>? = null
        var meta: TickerChartMeta? = null
    }

    data class TickerChartMeta(var currency: String? = null)

    data class Indicators(var adjclose: List<AdjClose>? = null)

    data class AdjClose(var adjclose: List<Float>? = null)
}