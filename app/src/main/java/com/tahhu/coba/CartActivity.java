package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private TextView totalPriceTextView;
    private Button btnCheckout;
    private CartAdapter cartAdapter;
    private List<Product> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Inisialisasi RecyclerView dan komponen lainnya
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        // Menyusun RecyclerView dengan data produk yang ada di keranjang
        cartItems = new ArrayList<>();

        // Simulasi produk yang ditambahkan ke keranjang (ganti dengan data nyata)
        cartItems.add(new Product("Lampu Stan", "Rp 50,000", R.drawable.product1, "4.5", "120 sold", "Description"));
        cartItems.add(new Product("Kipas Angin", "Rp 22,000", R.drawable.product2, "4.7", "12k sold", "Description"));
        cartItems.add(new Product("Sepatu Slim", "Rp 120,000", R.drawable.product4, "4.5", "12k sold", "Description"));
        cartItems.add(new Product("Sepatu RDS", "Rp 420,000", R.drawable.product3, "4.8", "6k sold", "Description"));
        cartItems.add(new Product("Lampu RS", "Rp 420,000", R.drawable.product1, "4.3", "134 sold", "Description"));
        cartItems.add(new Product("Kipas SSS", "Rp 420,000", R.drawable.product2, "4.7", "12k sold", "Description"));

        cartAdapter = new CartAdapter(this, cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        // Menampilkan total harga produk di keranjang
        updateTotalPrice();

        // Menangani event checkout
        btnCheckout.setOnClickListener(v -> {

            // Handle checkout logic here, misalnya pindah ke halaman pembayaran
            Intent intent = new Intent(CartActivity.this, Marketplace.class);
            startActivity(intent);
        });
    }

    // Menghitung total harga produk di keranjang
    private void updateTotalPrice() {
        int total = 0;
        for (Product product : cartItems) {
            total += Integer.parseInt(product.getPrice().replace("Rp ", "").replace(",", ""));
        }
        totalPriceTextView.setText("Total: Rp " + total);
    }
}
