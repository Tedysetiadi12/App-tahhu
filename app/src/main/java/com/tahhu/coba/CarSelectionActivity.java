package com.tahhu.coba;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CarSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private List<Car> carList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_selection);

        // Ambil data address dan destination dari Intent
        String address = getIntent().getStringExtra("address");
        String destination = getIntent().getStringExtra("destination");

        // Populate the list of bikes (you can replace this with your actual data)
        carList = new ArrayList<>();
        carList.add(new Car("Mercedes Bens", 299.99, R.drawable.ic_cars));
        carList.add(new Car("Inova reborn", 499.99, R.drawable.ic_cars));
        carList.add(new Car("Avanza", 349.99, R.drawable.ic_cars));

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCars);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Siapkan adapter dan kirim data ke adapter
        carAdapter = new CarAdapter(this, carList, address, destination);
        recyclerView.setAdapter(carAdapter);
    }
}
