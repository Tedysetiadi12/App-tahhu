package com.tahhu.coba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodItems;
    private final OnItemClickListener onItemClickListener;

    public FoodAdapter(List<FoodItem> foodItems, OnItemClickListener onItemClickListener) {
        this.foodItems = foodItems;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        FoodItem item = foodItems.get(position);
        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText("Rp " + item.getPrice());
        holder.cookingTimeTextView.setText("Time: " + item.getCookingTime() + " min");
        holder.foodImageView.setImageResource(item.getImageResId()); // Set image
        holder.orderButton.setOnClickListener(v -> onItemClickListener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public void updateData(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(FoodItem item);
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView cookingTimeTextView;
        ImageView foodImageView;
        Button orderButton;

        public FoodViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_food_name);
            priceTextView = itemView.findViewById(R.id.tv_food_price);
            cookingTimeTextView = itemView.findViewById(R.id.tv_food_time);
            foodImageView = itemView.findViewById(R.id.iv_food_image);
            orderButton = itemView.findViewById(R.id.btn_order);
        }
    }
}
