package com.tahhu.coba;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditSecurityPasswordActivity extends AppCompatActivity {

    private EditText oldPassword, newPassword, confirmPassword;
    private Button savePasswordButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_security_password);

        // Initialize UI elements
        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        savePasswordButton = findViewById(R.id.savePasswordButton);
        backButton = findViewById(R.id.backButton);

        // Back button action
        backButton.setOnClickListener(v -> finish());

        // Save button action
        savePasswordButton.setOnClickListener(v -> {
            String oldPass = oldPassword.getText().toString().trim();
            String newPass = newPassword.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();

            // Validate input
            if (TextUtils.isEmpty(oldPass)) {
                oldPassword.setError("Enter your old password");
                return;
            }
            if (TextUtils.isEmpty(newPass)) {
                newPassword.setError("Enter a new password");
                return;
            }
            if (newPass.length() < 6) {
                newPassword.setError("Password must be at least 6 characters");
                return;
            }
            if (!newPass.equals(confirmPass)) {
                confirmPassword.setError("Passwords do not match");
                return;
            }

            // Handle password update logic here (e.g., send to server)
            Toast.makeText(EditSecurityPasswordActivity.this,
                    "Password successfully updated!",
                    Toast.LENGTH_SHORT).show();
            finish(); // Close activity after success
        });
    }
}
