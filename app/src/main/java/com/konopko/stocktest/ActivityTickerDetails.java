package com.konopko.stocktest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import com.konopko.stocktest.databinding.ActivityTickerDetailsBinding;

import java.util.Locale;

import timber.log.Timber;

public class ActivityTickerDetails extends AppCompatActivity {

    private ViewModelTickerDetails viewModel;
    private ActivityTickerDetailsBinding binding;

    public static void open(AppCompatActivity activity, String tickerId){
        Intent intent = new Intent(activity, ActivityTickerDetails.class);
        intent.putExtra(Intent.EXTRA_UID, tickerId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ticker_details);

        String tickerId = getIntent().getStringExtra(Intent.EXTRA_UID);
        if (tickerId == null) {
            Timber.w("tickerId not set");
            finish();
            return;
        }

        viewModel = ViewModelProviders.of(this, new ModelTickerDetailsFactory(tickerId)).get(ViewModelTickerDetails.class);
        viewModel.getDataTicker().observe(this, ticker -> {
            binding.setTickerId(ticker.getId());
            binding.setTickerPrice(String.format(Locale.getDefault(), "%.2f", ticker.getCurrentValue()));
            binding.setTickerDesc(ticker.getCompanyDesc());
            //todo set chart
        });
    }
}
