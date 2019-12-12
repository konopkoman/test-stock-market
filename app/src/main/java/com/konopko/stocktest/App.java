package com.konopko.stocktest;

import android.app.Application;

import timber.log.Timber;

public class App extends Application {

    public static final String BASE_URL = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/";
    public static final String HEADER_HOST = "apidojo-yahoo-finance-v1.p.rapidapi.com";
    public static final String HEADER_API_KEY = "61da84331amsh74156549a13e1b4p1f04bejsn1a1b92a2e0a0";

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }

    }

}
