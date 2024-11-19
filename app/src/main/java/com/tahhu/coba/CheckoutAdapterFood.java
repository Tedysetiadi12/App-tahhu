package com.tahhu.coba;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CheckoutAdapterFood extends RecyclerView.Adapter<CheckoutAdapterFood.CheckoutViewHolder> {

    private List<FoodItem> foodItems;
    private final OnQuantityChangeListener onQuantityChangeListener;

    public CheckoutAdapterFood(List<FoodItem> foodItems, OnQuantityChangeListener onQuantityChangeListener) {
        this.foodItems = foodItems;
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout_food, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CheckoutViewHolder holder, int position) {
        FoodItem item = foodItems.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText("Rp " + item.getPrice());
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));

        holder.plusButton.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            onQuantityChangeListener.onQuantityChange();
        });

        holder.minusButton.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
                onQuantityChangeListener.onQuantityChange();
            } else {
                foodItems.remove(position);
                notifyItemRemoved(position);
                onQuantityChangeListener.onQuantityChange();
            }
        });
    }



    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange();
    }

    public static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        Button plusButton;
        Button minusButton;

        public CheckoutViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_checkout_image);
            nameTextView = itemView.findViewById(R.id.tv_checkout_name);
            priceTextView = itemView.findViewById(R.id.tv_checkout_price);
            quantityTextView = itemView.findViewById(R.id.tv_checkout_quantity);
            plusButton = itemView.findViewById(R.id.btn_plus);
            minusButton = itemView.findViewById(R.id.btn_minus);
        }
    }
}
