package com.konopko.stocktest;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ModelTickerDetailsFactory extends ViewModelProvider.NewInstanceFactory {

    private final String tickerId;

    public ModelTickerDetailsFactory(String tickerId) {
        super();
        this.tickerId = tickerId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return (T) new ViewModelTickerDetails(tickerId);
        } catch (Exception e){
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }

}
