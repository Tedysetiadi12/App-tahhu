package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class KeamananActivity extends AppCompatActivity {

    private CardView cardKeamanan, cardTamu, cardCCTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keamanan);

        // Initialize views
        initializeViews();

        // Set click listeners
        setClickListeners();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardKeamanan = findViewById(R.id.cardKeamanan);
        cardTamu = findViewById(R.id.cardTamu);
        cardCCTV = findViewById(R.id.cardCCTV);
    }

    private void setClickListeners() {
        cardKeamanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Forum Diskusi Activity
                Intent intent = new Intent(KeamananActivity.this, LayananKeamananActivity.class);
                startActivity(intent);
            }
        });

        cardTamu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Pengumuman Activity
                Intent intent = new Intent(KeamananActivity.this, KontrolTamuActivity.class);
                startActivity(intent);
            }
        });

        cardCCTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Kontak Penting Activity
                Intent intent = new Intent(KeamananActivity.this, StreamingCctvActivity.class);
                startActivity(intent);
            }
        });
    }

    // Optional: Add transition animation when opening new activities
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}