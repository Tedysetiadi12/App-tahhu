package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class SosialKomunitasActivity extends AppCompatActivity {

    private CardView cardAgenda, cardMarketplace, cardVolunteer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosial_komunitas);

        // Initialize views
        initializeViews();

        // Set click listeners
        setClickListeners();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardAgenda = findViewById(R.id.cardAgenda);
        cardMarketplace = findViewById(R.id.cardMarketplace);
        cardVolunteer = findViewById(R.id.cardVolunteer);
    }

    private void setClickListeners() {
        cardAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Forum Diskusi Activity
                Intent intent = new Intent(SosialKomunitasActivity.this, AgendaWargaActivity.class);
                startActivity(intent);
            }
        });

        cardMarketplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Pengumuman Activity
                Intent intent = new Intent(SosialKomunitasActivity.this, MarketplaceWargasActivity.class);
                startActivity(intent);
            }
        });

        cardVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Kontak Penting Activity
                Intent intent = new Intent(SosialKomunitasActivity.this, LayananVolunteerActivity.class);
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