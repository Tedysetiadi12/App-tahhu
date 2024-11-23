package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UserPrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_privacy_policy);

        // Initialize views
        ImageView backButton = findViewById(R.id.backButton);
        TextView privacyPolicyContent = findViewById(R.id.privacyPolicyContent);
        Button acceptButton = findViewById(R.id.acceptButton);
        Button declineButton = findViewById(R.id.declineButton);

        // Set privacy policy content
        privacyPolicyContent.setText(getString(R.string.privacy_policy_text)); // Ensure you add privacy_policy_text in strings.xml

        // Back button functionality
        backButton.setOnClickListener(view -> finish());

        // Accept button functionality
        acceptButton.setOnClickListener(view -> {
            // Add logic for when the user accepts the privacy policy
            Toast.makeText(UserPrivacyPolicyActivity.this, "Privacy policy accepted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserPrivacyPolicyActivity.this, MainActivity.class); // Replace with target activity
            startActivity(intent);
        });

        // Decline button functionality
        declineButton.setOnClickListener(view -> {
            // Add logic for when the user declines the privacy policy
            Toast.makeText(UserPrivacyPolicyActivity.this, "Privacy policy declined", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
