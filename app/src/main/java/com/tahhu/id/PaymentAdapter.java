package com.tahhu.id;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private List<CartProduct> cartProductList;

    public PaymentAdapter(PaymentActivityMarketplace paymentActivityMarketplace, List<CartProduct> cartProductList) {
        this.cartProductList = cartProductList;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment_product, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        CartProduct cartProduct = cartProductList.get(position);
        Product product = cartProduct.getProduct();

        holder.productNameView.setText(product.getName());
        holder.quantityView.setText("Jumlah: " + cartProduct.getQuantity());
        holder.priceView.setText("" + product.getPrice());
        holder.imageView.setImageResource(product.getImageResource());
    }

    @Override
    public int getItemCount() {
        return cartProductList.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView productNameView, quantityView, priceView;
        ImageView imageView;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameView = itemView.findViewById(R.id.productNameView);
            quantityView = itemView.findViewById(R.id.quantityView);
            priceView = itemView.findViewById(R.id.priceView);
            imageView = itemView.findViewById(R.id.productImageView);
        }
    }
}
