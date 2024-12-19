package com.tahhu.id;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class UserSettingActivity extends AppCompatActivity {

    private ImageView backButton, titleButton;
    private LinearLayout changePasswordLayout,logoutlayout ,PiutangLayout,editProfileLayout, addPaymentMethodLayout, pushNotificationLayout, aboutUsLayout,catatalauout, privacyPolicyLayout, kasirLayout;
    private Switch notificationSwitch;

    private FirebaseAuth mAuth;
    private TextView nameTextView, emailTextView;

    private DatabaseReference mDatabase;
    private ActivityResultLauncher<Intent> editProfileLauncher;
    private ImageView homeButton, menuMarket, shortVideo, calculator;
    private FloatingActionButton market;

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
        PiutangLayout = findViewById(R.id.PiutangLayout);
        privacyPolicyLayout = findViewById(R.id.privacyPolicyLayout);
        logoutlayout = findViewById(R.id.logoutLayout);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        kasirLayout = findViewById(R.id.kasirLayout);

        homeButton = findViewById(R.id.homeButton);
        menuMarket = findViewById(R.id.menumarket);
        shortVideo = findViewById(R.id.shortvidio);
        calculator = findViewById(R.id.Kalkulator);
        market = findViewById(R.id.Market);

        // Set click listeners
        homeButton.setOnClickListener(v -> navigateTo("home"));
        menuMarket.setOnClickListener(v -> updateActiveIcon("market"));
        shortVideo.setOnClickListener(v -> navigateTo("videos"));
        calculator.setOnClickListener(v -> navigateTo("calculator"));
        market.setOnClickListener(v -> navigateTo("cart"));
        // Inisialisasi FirebaseAuth dan DatabaseReference
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Cek apakah user sudah login
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Ambil UID pengguna yang login
            String userId = currentUser.getUid();

            // Ambil data pengguna dari Realtime Database
            mDatabase.child("users").child(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        // Ambil data username dari snapshot
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);

                        // Tampilkan username di TextView
                        nameTextView.setText(username);
                        emailTextView.setText(email);
                    } else {
                        Toast.makeText(UserSettingActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserSettingActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Jika pengguna belum login, arahkan ke halaman login
            startActivity(new Intent(UserSettingActivity.this, LoginActivity.class));
            finish();
        }

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

        PiutangLayout.setOnClickListener(v -> {
            // Handle about us action
            Intent intent = new Intent(UserSettingActivity.this, PiutangActivity.class);
            startActivity(intent);
        });

        // Set action for privacy policy
        privacyPolicyLayout.setOnClickListener(v -> {
            // Handle privacy policy action
            Intent intent = new Intent(UserSettingActivity.this, UserPrivacyPolicyActivity.class);
            startActivity(intent);
        });
        logoutlayout.setOnClickListener(v -> {
            // Handle privacy policy action
            mAuth.signOut();
            Intent intent = new Intent(UserSettingActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        kebabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        kasirLayout.setOnClickListener(v -> {
                Intent intent = new Intent(UserSettingActivity.this, KasirActivity.class);
                startActivity(intent);
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

    private void navigateTo(String destination) {
        Intent intent;
        switch (destination) {
            case "home":
                intent = new Intent(this, MainActivity.class);
                break;
            case "videos":
                intent = new Intent(this, ShortVidio.class);
                break;
            case "calculator":
                // Show calculator dialog
                DiscountCalculatorDialog dialog = new DiscountCalculatorDialog();
                dialog.show(getSupportFragmentManager(), "DiscountCalculatorDialog");
                return;
            case "cart":
                intent = new Intent(this, CartProductActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
        if (!destination.equals("market")) {
            finish(); // Close this activity if navigating away
        }
    }
    private void updateActiveIcon(String activeIcon) {
        int activeColor = getResources().getColor(R.color.white);
        int inactiveColor = getResources().getColor(R.color.inactive_icon);

        homeButton.setColorFilter(activeIcon.equals("home") ? activeColor : inactiveColor);
        menuMarket.setColorFilter(activeIcon.equals("market") ? activeColor : inactiveColor);
        shortVideo.setColorFilter(activeIcon.equals("videos") ? activeColor : inactiveColor);
        calculator.setColorFilter(activeIcon.equals("calculator") ? activeColor : inactiveColor);
        // Note: We don't change the color of the FloatingActionButton (market) as it's always highlighted
    }

    public static class DiscountCalculatorDialog extends DialogFragment {
        private EditText etPrice, etDiscount;
        private TextView tvInitialPrice, tvDiscount, tvFinalPrice;
        private Button btnReset;
        private TextWatcher textWatcher;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_diskon_kalkulator, container, false);

            // Initialize views
            etPrice = view.findViewById(R.id.etPrice);
            etDiscount = view.findViewById(R.id.etDiscount);
            tvInitialPrice = view.findViewById(R.id.tvInitialPrice);
            tvDiscount = view.findViewById(R.id.tvDiscount);
            tvFinalPrice = view.findViewById(R.id.tvFinalPrice);
            btnReset = view.findViewById(R.id.btnReset);

            // Set up text change listener
            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    calculateDiscount();
                }
            };

            // Add text change listeners
            etPrice.addTextChangedListener(textWatcher);
            etDiscount.addTextChangedListener(textWatcher);

            // Set up reset button
            btnReset.setOnClickListener(v -> resetCalculator());

            ImageView closeButton = view.findViewById(R.id.ic_close);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            return view;
        }

        private void calculateDiscount() {
            try {
                double price = Double.parseDouble(etPrice.getText().toString().isEmpty() ? "0" : etPrice.getText().toString());
                double discount = Double.parseDouble(etDiscount.getText().toString().isEmpty() ? "0" : etDiscount.getText().toString());

                double discountAmount = (price * discount) / 100;
                double finalPrice = price - discountAmount;

                // Format currency
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                // Update TextViews
                tvInitialPrice.setText(formatter.format(price).replace("IDR", "Rp"));
                tvDiscount.setText("-" + formatter.format(discountAmount).replace("IDR", "Rp"));
                tvFinalPrice.setText(formatter.format(finalPrice).replace("IDR", "Rp"));
            } catch (NumberFormatException e) {
                // Handle invalid input
                tvInitialPrice.setText("Rp 0");
                tvDiscount.setText("-Rp 0");
                tvFinalPrice.setText("Rp 0");
            }
        }

        private void resetCalculator() {
            etPrice.setText("");
            etDiscount.setText("");
            tvInitialPrice.setText("Rp 0");
            tvDiscount.setText("-Rp 0");
            tvFinalPrice.setText("Rp 0");
        }

        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            if (dialog != null) {
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }
        }
    }
}
