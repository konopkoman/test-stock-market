package com.konopko.stocktest;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.konopko.stocktest.databinding.ActivityAskTickersBinding;

public class ActivityAskTickers extends AppCompatActivity {

    private final int TAGS_MAX_LENGTH = 30;
    private ActivityAskTickersBinding binding;
    private ViewModelAskTickers viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_ask_tickers);
        binding.setHandler(this);

        viewModel = ViewModelProviders.of(this).get(ViewModelAskTickers.class);
        viewModel.getDataNext().observe(this, open -> {
            if (open)
                openActivityList();
            else
                Toast.makeText(this, R.string.tickers_list_error, Toast.LENGTH_SHORT).show();
        });

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
        viewModel.next(binding.tagsEditText.getTags());
    }

    private void openActivityList(){
        ActivityTickerList.open(this);
        clearTickers();
    }

    private void clearTickers(){
        binding.tagsEditText.setTags(null);
    }
}
