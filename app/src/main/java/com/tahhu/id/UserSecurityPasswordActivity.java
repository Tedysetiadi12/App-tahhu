package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class UserSecurityPasswordActivity extends AppCompatActivity {

    private ImageView backButton;
    private CardView changePasswordCard, securityQuestionsCard;
    private Switch enable2faSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_security_password);

        // Initialize UI components
        backButton = findViewById(R.id.backButton);
        changePasswordCard = findViewById(R.id.changePasswordCard);
        enable2faSwitch = findViewById(R.id.enable2faSwitch);
        securityQuestionsCard = findViewById(R.id.securityQuestionsCard);

        // Back button action
        backButton.setOnClickListener(v -> finish());

        // Change Password navigation
        changePasswordCard.setOnClickListener(v -> {
            Intent intent = new Intent(UserSecurityPasswordActivity.this, EditSecurityPasswordActivity.class);
            startActivity(intent);
        });

        // Enable/Disable 2FA
        enable2faSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(UserSecurityPasswordActivity.this,
                        "Two-Factor Authentication Enabled",
                        Toast.LENGTH_SHORT).show();
                // Handle enabling 2FA (e.g., API call)
            } else {
                Toast.makeText(UserSecurityPasswordActivity.this,
                        "Two-Factor Authentication Disabled",
                        Toast.LENGTH_SHORT).show();
                // Handle disabling 2FA (e.g., API call)
            }
        });

        // Security Questions navigation
        securityQuestionsCard.setOnClickListener(v -> {
            Intent intent = new Intent(UserSecurityPasswordActivity.this, SecurityQuestionsActivity.class);
            startActivity(intent);
        });
    }
}
