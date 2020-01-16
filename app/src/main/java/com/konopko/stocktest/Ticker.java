package com.konopko.stocktest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.konopko.stocktest.model.ModelTickerChart;
import com.konopko.stocktest.model.ModelTickerDetails;
import com.konopko.stocktest.model.wrapper.WrapperTickerChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class Ticker {

    private String id;

    private WrapperTickerChart tickerChart;
    private ModelTickerDetails.Result tickerDetails; //todo use wrapper

    private String error;

    public Ticker(@NonNull String id, @NonNull ModelTickerDetails.Result tickerDetails, @NonNull ModelTickerChart.Result tickerChart){
        Timber.d("tickerDetails = %s", new Gson().toJson(tickerDetails));
        Timber.d("tickerChart = %s", new Gson().toJson(tickerChart));
        this.id = id;
        this.tickerChart = new WrapperTickerChart(tickerChart);
        this.tickerDetails = tickerDetails;

        //fakeInit();
    }

    public Ticker(@NonNull String id, @NonNull String error){
        this.id = id;
        this.error = error;
    }

    @Nullable
    public String getError() {
        return error;
    }

    @Nullable
    public String getId(){
        return id;
    }

    @NonNull
    public LinkedHashMap<Long, Float> getChartData(){
        LinkedHashMap<Long, Float> result =
                tickerChart == null
                ? new LinkedHashMap<>()
                : new LinkedHashMap<>(Collections.unmodifiableMap(tickerChart.getPoints()));
        Timber.d("getChartData = %s", new Gson().toJson(result));
        return result;
    }

    @Nullable
    public Float getCurrentValue(){
        try {
            List<Float> values = new ArrayList<>(tickerChart.getPoints().values());
            return Objects.requireNonNull(values).get(values.size() -1);
        } catch (NullPointerException e){
            Timber.w(e);
            return null;
        }
    }

    @Nullable
    public String getCurrency(){
        return tickerChart.getCurrency();
    }

    @Nullable
    public String getCompanyDesc(){
        try {
            return Objects.requireNonNull(tickerDetails.getSummaryProfile()).getLongBusinessSummary();
        } catch (NullPointerException e){
            Timber.e(e);
            return null;
        }
    }

    /*private void fakeInit(){
        if (tickerChart == null)
            tickerChart = new TickerChart();
        tickerChart.getPoints().clear();
        tickerChart.getPoints().put((long) 1570455000, (float) 1208.25);
        tickerChart.getPoints().put((long) 1570541400, (float) 1190.13);
        tickerChart.getPoints().put((long)1570627800, (float) 1202.40);
    }*/



}
