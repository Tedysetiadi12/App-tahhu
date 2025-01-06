package com.tahhu.id;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.tahhu.id.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class fiturWifiActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitur_wifi);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            loadFragment(new NetworkListFragment()); // Sementara hanya uji satu fragment
            return true;
        });

//        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Log.d("NavigationItem", "Selected item ID: " + item.getItemId());
//                Fragment selectedFragment = null;
//                switch (item.getItemId()) {
//                    case R.id.nav_networks:
//                        selectedFragment = new NetworkListFragment();
//                        break;
//                    case R.id.nav_stats:
//                        selectedFragment = new UsageStatsFragment();
//                        break;
//                    case R.id.nav_guests:
//                        selectedFragment = new GuestManagementFragment();
//                        break;
//                    case R.id.nav_settings:
//                        selectedFragment = new SettingsFragment();
//                        break;
//                }
//                if (selectedFragment != null) {
//                    loadFragment(selectedFragment);
//                }
//                return true;
//            }
//        });



        // Load default fragment
        loadFragment(new NetworkListFragment());
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}