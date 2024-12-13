package com.tahhu.id;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class DailySpendingAdapter extends RecyclerView.Adapter<DailySpendingAdapter.ViewHolder> {
    private List<ShoppingItem> items;

    public DailySpendingAdapter(List<ShoppingItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily_spending, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemName, tvAmount, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        public void bind(ShoppingItem item) {
            String displayText = item.getDate();
            tvDate.setText(displayText);
            tvItemName.setText(item.getName());
            double total = item.getPrice() * item.getQuantity();
            tvAmount.setText(String.format(Locale.getDefault(), "Rp. %.2f", total));
        }

    }
}
