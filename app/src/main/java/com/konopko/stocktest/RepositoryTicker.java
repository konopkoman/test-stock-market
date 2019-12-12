package com.konopko.stocktest;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

public class RepositoryTicker {

    private static RepositoryTicker self;

    private CompositeDisposable disposable;

    private LinkedHashMap<String, Ticker> mapTicker = new LinkedHashMap<>(); // map key is ticker's uid
    private BehaviorSubject<LinkedHashMap<String, Ticker>> subject = BehaviorSubject.create();

    private RepositoryTicker(){

    }

    public static RepositoryTicker getInstance(){
        if (self == null)
            self = new RepositoryTicker();
        return self;
    }

    public void addTicker(@NonNull String tickerId, @Nullable Ticker ticker){
        mapTicker.put(tickerId, ticker);
        subject.onNext(mapTicker);
    }

    @Nullable
    public Ticker getTicker(@NonNull String tickerId){
        return mapTicker.get(tickerId);
    }

    public Observable<LinkedHashMap<String, Ticker>> subscribe(){
        return subject;
    }

    public void findTickers(@NonNull Context context, @NonNull List<String> listTickers){
        disposable = new CompositeDisposable();
        mapTicker.clear();

        ApiService apiService = ApiClient.getClient(context)
                .create(ApiService.class);

        for (String tickerId : listTickers){
            disposable.add(apiService.fetchTickerDetails(tickerId).subscribeOn(Schedulers.io())
                    .zipWith(apiService.fetchTickerChart(tickerId).subscribeOn(Schedulers.io()),
                            new BiFunction<TickerDetails, TickerChart, Ticker>(){
                                @Override
                                public Ticker apply(TickerDetails tickerDetails, TickerChart tickerChart) throws Exception {
                                    Timber.d("apply");
                                    return new Ticker(tickerId, tickerDetails, tickerChart);
                                }
                            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(ticker -> addTicker(tickerId, ticker), e -> {
                        addTicker(tickerId, new Ticker(tickerId, e.getMessage() == null ? e.toString() : e.getMessage()));
                        Timber.w(e,"subscribe error");
                    }));
        }
    }
}
