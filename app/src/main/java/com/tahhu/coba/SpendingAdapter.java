package com.tahhu.coba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SpendingAdapter extends RecyclerView.Adapter<SpendingAdapter.SpendingViewHolder> {

    private ArrayList<SpendingItem> spendingItems = new ArrayList<>();

    public void setData(ArrayList<SpendingItem> items) {
        this.spendingItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SpendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spending, parent, false);
        return new SpendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpendingViewHolder holder, int position) {
        SpendingItem currentItem = spendingItems.get(position);
        holder.nameTextView.setText(currentItem.getName());
        holder.amountTextView.setText("Rp."+String.valueOf(currentItem.getAmount()));
    }

    @Override
    public int getItemCount() {
        return spendingItems.size();
    }

    public static class SpendingViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, amountTextView;

        public SpendingViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
        }
    }


}

