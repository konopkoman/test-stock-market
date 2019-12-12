package com.konopko.stocktest;

public class TickerDetails extends BaseResponse {

    TickerDetailsSummaryProfile summaryProfile;

    public TickerDetails(){}

    class TickerDetailsSummaryProfile {

        String zip;
        String sector;
        String longBusinessSummary;
        String state;
        String country;
        String website;

    }
}
