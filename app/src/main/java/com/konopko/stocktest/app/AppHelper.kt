package com.konopko.stocktest.app

interface AppHelper {

    val urlBase: String
        get() = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/"
    val apiHeaderHost: String
        get() = "apidojo-yahoo-finance-v1.p.rapidapi.com"
    val apiHeaderKey: String
        get() = "61da84331amsh74156549a13e1b4p1f04bejsn1a1b92a2e0a0"

    fun getCurrencySign(currency: String): String

}