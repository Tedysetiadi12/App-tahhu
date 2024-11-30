package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SelectionTransport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_transport);

        // Ambil data "address" dan "destination" dari Intent yang dikirimkan
        String addressFromPreviousActivity = getIntent().getStringExtra("address");
        String destinationFromPreviousActivity = getIntent().getStringExtra("destination");

        // Inisialisasi Button untuk opsi transportasi
        Button optionMobil = findViewById(R.id.optionMobil);
        Button optionMotor = findViewById(R.id.optionMotor);
        Button optionPickup = findViewById(R.id.optionPickup);

        // Set OnClickListener untuk semua Button
        View.OnClickListener transportClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (v.getId() == R.id.optionMobil) {
                    // Arahkan ke CarSelectionActivity
                    intent = new Intent(SelectionTransport.this, CarSelectionActivity.class);
                    intent.putExtra("selection_info", "Mobil");
                } else if (v.getId() == R.id.optionMotor) {
                    // Arahkan ke BikeSelectionActivity
                    intent = new Intent(SelectionTransport.this, BikeSelectionActivity.class);
                    intent.putExtra("selection_info", "Motor");
                } else if (v.getId() == R.id.optionPickup) {
                    // Arahkan ke PickupSelectionActivity
                    intent = new Intent(SelectionTransport.this, KirimBarangActivity.class);
                    intent.putExtra("selection_info", "Kirim Barang");
                } else {
                    return; // Jika Button tidak dikenali, keluar dari metode
                }

                // Tambahkan data yang diterima ke Intent
                intent.putExtra("address", addressFromPreviousActivity);
                intent.putExtra("destination", destinationFromPreviousActivity);
                // Mulai aktivitas yang sesuai
                startActivity(intent);
            }
        };

        // Tetapkan listener untuk semua tombol
        optionMobil.setOnClickListener(transportClickListener);
        optionMotor.setOnClickListener(transportClickListener);
        optionPickup.setOnClickListener(transportClickListener);

        // Inisialisasi btn_back
        ImageView btnBack = findViewById(R.id.btn_back);
        // Menangani klik pada tombol kembali
        btnBack.setOnClickListener(v -> {
            // Menyelesaikan aktivitas saat tombol kembali diklik
            onBackPressed();
        });

        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        kebabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(SelectionTransport.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(SelectionTransport.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(SelectionTransport.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }
}
