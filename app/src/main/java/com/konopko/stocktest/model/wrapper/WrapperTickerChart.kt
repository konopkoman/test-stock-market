package com.konopko.stocktest.model.wrapper

import com.konopko.stocktest.model.ModelTickerChart

class WrapperTickerChart(private val model: ModelTickerChart.Result) {

    private val points = mutableMapOf<Long, Float>()

    fun getCurrency(): String {
        return model.chart?.result?.get(0)?.meta?.currency ?: ""
    }

    fun getPoints(): Map<Long, Float> {
        if (!points.isEmpty())
            return points

        val timestamps = getTimestampList() ?: return points
        val values = getValueList() ?: return points

        for (i in timestamps.indices)
            points.put(timestamps[i], values[i])

        //todo sort by key

        return points.toMap()
    }

    private fun getTimestampList(): List<Long>? {
        return model.chart?.result?.get(0)?.timestamp
    }

    private fun getValueList(): List<Float>? {
        return model.chart?.result?.get(0)?.indicators?.adjclose?.get(0)?.adjclose
    }

}