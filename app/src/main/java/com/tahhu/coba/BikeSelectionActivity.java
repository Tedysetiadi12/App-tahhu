package com.tahhu.coba;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

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
        bikeList.add(new Bike("Vario 150", 299.99, R.drawable.ic_motors));
        bikeList.add(new Bike("Honda supra", 499.99, R.drawable.ic_motors));
        bikeList.add(new Bike("PCX 150", 349.99, R.drawable.ic_motors));

        // Set the adapter
        bikeAdapter = new BikeAdapter(this, bikeList, address, destination);
        recyclerViewBikeList.setAdapter(bikeAdapter);
    }
}
