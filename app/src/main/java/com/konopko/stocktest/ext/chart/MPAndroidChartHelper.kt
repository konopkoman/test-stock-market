package com.konopko.stocktest.ext.chart

import com.github.mikephil.charting.data.Entry
import com.konopko.stocktest.Ticker
import java.util.*

class MPAndroidChartHelper {

    companion object : ChartHelper {

        override fun getChartDataSet(ticker: Ticker): List<Entry> {
            val result: MutableList<Entry> = ArrayList()
            for (entry: Map.Entry<Long, Float>  in ticker.getChartData().entries)
                result.add(Entry(entry.key.toFloat(), entry.value))
            return result
        }

        override fun getChartDataSetStepOne(ticker: Ticker): List<Entry> {
            val result: MutableList<Entry> = ArrayList()
            var i = 0
            for (entry: Map.Entry<Long, Float>  in ticker.getChartData().entries){
                    result.add(Entry(i.toFloat(), entry.value))
                i = i.inc()
            }
            return result
        }

        override fun getChartDataTimeDays(ticker: Ticker): List<Long> {
            val result: MutableList<Long> = ArrayList()
            for (entry: Map.Entry<Long, Float>  in ticker.getChartData().entries)
                result.add(entry.key)
            return result
        }
    }
}