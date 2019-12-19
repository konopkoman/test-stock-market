package com.konopko.stocktest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.konopko.stocktest.databinding.ActivityTickerListBinding;

import io.reactivex.disposables.CompositeDisposable;

public class ActivityTickerList extends ActivityBase<ActivityTickerListBinding> {

    private ViewModelTickerList viewModel;
    private AdapterTickerList adapter;
    private CompositeDisposable disposable = new CompositeDisposable();

    public static void open(Context context){
        Intent intent = new Intent(context, ActivityTickerList.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ticker_list;
    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.ticker_list_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ViewModelTickerList.class);

        adapter = new AdapterTickerList();
        binding.listTicker.setNestedScrollingEnabled(false);
        binding.listTicker.setAdapter(adapter);

        viewModel.getDataListTickerHolder().observe(this,
                list -> adapter.setList(list));

        viewModel.getDataTickerId().observe(this,
                tickerId -> ActivityTickerDetails.open(this, tickerId));

        viewModel.getDataNotification().observe(this,
                notification -> Toast.makeText(this, notification, Toast.LENGTH_SHORT).show());

        disposable.add(adapter.subscribeItemClick().subscribe(tickerListHolder -> viewModel.openTickerDetails(tickerListHolder.getTicker())));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
