package com.konopko.stocktest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class ViewModelTickerDetails extends ViewModel {

    @Inject
    RepositoryTicker repositoryTicker;

    private MutableLiveData<Ticker> dataTicker = new MutableLiveData<>();

    public ViewModelTickerDetails(@NonNull String tickerId){
        App.getComponent().inject(this);
        dataTicker.setValue(repositoryTicker.getTicker(tickerId));
    }

    public LiveData<Ticker> getDataTicker(){
        return dataTicker;
    }

}
