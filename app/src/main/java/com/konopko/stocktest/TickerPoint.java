package com.konopko.stocktest;

public class TickerPoint {

    private long time;
    private float value;

    public TickerPoint(long time, float value){
        this.time = time;
        this.value = value;
    }

    public long getTime() {
        return time;
    }

    public float getValue() {
        return value;
    }
}
