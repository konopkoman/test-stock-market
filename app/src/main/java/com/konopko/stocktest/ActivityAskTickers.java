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
    private final String TAG_SEPARATOR = " "; // white space

    private String text;

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
        Timber.d("clickNext");
        searchTickers(text);
        openActivityList();
    }

    public String getTags() {
        return text;
    }

    public void setTags(String text) {
        binding.buttonNext.setEnabled(checkTags(text));
        this.text = text;
        Timber.d("setTags = %s", text);
    }

    private boolean checkTags(@NonNull String text){
        return text.contains(TAG_SEPARATOR);
    }

    @NonNull
    private List<String> getListTickers(@NonNull String text){
        String[] words = text.split("\\s+");
        List<String> result = new ArrayList<>(Arrays.asList(words));

        Pattern p = Pattern.compile(TAG_SEPARATOR);
        Matcher m = p.matcher(text);
        int count = 0;
        while (m.find())
            ++count;

        if (count < result.size())
            result.remove(result.size()-1);

        return result;
    }

    private void searchTickers(@NonNull String text){
        List<String> tickers = getListTickers(text);
        Timber.d("searchTickers list = %s", new Gson().toJson(tickers));
        //todo search
    }

    private void openActivityList(){
        //todo open
    }
}
