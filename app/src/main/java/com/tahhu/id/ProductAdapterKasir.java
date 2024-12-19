package com.tahhu.id;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapterKasir extends RecyclerView.Adapter<ProductAdapterKasir.ProductViewHolder> {
    private List<ProductKasir> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onAddToCartClick(ProductKasir product);
    }

    public ProductAdapterKasir(List<ProductKasir> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_kasir, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductKasir product = products.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvProductStock;
        Button btnAddToCart;

        ProductViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductStock = itemView.findViewById(R.id.tvProductStock);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }

        void bind(final ProductKasir product, final OnProductClickListener listener) {
            tvProductName.setText(product.getName());
            tvProductPrice.setText(String.format("Rp %.0f", product.getPrice()));
            tvProductStock.setText(String.format("Stock: %d", product.getStock()));
            btnAddToCart.setEnabled(product.getStock() > 0);
            btnAddToCart.setOnClickListener(v -> listener.onAddToCartClick(product));
        }
    }
}

