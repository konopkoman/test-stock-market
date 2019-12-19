package com.konopko.stocktest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class ActivityBase<T extends ViewDataBinding> extends AppCompatActivity {

    protected T binding;
    protected abstract int getLayoutId();
    protected abstract String getPageTitle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getPageTitle());
    }
}
