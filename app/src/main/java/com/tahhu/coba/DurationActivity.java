package com.tahhu.coba;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DurationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration);

        // Tangkap SECURITY_TYPE dari Intent
        String securityType = getIntent().getStringExtra("SECURITY_TYPE");

        // Card Views
        CardView cardDaily = findViewById(R.id.card_daily);
        CardView cardMonthly = findViewById(R.id.card_monthly);
        CardView cardYearly = findViewById(R.id.card_yearly);

        // Detail TextViews
        TextView detailDaily = findViewById(R.id.detail_daily);
        TextView detailMonthly = findViewById(R.id.detail_monthly);
        TextView detailYearly = findViewById(R.id.detail_yearly);

        // Judul atau informasi tambahan
        TextView title = findViewById(R.id.tv_title);
        title.setText("Pilih Lama Waktu untuk " + securityType);

        // Tombol "Pesan Sekarang"
        Button btnPesanSekarang = findViewById(R.id.btn_pesan_sekarang);

        // Variabel untuk menyimpan durasi dan harga yang dipilih
        final String[] selectedDuration = new String[1];
        final String[] selectedPrice = new String[1];

        // Handle clicks
        cardDaily.setOnClickListener(v -> {
            toggleVisibility(detailDaily);
            detailMonthly.setVisibility(View.GONE);
            detailYearly.setVisibility(View.GONE);
            btnPesanSekarang.setVisibility(View.VISIBLE);
            selectedDuration[0] = "Harian";
            selectedPrice[0] = "Rp 100.000";
        });

        cardMonthly.setOnClickListener(v -> {
            toggleVisibility(detailMonthly);
            detailDaily.setVisibility(View.GONE);
            detailYearly.setVisibility(View.GONE);
            btnPesanSekarang.setVisibility(View.VISIBLE);
            selectedDuration[0] = "Bulanan";
            selectedPrice[0] = "Rp 500.000";
        });

        cardYearly.setOnClickListener(v -> {
            toggleVisibility(detailYearly);
            detailDaily.setVisibility(View.GONE);
            detailMonthly.setVisibility(View.GONE);
            btnPesanSekarang.setVisibility(View.VISIBLE);
            selectedDuration[0] = "Tahunan";
            selectedPrice[0] = "Rp 5.000.000";
        });

        // Handle "Pesan Sekarang" click
        btnPesanSekarang.setOnClickListener(v -> {
            Intent intent = new Intent(DurationActivity.this, PaymentActivitySecurity.class);
            intent.putExtra("SECURITY_TYPE", securityType);
            intent.putExtra("DURATION", selectedDuration[0]);
            intent.putExtra("PRICE", selectedPrice[0]);
            startActivity(intent);
        });
    }

    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
