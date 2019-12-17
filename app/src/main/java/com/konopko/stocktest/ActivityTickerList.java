package com.konopko.stocktest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.konopko.stocktest.databinding.ActivityTickerListBinding;

public class ActivityTickerList extends AppCompatActivity {

    private ViewModelTickerList viewModel;
    private ActivityTickerListBinding binding;
    private AdapterTickerList adapter;

    public static void open(AppCompatActivity activity){
        Intent intent = new Intent(activity, ActivityTickerList.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ticker_list);

        viewModel = ViewModelProviders.of(this).get(ViewModelTickerList.class);

        adapter = new AdapterTickerList();
        binding.listTicker.setNestedScrollingEnabled(false);
        binding.listTicker.setAdapter(adapter);

        viewModel.getDataListTickerHolder().observe(this, list -> adapter.setList(list));
    }
}
