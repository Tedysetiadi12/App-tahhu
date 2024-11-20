package com.tahhu.coba;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatActivity;

public class UserSettingActivity extends AppCompatActivity {

    private ImageView backButton, titleButton;
    private LinearLayout changePasswordLayout, editProfileLayout, addPaymentMethodLayout, pushNotificationLayout, aboutUsLayout, privacyPolicyLayout;
    private Switch notificationSwitch;  // Pastikan hanya ada satu deklarasi Switch

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        // Initialize views
        backButton = findViewById(R.id.back);
        titleButton = findViewById(R.id.btn_titiktiga);
        changePasswordLayout = findViewById(R.id.changePasswordLayout);
        editProfileLayout = findViewById(R.id.editProfileLayout);
        addPaymentMethodLayout = findViewById(R.id.addPaymentMethodLayout);
        pushNotificationLayout = findViewById(R.id.pushNotificationLayout);
        aboutUsLayout = findViewById(R.id.aboutUsLayout);
        privacyPolicyLayout = findViewById(R.id.privacyPolicyLayout);
        notificationSwitch = findViewById(R.id.notificationSwitch);  // Gunakan Switch yang benar

        // Set action for back button
        backButton.setOnClickListener(v -> finish());  // Close the current activity

        // Set action for menu button
        titleButton.setOnClickListener(v -> {
            // You can implement a menu popup here if necessary
            Toast.makeText(UserSettingActivity.this, "Menu Clicked", Toast.LENGTH_SHORT).show();
        });

        // Set action for change password
        changePasswordLayout.setOnClickListener(v -> {
            // Handle change password action
            Toast.makeText(UserSettingActivity.this, "Change Password Clicked", Toast.LENGTH_SHORT).show();
        });

        // Set action for edit profile
        editProfileLayout.setOnClickListener(v -> {
            // Handle edit profile action
            Toast.makeText(UserSettingActivity.this, "Edit Profile Clicked", Toast.LENGTH_SHORT).show();
        });

        // Set action for add payment method
        addPaymentMethodLayout.setOnClickListener(v -> {
            // Handle add payment method action
            Toast.makeText(UserSettingActivity.this, "Add Payment Method Clicked", Toast.LENGTH_SHORT).show();
        });

        // Set action for push notification toggle
        pushNotificationLayout.setOnClickListener(v -> {
            // Handle push notification toggle state change
            boolean isNotificationEnabled = notificationSwitch.isChecked();
            Toast.makeText(UserSettingActivity.this, "Push Notification " + (isNotificationEnabled ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
        });

        // Set action for notification switch
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the switch change here
                if (isChecked) {
                    // Switch is ON
                    Toast.makeText(UserSettingActivity.this, "Notifications Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is OFF
                    Toast.makeText(UserSettingActivity.this, "Notifications Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set action for about us
        aboutUsLayout.setOnClickListener(v -> {
            // Handle about us action
            Toast.makeText(UserSettingActivity.this, "About Us Clicked", Toast.LENGTH_SHORT).show();
        });

        // Set action for privacy policy
        privacyPolicyLayout.setOnClickListener(v -> {
            // Handle privacy policy action
            Toast.makeText(UserSettingActivity.this, "Privacy Policy Clicked", Toast.LENGTH_SHORT).show();
        });
    }
}
