package com.tahhu.coba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class LokasiPenukaranActivity extends AppCompatActivity {

    private ListView listViewLokasi;
    private Button btnTutup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi_penukaran);

        // Tombol kembali
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke halaman sebelumnya
            }
        });

        // Initialize views
        listViewLokasi = findViewById(R.id.listViewLokasi);
        btnTutup = findViewById(R.id.btnTutup);

        // Example data for locations (replace with real data from database or API)
        String[] lokasi = {"Lokasi 1", "Lokasi 2", "Lokasi 3"};

        // Set up ListView (you could use ArrayAdapter or a custom adapter for more complex views)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lokasi);
        listViewLokasi.setAdapter(adapter);

        // Button to close the activity
        btnTutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity and return to the previous one
            }
        });
    }
}
