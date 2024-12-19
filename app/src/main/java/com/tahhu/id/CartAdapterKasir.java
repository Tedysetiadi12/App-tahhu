package com.tahhu.id;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapterKasir extends RecyclerView.Adapter<CartAdapterKasir.CartViewHolder> {
    private List<CartItemKasir> cartItems;
    private OnCartItemChangeListener listener;

    public interface OnCartItemChangeListener {
        void onQuantityChange(CartItemKasir item, int newQuantity);
        void onRemoveFromCart(CartItemKasir item);
    }

    public CartAdapterKasir(List<CartItemKasir> cartItems, OnCartItemChangeListener listener) {
        this.cartItems = cartItems != null ? cartItems : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_kasir, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (position < cartItems.size()) {
            CartItemKasir item = cartItems.get(position);
            holder.bind(item, listener);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public void updateCartItems(List<CartItemKasir> newItems) {
        this.cartItems = new ArrayList<>(newItems);
        notifyDataSetChanged();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvSubtotal, tvQuantity;
        Button btnRemove, btnIncrease, btnDecrease;

        CartViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvCartProductName);
            tvProductPrice = itemView.findViewById(R.id.tvCartProductPrice);
            tvSubtotal = itemView.findViewById(R.id.tvCartSubtotal);
            tvQuantity = itemView.findViewById(R.id.tvCartQuantity);
            btnRemove = itemView.findViewById(R.id.btnRemoveFromCart);
            btnIncrease = itemView.findViewById(R.id.btnIncreaseQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecreaseQuantity);
        }

        void bind(final CartItemKasir item, final OnCartItemChangeListener listener) {
            if (item == null || item.getProduct() == null) return;

            try {
                tvProductName.setText(item.getProduct().getName());
                tvProductPrice.setText(String.format(Locale.getDefault(), "Rp %.0f", item.getProduct().getPrice()));
                tvQuantity.setText(String.valueOf(item.getQuantity()));
                updateSubtotal(item);

                btnIncrease.setOnClickListener(v -> {
                    try {
                        int newQuantity = item.getQuantity() + 1;
                        if (newQuantity <= item.getProduct().getStock()) {
                            listener.onQuantityChange(item, newQuantity);
                            tvQuantity.setText(String.valueOf(newQuantity));
                            updateSubtotal(item);
                        } else {
                            Toast.makeText(itemView.getContext(), "Stok tidak mencukupi", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("CartAdapterKasir", "Error increasing quantity: " + e.getMessage());
                    }
                });

                btnDecrease.setOnClickListener(v -> {
                    try {
                        int newQuantity = item.getQuantity() - 1;
                        if (newQuantity > 0) {
                            listener.onQuantityChange(item, newQuantity);
                            tvQuantity.setText(String.valueOf(newQuantity));
                            updateSubtotal(item);
                        } else {
                            Toast.makeText(itemView.getContext(), "Jumlah minimal 1", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("CartAdapterKasir", "Error decreasing quantity: " + e.getMessage());
                    }
                });

                btnRemove.setOnClickListener(v -> {
                    try {
                        listener.onRemoveFromCart(item);
                    } catch (Exception e) {
                        Log.e("CartAdapterKasir", "Error removing item: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.e("CartAdapterKasir", "Error binding view holder: " + e.getMessage());
            }
        }

        private void updateSubtotal(CartItemKasir item) {
            if (item != null) {
                tvSubtotal.setText(String.format(Locale.getDefault(), "Rp %.0f", item.getSubtotal()));
            }
        }
    }
}

