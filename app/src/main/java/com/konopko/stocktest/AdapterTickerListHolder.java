package com.konopko.stocktest;

public class AdapterTickerListHolder {

    private String tickerId;
    private Ticker ticker;

    public AdapterTickerListHolder(String tickerId, Ticker ticker){
        this.tickerId = tickerId;
        this.ticker = ticker;
    }

    public String getTickerId() {
        return tickerId;
    }

    public Ticker getTicker() {
        return ticker;
    }
}
