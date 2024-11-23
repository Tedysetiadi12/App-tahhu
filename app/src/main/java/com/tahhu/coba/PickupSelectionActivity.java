package com.tahhu.coba;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

public class PickupSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPickupList;
    private PickupAdapter pickupAdapter;
    private ArrayList<Pickup> pickupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_selection);

        // Ambil data address dan destination dari Intent
        String address = getIntent().getStringExtra("address");
        String destination = getIntent().getStringExtra("destination");

        // Initialize the RecyclerView and list
        recyclerViewPickupList = findViewById(R.id.recyclerViewPickupList);
        recyclerViewPickupList.setLayoutManager(new LinearLayoutManager(this));

        // Populate the list of pickups (you can replace this with your actual data)
        pickupList = new ArrayList<>();
        pickupList.add(new Pickup("Pickup Truck", 49999, R.drawable.ic_tukar));
        pickupList.add(new Pickup("Van", 59999, R.drawable.ic_tukar));
        pickupList.add(new Pickup("Delivery Pickup", 39999, R.drawable.ic_tukar));

        // Set the adapter
        pickupAdapter = new PickupAdapter(this, pickupList, address, destination);
        recyclerViewPickupList.setAdapter(pickupAdapter);

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
                Toast.makeText(PickupSelectionActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(PickupSelectionActivity.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(PickupSelectionActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }
}
