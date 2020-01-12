package com.konopko.stocktest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class ActivityBase<T : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: T
    protected abstract fun getLayoutId(): Int
    protected abstract fun getPageTitle(): String?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())

        supportActionBar?.title = getPageTitle()
    }
}