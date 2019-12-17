package com.konopko.stocktest;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.konopko.stocktest.databinding.ListItemTickerBinding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class AdapterTickerList extends RecyclerView.Adapter<AdapterTickerList.TickerViewHolder> {

    private final PublishSubject<Ticker> onClickSubject = PublishSubject.create();
    private List<AdapterTickerListHolder> list = new ArrayList<>();


    public void setList(@NonNull  List<AdapterTickerListHolder> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemTickerBinding binding = ListItemTickerBinding.inflate(inflater);
        return new TickerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TickerViewHolder holder, int position) {
        AdapterTickerListHolder item = list.get(position);
        if (item != null) {
            holder.bind(item);
            holder.itemView.setOnClickListener(view -> onClickSubject.onNext(item.getTicker()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TickerViewHolder extends RecyclerView.ViewHolder {

        private ListItemTickerBinding binding;

        public TickerViewHolder(ListItemTickerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AdapterTickerListHolder item) {
            binding.setTickerId(item.getTickerId());
            Ticker ticker = item.getTicker();
            binding.setInProgress(ticker == null);

            // ticker has been loaded
            if (ticker != null){
                Timber.d("TickerViewHolder bind ticker error = %s", ticker.getError());
                if (ticker.getError() != null)
                    binding.setTickerError(ticker.getError());
                else {
                    binding.setTickerCurrency(App.getCurrencySign(ticker.getCurrency()));
                    binding.setTickerPrice(String.format(Locale.getDefault(), "%.2f", ticker.getCurrentValue()));
                }
            }
            binding.executePendingBindings();
        }
    }
}
