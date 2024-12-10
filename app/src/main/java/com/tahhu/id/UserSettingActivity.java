package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class UserSettingActivity extends AppCompatActivity {

    private ImageView backButton, titleButton;
    private LinearLayout changePasswordLayout, editProfileLayout, addPaymentMethodLayout, pushNotificationLayout, aboutUsLayout,catatalauout, privacyPolicyLayout;
    private Switch notificationSwitch;
    private ActivityResultLauncher<Intent> editProfileLauncher;

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
        catatalauout = findViewById(R.id.catatanLayout);
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
            // Navigate to UserSettingPasswordSecurityActivity first
            Intent intent = new Intent(UserSettingActivity.this, UserSecurityPasswordActivity.class);
            startActivity(intent);
        });

        // Inisialisasi ActivityResultLauncher
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // Mendapatkan data yang sudah diperbarui
                            String updatedName = data.getStringExtra("updatedName");
                            String updatedLastName = data.getStringExtra("updatedLastName");
                            String updatedEmail = data.getStringExtra("updatedEmail");

                            // Memperbarui UI dengan data baru
                            TextView nameTextView = findViewById(R.id.nameTextView);
                            TextView emailTextView = findViewById(R.id.email);
                            nameTextView.setText(updatedName + " " + updatedLastName);
                            emailTextView.setText(updatedEmail);
                        }
                    }
                }
        );

        // Listener untuk Edit Profile
        findViewById(R.id.editProfileLayout).setOnClickListener(v -> {
            // Membuat Intent untuk EditProfileActivity
            Intent intent = new Intent(UserSettingActivity.this, EditProfileActivity.class);
            intent.putExtra("name", "John");
            intent.putExtra("lastname", "Doe");
            intent.putExtra("email", "john.doe@example.com");

            // Meluncurkan Activity menggunakan ActivityResultLauncher
            if (editProfileLauncher != null) {
                editProfileLauncher.launch(intent);
            } else {
                Log.e("UserSettingActivity", "editProfileLauncher is null");
            }
        });


        // Set action for add payment method
        addPaymentMethodLayout.setOnClickListener(v -> {
            // Handle add payment method action
            Intent intent = new Intent(UserSettingActivity.this, UserAddPaymentMethodActivity.class);
            startActivity(intent);
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
            Intent intent = new Intent(UserSettingActivity.this, UserAboutUsActivity.class);
            startActivity(intent);
        }); // Set action for about us
        catatalauout.setOnClickListener(v -> {
            // Handle about us action
            Intent intent = new Intent(UserSettingActivity.this, CatatanActivity.class);
            startActivity(intent);
        });

        // Set action for privacy policy
        privacyPolicyLayout.setOnClickListener(v -> {
            // Handle privacy policy action
            Intent intent = new Intent(UserSettingActivity.this, UserPrivacyPolicyActivity.class);
            startActivity(intent);
        });

        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        kebabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(UserSettingActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(UserSettingActivity.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(UserSettingActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

}
