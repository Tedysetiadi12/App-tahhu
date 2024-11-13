package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailProductActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        ImageView btnback = findViewById(R.id.back_beranda);
        ImageView productImage = findViewById(R.id.productImage);
        TextView productName = findViewById(R.id.productName);
        TextView productPrice = findViewById(R.id.productPrice);
        TextView productRating = findViewById(R.id.productRating);
        TextView productSold = findViewById(R.id.productSold);


        // Ambil data dari Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productName.setText(extras.getString("productName"));
            productPrice.setText(extras.getString("productPrice"));
            productRating.setText(extras.getString("productRating"));
            productSold.setText(extras.getString("productSold"));
            productImage.setImageResource(extras.getInt("productImage"));
        }

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(DetailProductActivity.this, Marketplace.class);
                startActivity(intent);
            }
        });
    }
}