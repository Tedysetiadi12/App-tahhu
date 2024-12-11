package com.tahhu.id;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;



public class DetailProductActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        ImageView btnback = findViewById(R.id.back_beranda);
        ImageView productImage = findViewById(R.id.productImage);
        ImageView userImage = findViewById(R.id.userImage);
//        ImageView btn_cart = findViewById(R.id.btn_cart);
//        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        TextView productName = findViewById(R.id.productName);
        TextView productPrice = findViewById(R.id.productPrice);
        TextView productRating = findViewById(R.id.productRating);
        TextView productSold = findViewById(R.id.productSold);
        TextView productDescription = findViewById(R.id.productDescription);  // Menambahkan TextView untuk deskripsi
        Button addToCartButton = findViewById(R.id.addToCartButton);
        // Ambil data dari Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productName.setText(extras.getString("productName"));
            productPrice.setText(extras.getString("productPrice"));
            productRating.setText(extras.getString("productRating"));
            productSold.setText(extras.getString("productSold"));
            productImage.setImageResource(extras.getInt("productImage"));
            userImage.setImageResource(extras.getInt("productImage"));
            productDescription.setText(extras.getString("productDescription"));  // Menampilkan deskripsi produk
        }

        addToCartButton.setOnClickListener(v -> {
            // Add product to cart
            Product product = new Product(
                    extras.getString("productName"),
                    extras.getString("productPrice"),
                    extras.getInt("productImage"),
                    extras.getString("productRating"),
                    extras.getString("productSold"),
                    extras.getString("productDescription")
            );
            startActivity(new Intent(getApplicationContext(),CartProductActivity.class));
            CartManager.getInstance().addProductToCart(product);
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke Marketplace
                Intent intent = new Intent(DetailProductActivity.this, Marketplace.class);
                startActivity(intent);
            }
        });

//        kebabIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupMenu(v);
//            }
//        });

        // Menangani event checkout
//        btn_cart.setOnClickListener(v -> {
//
//            // Handle checkout logic here, misalnya pindah ke halaman pembayaran
//            Intent intent = new Intent(DetailProductActivity.this, CartProductActivity.class);
//            startActivity(intent);
//        });

    }

    // Metode untuk menampilkan PopupMenu
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(DetailProductActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(DetailProductActivity.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(DetailProductActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }
}