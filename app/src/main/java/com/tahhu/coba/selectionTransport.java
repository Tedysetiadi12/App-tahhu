package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class selectionTransport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_transport);

        // Ambil data "address" dan "destination" dari Intent yang dikirimkan
        String addressFromPreviousActivity = getIntent().getStringExtra("address");
        String destinationFromPreviousActivity = getIntent().getStringExtra("destination");

        // Inisialisasi LinearLayout untuk opsi mobil
        View optionMobil = findViewById(R.id.optionMobil);
        View optionMotor = findViewById(R.id.optionMotor);
        View optionPickup = findViewById(R.id.optionPickup);

        // Set OnClickListener untuk "Mobil"
        optionMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan ke CarSelectionActivity dan kirimkan data yang diterima
                Intent intent = new Intent(selectionTransport.this, CarSelectionActivity.class);
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
                Intent intent = new Intent(selectionTransport.this, BikeSelectionActivity.class);
                intent.putExtra("address", addressFromPreviousActivity);
                intent.putExtra("destination", destinationFromPreviousActivity);
                startActivity(intent);
            }
        });

        // Set OnClickListener untuk "Pickup"
        optionPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan ke BikeSelectionActivity dan kirimkan data yang diterima
                Intent intent = new Intent(selectionTransport.this, PickupSelectionActivity.class);
                intent.putExtra("address", addressFromPreviousActivity);
                intent.putExtra("destination", destinationFromPreviousActivity);
                startActivity(intent);
        }
        });
    }
}
