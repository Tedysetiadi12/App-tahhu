package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class KomunikasiActivity extends AppCompatActivity {

    private CardView cardForumDiskusi;
    private CardView cardPengumuman;
    private CardView cardKontak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komunikasi);

        // Initialize views
        initializeViews();

        // Set click listeners
        setClickListeners();
    }

    private void initializeViews() {
        cardForumDiskusi = findViewById(R.id.cardForumDiskusi);
        cardPengumuman = findViewById(R.id.cardPengumuman);
        cardKontak = findViewById(R.id.cardKontak);
    }

    private void setClickListeners() {
        cardForumDiskusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Forum Diskusi Activity
                Intent intent = new Intent(KomunikasiActivity.this, ForumDiskusiActivity.class);
                startActivity(intent);
            }
        });

        cardPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Pengumuman Activity
                Intent intent = new Intent(KomunikasiActivity.this, PengumumanActivity.class);
                startActivity(intent);
            }
        });

        cardKontak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Kontak Penting Activity
                Intent intent = new Intent(KomunikasiActivity.this, KontakPentingActivity.class);
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