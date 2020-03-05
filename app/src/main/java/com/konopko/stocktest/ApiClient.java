package com.konopko.stocktest;

import androidx.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.konopko.stocktest.app.AppHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static int REQUEST_TIMEOUT = 10;
    private static OkHttpClient okHttpClient;

    public static Retrofit getClient(@NonNull AppHelper helper) {

        if (okHttpClient == null)
            okHttpClient = initOkHttp(helper);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(helper.getUrlBase())
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient initOkHttp(@NonNull AppHelper helper) {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("x-rapidapi-host", helper.getApiHeaderHost())
                        .addHeader("x-rapidapi-key", helper.getApiHeaderKey());

                // Adding Authorization token (API Key)
                // Requests will be denied without API key
                /*if (!TextUtils.isEmpty(PrefUtils.getApiKey(context))) {
                    requestBuilder.addHeader("Authorization", PrefUtils.getApiKey(context));
                }*/

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return httpClient.build();
    }
}
