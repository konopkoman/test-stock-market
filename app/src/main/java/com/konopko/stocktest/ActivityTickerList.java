package com.konopko.stocktest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.konopko.stocktest.databinding.ActivityTickerListBinding;

import io.reactivex.disposables.CompositeDisposable;

public class ActivityTickerList extends AppCompatActivity {

    private ViewModelTickerList viewModel;
    private ActivityTickerListBinding binding;
    private AdapterTickerList adapter;
    private CompositeDisposable disposable = new CompositeDisposable();

    public static void open(Context context){
        Intent intent = new Intent(context, ActivityTickerList.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ticker_list);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.ticker_list_title);

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
