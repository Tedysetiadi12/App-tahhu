package com.tahhu.coba;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivitySecurity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_security);

        // Tangkap data dari Intent
        String securityType = getIntent().getStringExtra("SECURITY_TYPE");
        String duration = getIntent().getStringExtra("DURATION");
        String price = getIntent().getStringExtra("PRICE");

        // Tangkap input
        EditText etName = findViewById(R.id.et_name);
        EditText etEmail = findViewById(R.id.et_email);
        EditText etAddress = findViewById(R.id.et_address);

        // Tampilkan data di TextView
        TextView paymentDetails = findViewById(R.id.tv_payment_details);
        paymentDetails.setText("Pengamanan: " + securityType +
                "\nLama Waktu: " + duration +
                "\nHarga: " + price);
        ImageView iconArrow = findViewById(R.id.icon_arrowsecurity);
        RadioGroup radioGroupPayment = findViewById(R.id.radio_group_payment);
        iconArrow.setOnClickListener(new View.OnClickListener() {
            private boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                RadioButton rbOne = findViewById(R.id.rb_dana);
                RadioButton rbTwo = findViewById(R.id.rb_gopay);

                if (isExpanded) {
                    // Sembunyikan semua radio button kecuali rbOne dan rbTwo
                    for (int i = 0; i < radioGroupPayment.getChildCount(); i++) {
                        View child = radioGroupPayment.getChildAt(i);
                        if (child != rbOne && child != rbTwo) {
                            child.setVisibility(View.GONE);
                        }
                    }
                    iconArrow.setImageResource(R.drawable.ic_arrowdown); // Ganti ke ikon panah ke bawah
                } else {
                    // Tampilkan semua radio button
                    for (int i = 0; i < radioGroupPayment.getChildCount(); i++) {
                        View child = radioGroupPayment.getChildAt(i);
                        child.setVisibility(View.VISIBLE);
                    }
                    iconArrow.setImageResource(R.drawable.ic_arrowup); // Ganti ke ikon panah ke atas
                }
                isExpanded = !isExpanded;
            }
        });

        findViewById(R.id.btn_pay_now).setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String address = etAddress.getText().toString();

            int selectedPaymentId = radioGroupPayment.getCheckedRadioButtonId();
            String paymentMethod = "";

            if (selectedPaymentId == R.id.rb_dana) {
                paymentMethod = "Dana";
            } else if (selectedPaymentId == R.id.rb_gopay) {
                paymentMethod = "Gopay ";
            } else if (selectedPaymentId == R.id.rb_ovo) {
                paymentMethod = "Ovo";
            }else if (selectedPaymentId == R.id.rb_qris) {
                paymentMethod = "Qris";
            }else if (selectedPaymentId == R.id.rb_bri) {
                paymentMethod = "Bri";
            }else if (selectedPaymentId == R.id.rb_mandiri) {
                paymentMethod = "Mandiri";
            }

            if (name.isEmpty() || email.isEmpty() || address.isEmpty() || paymentMethod.isEmpty()) {
                // Validasi: semua data harus diisi
                Toast.makeText(this, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show();
                return;
            }

                showSuccessDialog();
        });

        // Inisialisasi btn_back
        ImageView btnBack = findViewById(R.id.btn_back);

        // Menangani klik pada tombol kembali
        btnBack.setOnClickListener(v -> {
            // Menyelesaikan aktivitas saat tombol kembali diklik
            onBackPressed();
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
            Intent intent = new Intent(PaymentActivitySecurity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Menutup aktivitas saat ini
        }, 2000); // Delay selama 2 detik
    }
}
