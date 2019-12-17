package com.konopko.stocktest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AdapterTickerListHolder {

    private String tickerId;
    private Ticker ticker;

    public AdapterTickerListHolder(@NonNull String tickerId, @Nullable Ticker ticker){
        this.tickerId = tickerId;
        this.ticker = ticker;
    }

    @NonNull
    public String getTickerId() {
        return tickerId;
    }

    @Nullable
    public Ticker getTicker() {
        return ticker;
    }
}
