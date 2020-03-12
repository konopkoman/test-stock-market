package com.konopko.stocktest.ext.chart

import com.github.mikephil.charting.data.Entry
import com.konopko.stocktest.Ticker

interface ChartHelper {

    fun getChartDataSet(ticker: Ticker): List<Entry>

    fun getChartDataSetStepOne(ticker: Ticker): List<Entry>

    fun getChartDataTimeDays(ticker: Ticker): List<Long>
}