package com.konopko.stocktest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.konopko.stocktest.databinding.ActivityTickerDetailsBinding
import timber.log.Timber
import java.util.*

class ActivityTickerDetails : ActivityBase<ActivityTickerDetailsBinding>() {

    private lateinit var viewModel: ViewModelTickerDetails

    companion object {
        fun open(context: Context, tickerId: String ){
            val intent = Intent(context, ActivityTickerDetails::class.java)
            intent.putExtra(Intent.EXTRA_UID, tickerId)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId()
            = R.layout.activity_ticker_details

    override fun getPageTitle()
            = getString(R.string.ticker_details_title)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tickerId = intent.getStringExtra(Intent.EXTRA_UID)
        if (tickerId == null) {
            Timber.w("tickerId not set")
            finish()
            return
        }

        viewModel = ViewModelProvider(this, ModelTickerDetailsFactory(tickerId)).get(ViewModelTickerDetails::class.java)

        viewModel.dataTicker.observe(this, Observer { ticker: Ticker? ->
            if (ticker == null || ticker.error != null)
                Timber.w("Ticker $tickerId is not available, error = %s", ticker?.error)
            else
                showTicker(ticker)
        })
    }

    private fun showTicker(ticker: Ticker){
        // company data
        binding.tickerId = ticker.id
        binding.tickerPrice = String.format(Locale.getDefault(), "%.2f", ticker.getCurrentValue())
        binding.tickerDesc = ticker.getCompanyDesc()

        // chart
        val chart = binding.chart

        chart.setTouchEnabled(false) // disable interactions
        chart.getLegend().setEnabled(false) // hide legend

        // show desc
        val description = Description()
        description.text = String.format("Days/%s", ticker.getCurrency())
        chart.setDescription(description)

        val listDates = FactoryMPAndroidChart.getChartDataTimeDays(ticker)
        val lineDataSet = LineDataSet(FactoryMPAndroidChart.getChartDataSetStepOne(ticker), ticker.id)
        lineDataSet.color = ContextCompat.getColor(this, R.color.colorPrimary)
        lineDataSet.lineWidth = 3f
        // hide circles, use dots
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimary))
        lineDataSet.circleRadius = 1.5f

        val xAxis = chart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                val timestamp = listDates[value.toInt()]
                Timber.d("timestamp = %d", timestamp)
                val cal = Calendar.getInstance()
                cal.timeInMillis = timestamp * 1000
                return cal[Calendar.DAY_OF_MONTH].toString()
            }
        }
        xAxis.granularity = 1f
        xAxis.valueFormatter = formatter

        chart.axisLeft.isEnabled = false
        chart.axisRight.setDrawGridLines(false)
        chart.setMaxVisibleValueCount(0) // hide labels of points


        val data = LineData(lineDataSet)
        chart.data = data
        chart.invalidate()
    }

}