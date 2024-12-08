package com.tahhu.coba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CompletedTransactionAdapter extends RecyclerView.Adapter<CompletedTransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactionList;

    public CompletedTransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_completed_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        if (transaction != null) {
            holder.transactionIdView.setText("" + transaction.getTransactionId());

            // Periksa apakah productList null
            List<CartProduct> productList = transaction.getProductList();
            if (productList != null && !productList.isEmpty()) {
                StringBuilder productDetails = new StringBuilder();
                for (CartProduct product : productList) {
                    if (product != null) {
                        productDetails.append("Produk: ").append(product.getProductName())
                                .append(", Qty: ").append(product.getQuantity())
                                .append("\nHarga: ").append(product.getProductPrice())
                                .append("\n");

                        // Load gambar produk menggunakan Glide
                        Glide.with(holder.itemView.getContext())
                                .load(product.getProductImageUrl()) // Asumsikan ada metode untuk mendapatkan URL gambar
                                .placeholder(R.drawable.home) // Gambar placeholder
                                .into(holder.productImageView);
                    }
                }
                holder.productNameView.setText(productDetails.toString());
            } else {
                holder.productNameView.setText("No products available");
            }

            holder.totalPriceView.setText("Total: Rp " + String.format("%,.2f", transaction.getTotalPrice()));
            holder.paymentMethodView.setText("Payment: " + transaction.getPaymentMethod());
        }
    }


    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView transactionIdView, productNameView, totalPriceView, paymentMethodView;
        ImageView productImageView;


        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            transactionIdView = itemView.findViewById(R.id.transactionIdView);
            productNameView = itemView.findViewById(R.id.productNameView);
            totalPriceView = itemView.findViewById(R.id.totalPriceView);
            paymentMethodView = itemView.findViewById(R.id.paymentMethodView);
        }
    }
}

