package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 3000; // Durasi Splash Screen (ms)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                // User sudah login, arahkan ke MainActivity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // User belum login, arahkan ke LoginActivity
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish(); // Mengakhiri SplashActivity
        }, SPLASH_DISPLAY_LENGTH);
    }
}
