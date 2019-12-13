package com.konopko.stocktest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.Objects;
import java.util.TreeMap;

import timber.log.Timber;

public class Ticker {

    private String id;

    private TickerChart tickerChart;
    private TickerDetails tickerDetails;

    private String error;

    public Ticker(@NonNull String id, @NonNull TickerDetails tickerDetails, @NonNull TickerChart tickerChart){
        Timber.d("tickerDetails = %s", new Gson().toJson(tickerDetails));
        Timber.d("tickerChart = %s", new Gson().toJson(tickerChart));
        this.id = id;
        this.tickerChart = tickerChart;
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
    public TreeMap<Long, Float> getChartData(){
        Timber.d("getChartData = %s", new Gson().toJson(new TreeMap<>(Collections.unmodifiableMap(tickerChart.getPoints()))));
        return new TreeMap<>(Collections.unmodifiableMap(tickerChart.getPoints()));
    }

    @Nullable
    public Float getCurrentValue(){
        try {
            return Objects.requireNonNull(tickerChart.getPoints().lastEntry()).getValue();
        } catch (NullPointerException e){
            Timber.e(e);
            return null;
        }
    }

    @Nullable
    public String getCompanyDesc(){
        try {
            return Objects.requireNonNull(tickerDetails.summaryProfile).longBusinessSummary;
        } catch (NullPointerException e){
            Timber.e(e);
            return null;
        }
    }

    private void fakeInit(){
        if (tickerChart == null)
            tickerChart = new TickerChart();
        tickerChart.getPoints().clear();
        tickerChart.getPoints().put((long) 1570455000, (float) 1208.25);
        tickerChart.getPoints().put((long) 1570541400, (float) 1190.13);
        tickerChart.getPoints().put((long)1570627800, (float) 1202.40);
    }



}
