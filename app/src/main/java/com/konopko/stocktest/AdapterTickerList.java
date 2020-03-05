package com.konopko.stocktest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.konopko.stocktest.app.App;
import com.konopko.stocktest.databinding.ListItemTickerBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class AdapterTickerList extends RecyclerView.Adapter<AdapterTickerList.TickerViewHolder> {

    private final PublishSubject<AdapterTickerListHolder> onClickSubject = PublishSubject.create();
    private List<AdapterTickerListHolder> list = new ArrayList<>();

    public AdapterTickerList(){}

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
            holder.itemView.setOnClickListener(view -> onClickSubject.onNext(item));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Observable<AdapterTickerListHolder> subscribeItemClick() {
        return onClickSubject;
    }

    public static class TickerViewHolder extends RecyclerView.ViewHolder {

        private ListItemTickerBinding binding;
        private Context context;

        public TickerViewHolder(@NonNull ListItemTickerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = binding.getRoot().getContext();
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
                    binding.setTickerCurrency(ticker.getCurrency() == null ? null : App.Companion.getInstance().getCurrencySign(ticker.getCurrency()));
                    binding.setTickerPrice(String.format(Locale.getDefault(), "%.2f", ticker.getCurrentValue()));
                    setChart(binding.chart, ticker);
                }
            }
            binding.executePendingBindings();
        }

        private void setChart(@NonNull LineChart chart, @NonNull Ticker ticker){
            chart.setTouchEnabled(false); // disable interactions
            chart.getLegend().setEnabled(false); // hide legend
            chart.setDescription(null);

            LineDataSet lineDataSet = new LineDataSet(FactoryMPAndroidChart.getChartDataSetStepOne(ticker), ticker.getId());
            lineDataSet.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
            lineDataSet.setLineWidth(2f);
            // hide circles, use dots
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setCircleColor(ContextCompat.getColor(context, R.color.colorPrimary));
            lineDataSet.setCircleRadius(1f);

            chart.getXAxis().setEnabled(false);
            chart.getAxisLeft().setEnabled(false);
            chart.getAxisRight().setEnabled(false);
            chart.setMaxVisibleValueCount(0); // hide labels of points

            LineData data = new LineData(lineDataSet);
            chart.setData(data);
            chart.invalidate();
        }
    }
}
