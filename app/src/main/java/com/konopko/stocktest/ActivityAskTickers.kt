package com.konopko.stocktest

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.text.InputFilter.LengthFilter
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.konopko.stocktest.databinding.ActivityAskTickersBinding

class ActivityAskTickers : ActivityBase<ActivityAskTickersBinding>() {

    private val TAGS_MAX_LENGTH = 30
    private lateinit var viewModel: ViewModelAskTickers

    override fun getLayoutId(): Int {
        return R.layout.activity_ask_tickers
    }

    override fun getPageTitle(): String? {
        return getString(R.string.app_name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.handler = this

        binding.tagsEditText.maxLines = 2
        binding.tagsEditText.filters = arrayOf(
                InputFilter { cs, start, end, spanned, dStart, dEnd -> if (cs == " ") "" else cs },
                AllCaps(),  // set all caps
                LengthFilter(TAGS_MAX_LENGTH)
        )

        viewModel = ViewModelProvider(this, SavedStateViewModelFactory(application, this)).get(ViewModelAskTickers::class.java)

        viewModel.dataNext.observe(this, Observer {open: Boolean ->
                if (open)
                    openActivityList()
                else
                    Toast.makeText(this, R.string.tickers_list_error, Toast.LENGTH_SHORT).show()
        })

    }

    fun onClickNext(view: View) = viewModel.next(binding.tagsEditText.getTags())

    private fun openActivityList() {
        ActivityTickerList.open(this)
        clearTickers()
    }

    private fun clearTickers() {
        binding.tagsEditText.setTags("")
    }
}
