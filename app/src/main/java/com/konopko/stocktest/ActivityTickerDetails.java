package com.konopko.stocktest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.konopko.stocktest.databinding.ActivityTickerDetailsBinding;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class ActivityTickerDetails extends ActivityBase<ActivityTickerDetailsBinding> {

    private ViewModelTickerDetails viewModel;

    public static void open(Context context, String tickerId){
        Intent intent = new Intent(context, ActivityTickerDetails.class);
        intent.putExtra(Intent.EXTRA_UID, tickerId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ticker_details;
    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.ticker_details_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String tickerId = getIntent().getStringExtra(Intent.EXTRA_UID);
        if (tickerId == null) {
            Timber.w("tickerId not set");
            finish();
            return;
        }

        LineChart chart = binding.chart;

        viewModel = ViewModelProviders.of(this, new ModelTickerDetailsFactory(tickerId)).get(ViewModelTickerDetails.class);
        viewModel.getDataTicker().observe(this, ticker -> {

            // company data
            binding.setTickerId(ticker.getId());
            binding.setTickerPrice(String.format(Locale.getDefault(), "%.2f", ticker.getCurrentValue()));
            binding.setTickerDesc(ticker.getCompanyDesc());

            // chart
            if (ticker.getError() == null) {
                chart.setTouchEnabled(false); // disable interactions
                chart.getLegend().setEnabled(false); // hide legend
                // show desc
                Description description = new Description();
                description.setText(String.format("Days/%s", ticker.getCurrency()));
                chart.setDescription(description);

                final List<Long> listDates = FactoryMPAndroidChart.getChartDataTimeDays(ticker);
                LineDataSet lineDataSet = new LineDataSet(FactoryMPAndroidChart.getChartDataSetStepOne(ticker), ticker.getId());
                lineDataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                lineDataSet.setLineWidth(3f);
                // hide circles, use dots
                lineDataSet.setDrawCircleHole(false);
                lineDataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimary));
                lineDataSet.setCircleRadius(1.5f);

                XAxis xAxis = chart.getXAxis();
                xAxis.setDrawGridLines(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                ValueFormatter formatter = new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        long timestamp = listDates.get((int) value);
                        Timber.d("timestamp = %d", timestamp);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(timestamp * 1000);
                        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                    }
                };
                xAxis.setGranularity(1f);
                xAxis.setValueFormatter(formatter);

                chart.getAxisLeft().setEnabled(false);
                chart.getAxisRight().setDrawGridLines(false);
                chart.setMaxVisibleValueCount(0); // hide labels of points

                LineData data = new LineData(lineDataSet);
                chart.setData(data);
                chart.invalidate();
            }
        });
    }
}
