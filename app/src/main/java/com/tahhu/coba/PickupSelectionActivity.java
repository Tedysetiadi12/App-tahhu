package com.tahhu.coba;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
        pickupList.add(new Pickup("Pickup Truck", 499.99, R.drawable.ic_tukar));
        pickupList.add(new Pickup("Van", 599.99, R.drawable.ic_tukar));
        pickupList.add(new Pickup("Delivery Pickup", 399.99, R.drawable.ic_tukar));

        // Set the adapter
        pickupAdapter = new PickupAdapter(this, pickupList, address, destination);
        recyclerViewPickupList.setAdapter(pickupAdapter);
    }
}
