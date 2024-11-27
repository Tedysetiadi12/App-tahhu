package com.tahhu.coba;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
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

        RadioGroup radioGroupPayment = findViewById(R.id.radio_group_payment);
        findViewById(R.id.btn_pay_now).setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String address = etAddress.getText().toString();

            int selectedPaymentId = radioGroupPayment.getCheckedRadioButtonId();
            String paymentMethod = "";

            if (selectedPaymentId == R.id.rb_paypal) {
                paymentMethod = "PayPal";
            } else if (selectedPaymentId == R.id.rb_credit_card) {
                paymentMethod = "Kartu Kredit";
            } else if (selectedPaymentId == R.id.rb_payoneer) {
                paymentMethod = "Payoneer";
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
