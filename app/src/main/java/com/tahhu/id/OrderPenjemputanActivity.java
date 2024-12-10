package com.tahhu.id;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

public class OrderPenjemputanActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText edtAlamatPenjemputan;
    private Button btnOrder;
    private Marker currentMarker;  // Marker untuk lokasi yang dipilih

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_penjemputan);

        // Tombol kembali
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Initialize views
        edtAlamatPenjemputan = findViewById(R.id.edtAlamatPenjemputan);
        btnOrder = findViewById(R.id.btnOrder);

        // Mendapatkan peta
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Set button click listener untuk tombol "Pesan Penjemputan"
        btnOrder.setOnClickListener(v -> {
            String alamat = edtAlamatPenjemputan.getText().toString().trim();

            if (alamat.isEmpty()) {
                Toast.makeText(OrderPenjemputanActivity.this, "Harap pilih alamat penjemputan", Toast.LENGTH_SHORT).show();
            } else {
                // Proses pemesanan penjemputan di sini
                Toast.makeText(OrderPenjemputanActivity.this, "Penjemputan berhasil dipesan", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        kebabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        // Menangani klik pada tombol kembali
        btnBack.setOnClickListener(v -> {
            // Menyelesaikan aktivitas saat tombol kembali diklik
            onBackPressed();
        });
    }

    // Metode untuk menampilkan PopupMenu
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(OrderPenjemputanActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(OrderPenjemputanActivity.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(OrderPenjemputanActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Menambahkan marker di lokasi default (misalnya Jakarta)
        LatLng defaultLocation = new LatLng(-6.2088, 106.8456);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));
        currentMarker = mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Lokasi Penjemputan"));

        // Menambahkan listener untuk drag marker
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDrag(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // Mendapatkan alamat setelah marker dipindahkan
                LatLng latLng = marker.getPosition();
                getAddressFromLatLng(latLng);  // Menampilkan alamat berdasarkan koordinat
            }
        });

        // Set default marker dapat di-drag
        currentMarker.setDraggable(true);
    }

    private void getAddressFromLatLng(LatLng latLng) {
        // Gunakan Geocoder atau API Geocoding untuk mendapatkan alamat dari latLng
        String address = "Alamat ditemukan";  // Simulasi alamat
        edtAlamatPenjemputan.setText(address);
    }
}
