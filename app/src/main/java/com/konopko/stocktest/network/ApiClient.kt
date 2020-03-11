package com.konopko.stocktest.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.konopko.stocktest.app.AppHelper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {
        private val REQUEST_TIMEOUT = 10

        fun getClient(helper: AppHelper): Retrofit
            = Retrofit.Builder()
                    .baseUrl(helper.urlBase)
                    .client(initOkHttp(helper))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

        private fun initOkHttp(helper: AppHelper): OkHttpClient {
            val httpClient = OkHttpClient().newBuilder()
                    .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            httpClient.addInterceptor(interceptor)

            httpClient.addInterceptor {
                val original: Request = it.request()
                val requestBuilder: Request.Builder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("x-rapidapi-host", helper.apiHeaderHost)
                        .addHeader("x-rapidapi-key", helper.apiHeaderKey)

                val request: Request = requestBuilder.build()
                return@addInterceptor it.proceed(request)
            }
            return httpClient.build()
        }
    }

}