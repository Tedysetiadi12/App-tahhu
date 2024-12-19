package com.tahhu.id;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionItemsAdapter extends RecyclerView.Adapter<TransactionItemsAdapter.ViewHolder> {
    private List<TransactionItem> items = new ArrayList<>();

    public void setItems(List<TransactionItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemQuantity, tvItemSubtotal;

        ViewHolder(View view) {
            super(view);
            tvItemName = view.findViewById(R.id.tvItemName);
            tvItemQuantity = view.findViewById(R.id.tvItemQuantity);
            tvItemSubtotal = view.findViewById(R.id.tvItemSubtotal);
        }

        void bind(TransactionItem item) {
            tvItemName.setText(item.getProductName());
            tvItemQuantity.setText(String.format(Locale.getDefault(),
                    "x%d", item.getQuantity()));
            tvItemSubtotal.setText(String.format(Locale.getDefault(),
                    "Rp %,.0f", item.getPrice()));
        }
    }
}
