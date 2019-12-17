package com.konopko.stocktest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;

import com.google.gson.Gson;
import com.konopko.stocktest.databinding.ActivityAskTickersBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

public class ActivityAskTickers extends AppCompatActivity {

    private final int TAGS_MAX_LENGTH = 30;
    private ActivityAskTickersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_ask_tickers);
        binding.setHandler(this);

        binding.tagsEditText.setMaxLines(2);
        binding.tagsEditText.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start, int end, Spanned spanned, int dStart, int dEnd) {
                        if(cs.equals(" ")) // ignore space
                            return "";
                        return cs;
                    }
                },
                new InputFilter.AllCaps(), // set all caps
                new InputFilter.LengthFilter(TAGS_MAX_LENGTH)
        });

    }

    public void onClickNext(View view){
        List<String> tickers = binding.tagsEditText.getTags();
        Timber.d("clickNext tags = %s", new Gson().toJson(tickers));
        if (tickers.isEmpty()) return;
        searchTickers(tickers);
        openActivityList();
        clearTickers();
    }

    private void searchTickers(@NonNull List<String> tickers){
        Timber.d("searchTickers list = %s", new Gson().toJson(tickers));
        RepositoryTicker.getInstance().findTickers(tickers);
    }

    private void openActivityList(){
        ActivityTickerList.open(this);
    }

    private void clearTickers(){
        binding.tagsEditText.setTags(null);
    }
}
