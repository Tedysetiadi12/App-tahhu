package com.tahhu.id;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentSecurityServicesActivity extends AppCompatActivity {
    private TextView securityDetailsTextView;
    private RadioGroup paymentMethodRadioGroup;
    private EditText nameEditText, emailEditText, addressEditText;
    private Button confirmPaymentButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private String securityName;
    private String securityType;
    private String period;
    private String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_security);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("security");

        securityDetailsTextView = findViewById(R.id.securityDetailsTextView);
        paymentMethodRadioGroup = findViewById(R.id.paymentMethodRadioGroup);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        securityName = getIntent().getStringExtra("securityName");
        securityType = getIntent().getStringExtra("securityType");
        period = getIntent().getStringExtra("period");
        price = getIntent().getStringExtra("price");

        String securityTypeDisplay = securityType.equals("Kantor") ? "Security Kantor" : "Security Pribadi";
        String periodDisplay = "";
        switch (period) {
            case "Harian":
                periodDisplay = "Harian";
                break;
            case "Bulanan":
                periodDisplay = "Bulanan";
                break;
            case "Tahunan":
                periodDisplay = "Tahunan";
                break;
        }

        securityDetailsTextView.setText("Security: " + securityName +
                "\nType: " + securityTypeDisplay +
                "\nPeriod: " + periodDisplay +
                "\nPrice: " + price);

        confirmPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBookingToFirebase();
            }
        });

        ImageView icon_arrowsecurity = findViewById(R.id.icon_arrowsecurity);
        icon_arrowsecurity.setOnClickListener(new View.OnClickListener() {
            private boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                RadioButton rbOne = findViewById(R.id.rb_dana);
                RadioButton rbTwo = findViewById(R.id.rb_gopay);

                if (isExpanded) {
                    // Sembunyikan semua radio button kecuali rbOne dan rbTwo
                    for (int i = 0; i < paymentMethodRadioGroup.getChildCount(); i++) {
                        View child = paymentMethodRadioGroup.getChildAt(i);
                        if (child != rbOne && child != rbTwo) {
                            child.setVisibility(View.GONE);
                        }
                    }
                    icon_arrowsecurity.setImageResource(R.drawable.ic_arrowdown); // Ganti ke ikon panah ke bawah
                } else {
                    // Tampilkan semua radio button
                    for (int i = 0; i < paymentMethodRadioGroup.getChildCount(); i++) {
                        View child = paymentMethodRadioGroup.getChildAt(i);
                        child.setVisibility(View.VISIBLE);
                    }
                    icon_arrowsecurity.setImageResource(R.drawable.ic_arrowup); // Ganti ke ikon panah ke atas
                }
                isExpanded = !isExpanded;
            }
        });
    }

    private void saveBookingToFirebase() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String address = addressEditText.getText().toString();
        int selectedPaymentMethodId = paymentMethodRadioGroup.getCheckedRadioButtonId();
        String paymentMethod = "";

        if (selectedPaymentMethodId == R.id.rb_dana) {
            paymentMethod = "DANA";
        } else if (selectedPaymentMethodId == R.id.rb_qris) {
            paymentMethod = "QRIS";
        } else if (selectedPaymentMethodId == R.id.rb_gopay) {
            paymentMethod = "GoPay";
        } else if (selectedPaymentMethodId == R.id.rb_ovo) {
            paymentMethod = "OVO";
        } else if (selectedPaymentMethodId == R.id.rb_bri) {
            paymentMethod = "Bank Transfer BRI";
        } else if (selectedPaymentMethodId == R.id.rb_bca) {
            paymentMethod = "Bank Transfer BCA";
        } else if (selectedPaymentMethodId == R.id.rb_mandiri) {
            paymentMethod = "Bank Transfer Mandiri";
        }

        if (paymentMethod.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        } else if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        } else if (selectedPaymentMethodId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        String bookingId = databaseReference.push().getKey();
        Booking booking = new Booking(bookingId, securityName, securityType, period, price, paymentMethod, address);

        databaseReference.child(bookingId).setValue(booking)
                .addOnSuccessListener(aVoid -> {
                    showSuccessDialog();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PaymentSecurityServicesActivity.this, "Failed to save booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showSuccessDialog() {

        // Buat dialog
        Dialog successDialog = new Dialog(this);
        successDialog.setContentView(R.layout.dialog_payment_success);
        successDialog.setCancelable(false); // Dialog tidak bisa ditutup dengan back button

        // Tampilkan dialog
        successDialog.show();

        // Jalankan delay untuk redirect setelah beberapa detik
        new Handler().postDelayed(() -> {
            successDialog.dismiss(); // Tutup dialog
            // Redirect ke halaman Home
            Intent intent = new Intent(PaymentSecurityServicesActivity.this, SecurityServices.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Menutup aktivitas saat ini
        }, 1000); // Delay selama 2 detik
    }
}

