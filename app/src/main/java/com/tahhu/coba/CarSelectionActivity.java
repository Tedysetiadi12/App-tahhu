package com.tahhu.coba;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.widget.ImageView;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

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
        carList.add(new Car("Mercedes Bens", 29999,"4 Penumpang" ,R.drawable.ic_carnew));
        carList.add(new Car("Inova reborn", 49999, "4 Penumpang" ,R.drawable.ic_carnew));
        carList.add(new Car("Avanza", 34999, "4 Penumpang" ,R.drawable.ic_carnew));
        carList.add(new Car("Kijang", 29999,"4 Penumpang" ,R.drawable.ic_carnew));
        carList.add(new Car("Daihatsu", 49999, "4 Penumpang" ,R.drawable.ic_carnew));
        carList.add(new Car("Honda Supra", 34999, "4 Penumpang" ,R.drawable.ic_carnew));
        carList.add(new Car("Lamborgini", 29999,"4 Penumpang" ,R.drawable.ic_carnew));
        carList.add(new Car("Panther", 49999, "4 Penumpang" ,R.drawable.ic_carnew));
        carList.add(new Car("Pajero", 34999, "4 Penumpang" ,R.drawable.ic_carnew));

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCars);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Siapkan adapter dan kirim data ke adapter
        carAdapter = new CarAdapter(this, carList, address, destination, "Mobil");
        recyclerView.setAdapter(carAdapter);

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
                Toast.makeText(CarSelectionActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(CarSelectionActivity.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(CarSelectionActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }
}
