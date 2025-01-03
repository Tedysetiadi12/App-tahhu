package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class AdministrasiActivity extends AppCompatActivity {

    private CardView cardIuran, cardKegiatan, cardKeluhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrasi);

        // Initialize views
        initializeViews();

        // Set click listeners
        setClickListeners();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardIuran = findViewById(R.id.cardIuran);
        cardKegiatan = findViewById(R.id.cardKegiatan);
        cardKeluhan = findViewById(R.id.cardKeluhan);
    }

    private void setClickListeners() {
        cardIuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Forum Diskusi Activity
                Intent intent = new Intent(AdministrasiActivity.this, PembayaranIuranActivity.class);
                startActivity(intent);
            }
        });

        cardKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Pengumuman Activity
                Intent intent = new Intent(AdministrasiActivity.this, PendaftaranKegiatanActivity.class);
                startActivity(intent);
            }
        });

        cardKeluhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Kontak Penting Activity
                Intent intent = new Intent(AdministrasiActivity.this, LayananKeluhanActivity.class);
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