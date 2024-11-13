package com.tahhu.coba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Marketplace extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_marketplace);

        ImageView btnback = findViewById(R.id.back_beranda);
        RecyclerView productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Deskripsi produk (sama untuk semua produk)
        String productDescription = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

        List<Product> products = new ArrayList<>();
        products.add(new Product("Lampu Stan", "Rp 50,000", R.drawable.product1, "4.5", "120 sold", productDescription));
        products.add(new Product("Kipas Angin", "Rp 22,000", R.drawable.product2, "4.7", "12k sold", productDescription));
        products.add(new Product("Sepatu Slim", "Rp 120,000", R.drawable.product4, "4.5", "12k sold", productDescription));
        products.add(new Product("Sepatu RDS", "Rp 420,000", R.drawable.product3, "4.8", "6k sold", productDescription));
        products.add(new Product("Lampu RS", "Rp 420,000", R.drawable.product1, "4.3", "134 sold", productDescription));
        products.add(new Product("Kipas SSS", "Rp 420,000", R.drawable.product2, "4.7", "12k sold", productDescription));

        // Tambahkan lebih banyak produk jika diperlukan
        ProductAdapter adapter = new ProductAdapter(this, products);
        productRecyclerView.setAdapter(adapter);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(Marketplace.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        kebabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    // Metode untuk menampilkan PopupMenu
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(Marketplace.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(Marketplace.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(Marketplace.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }
}