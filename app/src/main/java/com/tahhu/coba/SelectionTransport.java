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

        // Set OnClickListener untuk "Mobil"
        optionMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan ke CarSelectionActivity dan kirimkan data yang diterima
                Intent intent = new Intent(SelectionTransport.this, CarSelectionActivity.class);
                intent.putExtra("address", addressFromPreviousActivity);
                intent.putExtra("destination", destinationFromPreviousActivity);
                startActivity(intent);
            }
        });

        // Set OnClickListener untuk "Motor"
        optionMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan ke BikeSelectionActivity dan kirimkan data yang diterima
                Intent intent = new Intent(SelectionTransport.this, BikeSelectionActivity.class);
                intent.putExtra("address", addressFromPreviousActivity);
                intent.putExtra("destination", destinationFromPreviousActivity);
                startActivity(intent);
            }
        });

        // Set OnClickListener untuk "Pickup"
        optionPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan ke PickupSelectionActivity dan kirimkan data yang diterima
                Intent intent = new Intent(SelectionTransport.this, KirimBarangActivity.class);
                intent.putExtra("address", addressFromPreviousActivity);
                intent.putExtra("destination", destinationFromPreviousActivity);
                startActivity(intent);
            }
        });

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
