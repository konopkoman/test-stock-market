package com.konopko.stocktest.network

import com.konopko.stocktest.model.ModelTickerChart
import com.konopko.stocktest.model.ModelTickerDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    // Fetch ticker details
    @GET("stock/get-detail?region=US&lang=en")
    fun fetchTickerDetails(@Query("symbol") tickerId: String): Single<ModelTickerDetails.Result>

    // Fetch ticker chart
    @GET("stock/v2/get-chart?interval=1d&region=US&lang=en&range=1mo")
    fun fetchTickerChart(@Query("symbol") tickerId: String): Single<ModelTickerChart.Result>
}