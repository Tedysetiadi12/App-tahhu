package com.tahhu.id;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private DatabaseReference locationRef;
    private ValueEventListener locationListener;
    private String bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        bookingId = getIntent().getStringExtra("bookingId");
        locationRef = FirebaseDatabase.getInstance().getReference("security_locations").child(bookingId);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                    double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                    LatLng location = new LatLng(latitude, longitude);

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(location).title("Security Guard"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        };

        locationRef.addValueEventListener(locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationRef != null && locationListener != null) {
            locationRef.removeEventListener(locationListener);
        }
    }
}

