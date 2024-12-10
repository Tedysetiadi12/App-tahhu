package com.tahhu.id;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartViewHolder> {

    private final List<CartProduct> cartProductList;  // Ganti List<Product> menjadi List<CartProduct>
    private final Runnable updateTotalsCallback;

    public CartProductAdapter(List<CartProduct> cartProductList, Runnable updateTotalsCallback) {
        this.cartProductList = cartProductList;
        this.updateTotalsCallback = updateTotalsCallback;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartProduct cartProduct = cartProductList.get(position);  // Ambil CartProduct, bukan Product
        Product product = cartProduct.getProduct();  // Ambil Product dari CartProduct

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productQuantity.setText(String.valueOf(cartProduct.getQuantity()));
        holder.productImage.setImageResource(product.getImageResource());

        holder.increaseQuantity.setOnClickListener(v -> {
            cartProduct.increaseQuantity();
            holder.productQuantity.setText(String.valueOf(cartProduct.getQuantity()));
            updateTotalsCallback.run();
        });

        holder.increaseQuantity.setOnClickListener(v -> {
            cartProduct.increaseQuantity();  // Menambah quantity pada CartProduct
            holder.productQuantity.setText(String.valueOf(cartProduct.getQuantity()));
            updateTotalsCallback.run();
        });

        holder.decreaseQuantity.setOnClickListener(v -> {
            if (cartProduct.getQuantity() > 1) {
                cartProduct.decreaseQuantity();  // Mengurangi quantity pada CartProduct
                holder.productQuantity.setText(String.valueOf(cartProduct.getQuantity()));
                updateTotalsCallback.run();
            } else {
                // If quantity is 1, remove the product from the cart
                cartProductList.remove(position);  // Remove the product from the list
                notifyItemRemoved(position);  // Notify the adapter that the item was removed
                updateTotalsCallback.run();  // Update the totals after removal
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartProductList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView increaseQuantity,productImage,decreaseQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.cartProductName);
            productPrice = itemView.findViewById(R.id.cartProductPrice);
            productQuantity = itemView.findViewById(R.id.cartProductQuantity);
            productImage = itemView.findViewById(R.id.cartProductImage);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
        }
    }
}

