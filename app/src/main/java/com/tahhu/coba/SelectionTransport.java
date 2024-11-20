package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                Intent intent = new Intent(SelectionTransport.this, PickupSelectionActivity.class);
                intent.putExtra("address", addressFromPreviousActivity);
                intent.putExtra("destination", destinationFromPreviousActivity);
                startActivity(intent);
            }
        });
    }
}
