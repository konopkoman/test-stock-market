package com.konopko.stocktest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.konopko.stocktest.dagger.AppComponent;
import com.konopko.stocktest.dagger.DaggerAppComponent;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class App extends Application {

    public static final String BASE_URL = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/";
    public static final String HEADER_HOST = "apidojo-yahoo-finance-v1.p.rapidapi.com";
    public static final String HEADER_API_KEY = "61da84331amsh74156549a13e1b4p1f04bejsn1a1b92a2e0a0";

    private static App instance;
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        component = DaggerAppComponent.create();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }

    }

    @NonNull
    public static App getInstance(){
        return instance;
    }

    @NonNull
    public static AppComponent getComponent() {
        return component;
    }

    private static Map<String, String> currencySign = new HashMap<String, String>() {{
        put("USD", "$");
    }};

    public static String getCurrencySign(@Nullable String currency){
        return currencySign.containsKey(currency) ? currencySign.get(currency) : currency;
    }

}
