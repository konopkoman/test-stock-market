package com.konopko.stocktest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

public class ViewModelTickerList extends ViewModel {

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<AdapterTickerListHolder>> dataListTickerHolder;

    public LiveData<List<AdapterTickerListHolder>> getDataListTickerHolder(){
        if (dataListTickerHolder == null){
            dataListTickerHolder = new MutableLiveData<>();
            loadTickerList();
        }
        return dataListTickerHolder;
    }

    private void loadTickerList(){
        disposable.add(
                RepositoryTicker.getInstance().subscribeMapTickers().subscribe(
                        map -> dataListTickerHolder.setValue(getTickerListHolder(map))
                ));
    }

    private List<AdapterTickerListHolder> getTickerListHolder(@NonNull LinkedHashMap<String, Ticker> map){
        List<AdapterTickerListHolder> result = new ArrayList<>();
        for (Map.Entry<String, Ticker> item : map.entrySet())
            result.add(new AdapterTickerListHolder(item.getKey(), item.getValue()));
        return result;
    }

}
