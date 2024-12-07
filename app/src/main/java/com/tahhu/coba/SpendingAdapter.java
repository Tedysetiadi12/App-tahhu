package com.tahhu.coba;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SpendingAdapter extends RecyclerView.Adapter<SpendingAdapter.SpendingViewHolder> {

    private List<SpendingItem> spendingList;

    @Override
    public SpendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new SpendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpendingViewHolder holder, int position) {
        SpendingItem item = spendingList.get(position);
        holder.nameText.setText(item.getName());
        holder.amountText.setText("Amount: $" + item.getAmount());
    }

    @Override
    public int getItemCount() {
        return spendingList == null ? 0 : spendingList.size();
    }

    public void setData(List<SpendingItem> items) {
        spendingList = items;
        notifyDataSetChanged();
    }

    public static class SpendingViewHolder extends RecyclerView.ViewHolder {

        TextView nameText;
        TextView amountText;

        public SpendingViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(android.R.id.text1);
            amountText = itemView.findViewById(android.R.id.text2);
        }
    }
}
