package com.tahhu.coba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

public class LokasiPenukaranActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ListView listViewLokasi;
    private Button btnTutup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi_penukaran);

        // Inisialisasi UI
        listViewLokasi = findViewById(R.id.listViewLokasi);
        btnTutup = findViewById(R.id.btnTutup);

        // Setup Map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Button close action
        btnTutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // Close the activity
            }
        });

        // Membuat data lokasi penukaran
        ArrayList<LokasiPenukaran> lokasiList = new ArrayList<>();
        lokasiList.add(new LokasiPenukaran("Lokasi 1", "Alamat Lokasi 1"));
        lokasiList.add(new LokasiPenukaran("Lokasi 2", "Alamat Lokasi 2"));
        lokasiList.add(new LokasiPenukaran("Lokasi 3", "Alamat Lokasi 3"));
        lokasiList.add(new LokasiPenukaran("Lokasi 4", "Alamat Lokasi 4"));

        // Membuat Adapter untuk ListView
        LokasiAdapter adapter = new LokasiAdapter(this, lokasiList);
        listViewLokasi.setAdapter(adapter);

        // Menambahkan klik listener pada item ListView
        listViewLokasi.setOnItemClickListener((parent, view, position, id) -> {
            LokasiPenukaran lokasi = lokasiList.get(position);
            Toast.makeText(LokasiPenukaranActivity.this,
                    "Lokasi: " + lokasi.getNamaLokasi() + "\nAlamat: " + lokasi.getAlamat(),
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set initial position (e.g., your city or a default location)
        LatLng defaultLocation = new LatLng(-6.200000, 106.816666);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));

        // Example: Adding a marker for a location
        mMap.addMarker(new MarkerOptions()
                .position(defaultLocation)
                .title("Lokasi Penukaran 1"));
    }
}
