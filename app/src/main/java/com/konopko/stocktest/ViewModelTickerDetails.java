package com.konopko.stocktest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelTickerDetails extends ViewModel {

    private MutableLiveData<Ticker> dataTicker = new MutableLiveData<>();

    public ViewModelTickerDetails(@NonNull String tickerId){
        dataTicker.setValue(RepositoryTicker.getInstance().getTicker(tickerId));
    }

    public LiveData<Ticker> getDataTicker(){
        return dataTicker;
    }

}
