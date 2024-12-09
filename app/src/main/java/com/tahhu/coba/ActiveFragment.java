package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

public class ActiveFragment extends Fragment {
    private RecyclerView recyclerViewActive;
    private TextView totalQuantityView, totalPriceView;
    private RadioGroup radioGroupShipping;
    private int shippingCost = 0;
    private List<CartProduct> activeProducts;
    private double totalPrice;
    private int totalQuantity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active, container, false);

        recyclerViewActive = view.findViewById(R.id.recyclerViewActive);
        totalQuantityView = view.findViewById(R.id.totalQuantityView);
        totalPriceView = view.findViewById(R.id.totalPriceView);
        radioGroupShipping = view.findViewById(R.id.radioGroupShipping);
        TextView ongkirPriceView = view.findViewById(R.id.ongkirPriceView);

        // Inisialisasi RecyclerView
        recyclerViewActive.setLayoutManager(new LinearLayoutManager(getContext()));
        activeProducts = CartManager.getInstance().getCartProducts();

        if (activeProducts != null && !activeProducts.isEmpty()) {
            // Tambahkan callback untuk memperbarui total
            Runnable updateTotalsCallback = this::updateTotals;

            // Set adapter dengan callback
            CartProductAdapter adapter = new CartProductAdapter(activeProducts, updateTotalsCallback);
            recyclerViewActive.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
        }

        // Menangani klik pada tombol pembayaran
        Button btnPayment = view.findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(v -> {
            if (radioGroupShipping.getCheckedRadioButtonId() == -1) {
                // Jika tidak ada ongkir yang dipilih, tampilkan pesan peringatan
                Toast.makeText(getContext(), "Silakan pilih ongkos kirim terlebih dahulu!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getContext(), PaymentActivityMarketplace.class);
                // Kirim data totalPrice dan shippingCost ke PaymentActivity
                intent.putExtra("totalPrice", totalPrice);
                intent.putExtra("shippingCost", shippingCost);
                intent.putExtra("cartProductList", (Serializable) activeProducts);

                startActivity(intent);
            }
        });

        // Mengatur listener untuk perubahan pilihan ongkir
        radioGroupShipping.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_standard_shipping) {
                shippingCost = 10000;
            } else if (checkedId == R.id.rb_express_shipping) {
                shippingCost = 20000;
            } else if (checkedId == R.id.rb_nextday_shipping) {
                shippingCost = 30000;
            }

            ongkirPriceView.setText("Rp " + String.format("%,.2f", (double) shippingCost));

            // Memperbarui total harga setelah memilih ongkir
            updateTotals();
        });

        updateTotals();
        return view;
    }

    private void updateTotals() {
        totalQuantity = 0;
        totalPrice = 0.0;

        // Iterasi melalui activeProducts untuk menghitung total quantity dan total price
        for (CartProduct cartProduct : activeProducts) {
            Product product = cartProduct.getProduct();
            totalQuantity += cartProduct.getQuantity();

            // Menghapus "Rp" dan koma sebelum konversi harga
            String priceString = product.getPrice().replace("Rp", "").replace(",", "").trim();
            double price = Double.parseDouble(priceString);

            totalPrice += cartProduct.getQuantity() * price;
        }

        // Menambahkan biaya ongkir ke total harga
        totalPrice += shippingCost;

        // Memperbarui tampilan total quantity dan total price
        totalQuantityView.setText(String.valueOf(totalQuantity));
        totalPriceView.setText("Rp " + String.format("%,.2f", totalPrice)); // Format harga agar lebih rapi
    }
}
