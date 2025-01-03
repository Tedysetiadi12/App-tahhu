package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class fiturKebersihanLingkunganActivity extends AppCompatActivity {
    private CardView cardJadwalsampah, cardLaporansampah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fitur_kebersihan_lingkungan);
        initializeViews();
        setClickListeners();
    }


    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardJadwalsampah = findViewById(R.id.cardJdwalsampah);
        cardLaporansampah = findViewById(R.id.cardlaporanSampah);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Menutup aktivitas
            }
        });
    }


    private void setClickListeners() {
        cardJadwalsampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Forum Diskusi Activity
                Intent intent = new Intent(fiturKebersihanLingkunganActivity.this, jadwalPengambilanSampahActivity.class);
                startActivity(intent);
            }
        });

        cardLaporansampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Pengumuman Activity
                Intent intent = new Intent(fiturKebersihanLingkunganActivity.this, pelaporanSampahLiarActivity.class);
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