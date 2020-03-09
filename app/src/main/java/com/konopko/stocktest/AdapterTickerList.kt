package com.konopko.stocktest

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.konopko.stocktest.app.AppHelper
import com.konopko.stocktest.databinding.ListItemTickerBinding
import io.reactivex.subjects.PublishSubject
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.*

class AdapterTickerList: RecyclerView.Adapter<AdapterTickerList.TickerViewHolder>() {

    private val onClickSubject = PublishSubject.create<AdapterTickerListHolder>()
    private var list: List<AdapterTickerListHolder> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TickerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTickerBinding.inflate(inflater)
        return TickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TickerViewHolder, position: Int) {
        list[position].let {
            val item = it
            holder.bind(it)
            holder.itemView.setOnClickListener {
                onClickSubject.onNext(item)
            }
        }
    }

    override fun getItemCount()
            = list.size

    fun setList(list: List<AdapterTickerListHolder>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun subscribeItemClick()
        = onClickSubject


    class TickerViewHolder(val binding: ListItemTickerBinding): RecyclerView.ViewHolder(binding.root), KoinComponent {

        private val app : AppHelper by inject()
        private var context: Context = binding.root.context

        fun bind(item: AdapterTickerListHolder) {
            binding.tickerId = item.tickerId
            val ticker = item.ticker
            binding.inProgress = ticker == null

            // ticker has been loaded
            if (ticker != null) {
                Timber.d("TickerViewHolder bind ticker error = %s", ticker.error)
                if (ticker.error != null)
                    binding.tickerError = ticker.error
                else {
                    binding.tickerCurrency =
                            if (ticker.getCurrency() == null) null
                            else app.getCurrencySign(ticker.getCurrency() ?: "?")
                    binding.tickerPrice = String.format(Locale.getDefault(), "%.2f", ticker.getCurrentValue())
                    setChart(binding.chart, ticker)
                }
            }
            binding.executePendingBindings()
        }

        private fun setChart(chart: LineChart,ticker: Ticker ){
            chart.setTouchEnabled(false) // disable interactions
            chart.legend.isEnabled = false // hide legend
            chart.description = null

            val lineDataSet = LineDataSet(FactoryMPAndroidChart.getChartDataSetStepOne(ticker), ticker.id)
            lineDataSet.color = ContextCompat.getColor(context, R.color.colorPrimary)
            lineDataSet.lineWidth = 2f
            // hide circles, use dots
            lineDataSet.setDrawCircleHole(false)
            lineDataSet.setCircleColor(ContextCompat.getColor(context, R.color.colorPrimary))
            lineDataSet.circleRadius = 1f

            chart.xAxis.isEnabled = false
            chart.axisLeft.isEnabled = false
            chart.axisRight.isEnabled = false
            chart.setMaxVisibleValueCount(0) // hide labels of points

            val data = LineData(lineDataSet)
            chart.data = data
            chart.invalidate()
        }

    }

}