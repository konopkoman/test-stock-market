package com.konopko.stocktest.ext.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.konopko.stocktest.ViewModelTickerDetails

class ViewModelTickerDetailsFactory constructor(private val tickerId: String): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try {
            ViewModelTickerDetails(tickerId) as T
        } catch (e: Exception) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }
    }
}