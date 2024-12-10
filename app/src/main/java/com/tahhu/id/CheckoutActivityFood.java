package com.tahhu.id;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class CheckoutActivityFood extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckoutAdapterFood adapter;
    private TextView tvTotal;
    private List<FoodItem> cartItems;
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_food);

        recyclerView = findViewById(R.id.recyclerView_checkout);
        ImageView back = findViewById(R.id.imageViewBack);
        tvTotal = findViewById(R.id.tv_total);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = getIntent().getParcelableArrayListExtra("cartItems");
        calculateTotal();
        Intent intent2 = getIntent();
        List<FoodItem> cartItems = intent2.getParcelableArrayListExtra("cartItems");

        if (cartItems == null || cartItems.isEmpty()) {
            // Jika cartItems null atau kosong, tampilkan pesan atau tangani sesuai kebutuhan
            Toast.makeText(this, "Keranjang belanja kosong.", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter = new CheckoutAdapterFood(cartItems, new CheckoutAdapterFood.OnQuantityChangeListener() {
            @Override
            public void onQuantityChange() {
                calculateTotal();
            }
        });
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btn_payment).setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivityFood.this, PaymentActivityFood.class);
            intent.putParcelableArrayListExtra("finalItems", new ArrayList<>(cartItems));
            intent.putExtra("finalTotal", totalPrice);
            startActivity(intent);
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FoodActivity.class));
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

    private void calculateTotal() {
        totalPrice = 0;

        if (cartItems.isEmpty()) {
            tvTotal.setText("Your cart is empty");
            findViewById(R.id.btn_payment).setEnabled(false);
        } else {
            for (FoodItem item : cartItems) {
                totalPrice += item.getPrice() * item.getQuantity(); // Pertimbangkan quantity
            }
            tvTotal.setText("Total: Rp " + totalPrice);
            findViewById(R.id.btn_payment).setEnabled(true);
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(CheckoutActivityFood.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(CheckoutActivityFood.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(CheckoutActivityFood.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

}
