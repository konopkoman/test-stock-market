package com.konopko.stocktest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.gson.Gson;
import com.konopko.stocktest.databinding.ActivityMainBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable disposable = new CompositeDisposable();
    private ActivityMainBinding binding;
    private LineChart chart;
    private TickerDetails tickerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        chart = binding.chart;

        //RepositoryTicker.getInstance().addTicker("GOOGL", new Ticker(null, null));
        RepositoryTicker.getInstance().findTickers(getApplicationContext(), new ArrayList<String>(Arrays.asList("GOOGL")));

        Timber.d("app started");

        disposable.add(RepositoryTicker.getInstance().subscribe().subscribe(map -> {
            for(Map.Entry<String, Ticker> entry : map.entrySet()) {
                Timber.d("ticker name = %s, ticker data = %s", entry.getKey(), new Gson().toJson(entry.getValue()));
                Ticker ticker = entry.getValue();
                if (ticker != null && ticker.getError() == null) {
                    final List<Long> listDates = FactoryMPAndroidChart.getChartDataTimeDays(ticker);
                    LineDataSet lineDataSet = new LineDataSet(FactoryMPAndroidChart.getChartDataSetStepOne(ticker), "Label");
                    lineDataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    lineDataSet.setValueTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                    XAxis xAxis = chart.getXAxis();
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


                    LineData data = new LineData(lineDataSet);
                    chart.setData(data);
                    chart.invalidate();
                }
            }
        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
