package com.tahhu.coba;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

public class UserAboutUsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final LatLng OFFICE_LOCATION = new LatLng(-7.8089875, 110.3762969);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_about_us);

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        // Setup click listeners
        setupClickListeners();
    }

    private void setupClickListeners() {
        // Get Directions button
        MaterialButton btnGetDirections = findViewById(R.id.btnGetDirections);
        btnGetDirections.setOnClickListener(v -> openGoogleMaps());

        // Email clicks
        TextView tvPersonalEmail = findViewById(R.id.tvPersonalEmail);
        TextView tvInfoEmail = findViewById(R.id.tvInfoEmail);
        TextView tvCeoEmail = findViewById(R.id.tvCeoEmail);

        tvPersonalEmail.setOnClickListener(v -> openEmail(tvPersonalEmail.getText().toString()));
        tvInfoEmail.setOnClickListener(v -> openEmail(tvInfoEmail.getText().toString()));
        tvCeoEmail.setOnClickListener(v -> openEmail(tvCeoEmail.getText().toString()));

        // Website TextView
        TextView tvWebsite = findViewById(R.id.tvWebsite);
        tvWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite("https://www.tahhu.com");
            }
        });

        tvWebsite.setOnClickListener(v -> openWebsite(tvWebsite.getText().toString()));

        // Telegram click
        TextView tvTelegram = findViewById(R.id.tvTelegram);
        tvTelegram.setOnClickListener(v -> openTelegram(tvTelegram.getText().toString()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add marker for office location
        mMap.addMarker(new MarkerOptions()
                .position(OFFICE_LOCATION)
                .title("PT. Tahhu Teknologi Indonesia"));

        // Move camera to office location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(OFFICE_LOCATION, 15f));
    }

    private void openGoogleMaps() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + OFFICE_LOCATION.latitude +
                "," + OFFICE_LOCATION.longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps app is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void openEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:info@tahhu.com" + email));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openWebsite(String website) {
        Uri webpage = Uri.parse(website); // Proper URL
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openTelegram(String username) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("tg://resolve?domain=" + username.replace("@", "")));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Telegram not installed, open in browser instead
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://t.me/useone007" + username.replace("@", "")));
            startActivity(intent);
        }
    }
}
