package com.konopko.stocktest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.util.List;

import timber.log.Timber;

public class ViewModelAskTickers extends ViewModel {

    private MutableLiveData<Boolean> dataNext;

    public LiveData<Boolean> getDataNext(){
        if (dataNext == null)
            dataNext = new MutableLiveData<>();
        return dataNext;
    }

    public void next(@NonNull List<String> tickers){
        Timber.d("clickNext tags = %s", new Gson().toJson(tickers));
        if (!tickers.isEmpty()) {
            searchTickers(tickers);
            dataNext.setValue(true);
        }
    }

    private void searchTickers(@NonNull List<String> tickers){
        RepositoryTicker.getInstance().findTickers(tickers);
    }

}
