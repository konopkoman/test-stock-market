package com.konopko.stocktest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.TreeMap;

import timber.log.Timber;

public class TickerChart extends BaseResponse {

    private Chart chart;
    private TreeMap<Long, Float> points = new TreeMap<>();

    @Nullable
    public String getCurrency(){
        try {
            return chart.result.get(0).meta.currency;
        } catch (NullPointerException e){
            Timber.e(e);
            return null;
        }
    }

    @NonNull
    public TreeMap<Long, Float> getPoints(){

        if (!points.isEmpty())
            return points;

        List<Long> timestamps = getTimestampList();
        if (timestamps == null) return points;
        List<Float> values = getValueList();
        if (values == null) return points;

        for (int i = 0; i < timestamps.size(); i++)
            points.put(timestamps.get(i), values.get(i));

        return points;
    }

    @Nullable
    private List<Long> getTimestampList(){
        try {
            return chart.result.get(0).timestamp;
        } catch (NullPointerException e){
            Timber.e(e);
            return null;
        }
    }

    @Nullable
    private List<Float> getValueList(){
        try {
            return chart.result.get(0).indicators.adjclose.get(0).adjclose;
        } catch (NullPointerException e){
            Timber.e(e);
            return null;
        }
    }

    class Chart {
        String error;
        List<ResultItem> result;
    }

    class ResultItem {
        TickerChartMeta meta;
        List<Long> timestamp;
        Indicators indicators;
    }

    class TickerChartMeta {
        String currency;
    }

    class Indicators {
        List<AdjClose> adjclose;
    }

    class AdjClose {
        List<Float> adjclose;
    }
}
