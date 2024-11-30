package com.tahhu.coba;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class BikeSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBikeList;
    private BikeAdapter bikeAdapter;
    private ArrayList<Bike> bikeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_selection);

        // Ambil data address dan destination dari Intent
        String address = getIntent().getStringExtra("address");
        String destination = getIntent().getStringExtra("destination");

        // Initialize the RecyclerView and list
        recyclerViewBikeList = findViewById(R.id.recyclerViewBikeList);
        recyclerViewBikeList.setLayoutManager(new LinearLayoutManager(this));

        // Populate the list of bikes (you can replace this with your actual data)
        bikeList = new ArrayList<>();
        bikeList.add(new Bike("Vario 150", 29999, "2 Penumpang", R.drawable.ic_motor1));
        bikeList.add(new Bike("Honda supra", 49999,"2 Penumpang",  R.drawable.ic_motor1));
        bikeList.add(new Bike("KLX 150", 34999, "2 Penumpang", R.drawable.ic_motor1));
        bikeList.add(new Bike("Mio Sport", 29999, "2 Penumpang", R.drawable.ic_motor1));
        bikeList.add(new Bike("NMax 150", 49999,"2 Penumpang",  R.drawable.ic_motor1));
        bikeList.add(new Bike("PCX 150", 34999, "2 Penumpang", R.drawable.ic_motor1));

        // Set the adapter
        bikeAdapter = new BikeAdapter(this, bikeList, address, destination, "Motor");
        recyclerViewBikeList.setAdapter(bikeAdapter);

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
                Toast.makeText(BikeSelectionActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(BikeSelectionActivity.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(BikeSelectionActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }
}
