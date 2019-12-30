package com.konopko.stocktest;

import com.konopko.stocktest.model.ModelTickerChart;
import com.konopko.stocktest.model.ModelTickerDetails;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    // Fetch ticker details
    @GET("stock/get-detail?region=US&lang=en")
    Single<ModelTickerDetails.Result> fetchTickerDetails(@Query("symbol") String tickerId);

    // Fetch ticker chart
    @GET("stock/v2/get-chart?interval=1d&region=US&lang=en&range=1mo")
    Single<ModelTickerChart.Result> fetchTickerChart(@Query("symbol") String tickerId);

}
