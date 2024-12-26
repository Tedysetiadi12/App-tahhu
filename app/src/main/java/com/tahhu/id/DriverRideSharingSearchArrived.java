package com.tahhu.id;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

public class DriverRideSharingSearchArrived extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker driverMarker;
    private ImageView imgDriver;
    private TextView txtDriverName, txtVehicleInfo, txtLicensePlate, txtStatus;
    private MaterialButton btnCancel, btnChat, btnCall;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_ride_sharing_search_arrived);

        initializeViews();
        setupMapFragment();
        setupClickListeners();
        loadDriverInfo();

        // Show dialog after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDriverArrivedDialog();
            }
        }, 5000); // 5000 milliseconds = 5 seconds
    }

    private void showDriverArrivedDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_driver_ridesharing_arrived);
        dialog.setCancelable(false);

        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Find the OK button
        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // Navigate to next activity
                Intent intent = new Intent(DriverRideSharingSearchArrived.this, DialogRatingRideSharing.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();
    }

    private void initializeViews() {
        imgDriver = findViewById(R.id.imgDriver);
        txtDriverName = findViewById(R.id.txtDriverName);
        txtVehicleInfo = findViewById(R.id.txtVehicleInfo);
        txtLicensePlate = findViewById(R.id.txtLicensePlate);
        txtStatus = findViewById(R.id.txtStatus);
        btnCancel = findViewById(R.id.btnCancel);
        btnChat = findViewById(R.id.btnChat);
        btnCall = findViewById(R.id.btnCall);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e("MAP", "Error: Map Fragment is null");
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnCancel.setOnClickListener(v -> {
            // Show confirmation dialog
            new AlertDialog.Builder(this)
                    .setTitle("Cancel Order")
                    .setMessage("Are you sure you want to cancel this order?")
                    .setPositiveButton("Yes", (dialog, which) -> cancelOrder())
                    .setNegativeButton("No", null)
                    .show();
        });

        btnChat.setOnClickListener(v -> {
            // Open chat activity
            Intent intent = new Intent(this, ChatDriverFood.class);
            intent.putExtra("driver_id", getDriverId());
            startActivity(intent);
        });

        btnCall.setOnClickListener(v -> {
            // Make phone call
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + getDriverPhone()));
            startActivity(intent);
        });
    }

    private void loadDriverInfo() {
        // Load driver image (using Glide or similar library)
        Glide.with(this)
                .load(getDriverImageUrl())
                .placeholder(R.drawable.ic_user1)
                .into(imgDriver);

        // Set driver info
        txtDriverName.setText("Rayford Chenail");
        txtVehicleInfo.setText("Yamaha MX King");
        txtLicensePlate.setText("HSW 4736 XX");
        txtStatus.setText("Driver is heading to the restaurant...");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        // Set initial camera position
        LatLng initialPosition = new LatLng(-6.2088, 106.8456); // Example coordinates
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 15f));

        // Add driver marker
        driverMarker = mMap.addMarker(new MarkerOptions()
                .position(initialPosition)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.user)));

        // Start location updates
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        // Simulate driver movement (in real app, this would come from backend)
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDriverLocation();
                handler.postDelayed(this, 3000); // Update every 3 seconds
            }
        }, 3000);
    }

    private void updateDriverLocation() {
        // In real app, get location from backend
        // This is just for demo
        if (driverMarker != null) {
            LatLng newPosition = getNextDriverPosition();
            driverMarker.setPosition(newPosition);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(newPosition));
        }
    }

    // Helper methods (implement these based on your backend)
    private String getDriverId() {
        return "driver_123"; // Example
    }

    private String getDriverPhone() {
        return "+1234567890"; // Example
    }

    private String getDriverImageUrl() {
        return "https://example.com/driver.jpg"; // Example
    }

    private LatLng getNextDriverPosition() {
        // In real app, get from backend
        return new LatLng(-6.2088, 106.8456); // Example
    }

    private void cancelOrder() {
        // Implement order cancellation logic
        Toast.makeText(this, "Order cancelled", Toast.LENGTH_SHORT).show();
        finish();
    }
}