package com.konopko.stocktest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

public class ViewModelTickerList extends ViewModel {

    private RepositoryTicker repositoryTicker;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<AdapterTickerListHolder>> dataListTickerHolder;
    private MutableLiveData<String> dataTickerId;
    private MutableLiveData<Integer> dataNotification;


    public ViewModelTickerList(){
        repositoryTicker = RepositoryTicker.getInstance();
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
    }

    public LiveData<List<AdapterTickerListHolder>> getDataListTickerHolder(){
        if (dataListTickerHolder == null){
            dataListTickerHolder = new MutableLiveData<>();
            loadTickerList(repositoryTicker);
        }
        return dataListTickerHolder;
    }

    public LiveData<Integer> getDataNotification(){
        //todo should use Rx Publish Subject instead, to avoid getting the last notification when observer is subscribing
        if (dataNotification == null)
            dataNotification = new MutableLiveData<>();
        return dataNotification;
    }

    public LiveData<String> getDataTickerId(){
        if (dataTickerId == null)
            dataTickerId = new MutableLiveData<>();
        return dataTickerId;
    }

    public void openTickerDetails(@Nullable Ticker ticker){
        if (ticker != null && ticker.getError() == null)
            dataTickerId.setValue(ticker.getId());
        else
            dataNotification.setValue(R.string.ticker_error);
    }

    private void loadTickerList(RepositoryTicker repositoryTicker){
        disposable.add(
                repositoryTicker.subscribeMapTickers().subscribe(
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
