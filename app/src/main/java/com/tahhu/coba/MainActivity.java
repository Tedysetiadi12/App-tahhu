package com.tahhu.coba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public Button btninternet;
    public ImageView btn_radio,btn_market, btn_finence, btn_ride, btn_survey, btn_uco ,menu_market, menu_finance, menu_ride;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize the ImageView

        btninternet = findViewById(R.id.btn_internet);
        btn_finence = findViewById(R.id.financeIcon);
        btn_market = findViewById(R.id.marketIcon);
        btn_ride = findViewById(R.id.rideIcon);
        btn_survey = findViewById(R.id.surveyIcon);
        btn_uco = findViewById(R.id.ucoIcon);
        menu_market = findViewById(R.id.menumarket);
        menu_finance = findViewById(R.id.menufavorite);
        menu_ride = findViewById(R.id.menuride);
        btn_radio = findViewById(R.id.streaming);

        RecyclerView productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<Product> products = new ArrayList<>();
        products.add(new Product("Lampu Stan", "Rp 50,000", R.drawable.product1, "4.5", "120 sold"));
        products.add(new Product("Kipas Angin", "Rp 22,000", R.drawable.product2, "4.7", "12k sold"));
        products.add(new Product("Sepatu Slim", "Rp 120,000", R.drawable.product4, "4.5", "12k sold"));
        products.add(new Product("Sepatu RDS", "Rp 420,000", R.drawable.product3, "4.8", "6k sold"));
        products.add(new Product("Lampu RS", "Rp 420,000", R.drawable.product1, "4.3", "134 sold"));
        products.add(new Product("kipas SSS", "Rp 420,000", R.drawable.product2, "4.7", "12k sold"));
        // Add more products as needed

        ProductAdapter adapter = new ProductAdapter(this, products);
        productRecyclerView.setAdapter(adapter);
        // Set an OnClickListener
        btninternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://11.15.0.1";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        btn_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, RadioStreamin.class);
                startActivity(intent);
            }
        });
        btn_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, Marketpalce.class);
                startActivity(intent);
            }
        });
        btn_finence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, finance.class);
                startActivity(intent);
            }
        });
        btn_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, rideSharing.class);
                startActivity(intent);
            }
        });
        btn_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, Survey.class);
                startActivity(intent);
            }
        });
        btn_uco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, UcoActivity.class);
                startActivity(intent);
            }
        });
        menu_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, Marketpalce.class);
                startActivity(intent);
            }
        });
        menu_finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, finance.class);
                startActivity(intent);
            }
        });
        menu_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, rideSharing.class);
                startActivity(intent);
            }
        });

    }
}