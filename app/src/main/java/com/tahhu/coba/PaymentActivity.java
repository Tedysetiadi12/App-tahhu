package com.tahhu.coba;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {
    private TextView totalPricePayment;
    private RadioGroup radioGroupPayment;
    private EditText editCardNumber, editExpiryDate, editSecurityNumber, editCardHolder;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Inisialisasi view dari XML
        totalPricePayment = findViewById(R.id.totalPricePayment);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        editCardNumber = findViewById(R.id.editCardNumber);
        editExpiryDate = findViewById(R.id.editExpiryDate);
        editSecurityNumber = findViewById(R.id.editSecurityNumber);
        editCardHolder = findViewById(R.id.editCardHolder);
        Button btnProceedPayment = findViewById(R.id.btnProceedPayment);

        // Ambil totalPrice yang dikirimkan dari CartProductActivity
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        totalPricePayment.setText("Rp " + String.format("%,.2f", totalPrice));

        btnProceedPayment.setOnClickListener(v -> {
            // Proses pembayaran sesuai pilihan pengguna
            int selectedPaymentMethodId = radioGroupPayment.getCheckedRadioButtonId();
            String paymentMethod = "";

            if (selectedPaymentMethodId == R.id.rb_credit_card) {
                paymentMethod = "Credit Card";
                // Verifikasi data kartu kredit
                String cardNumber = editCardNumber.getText().toString();
                String expiryDate = editExpiryDate.getText().toString();
                String securityCode = editSecurityNumber.getText().toString();
                String cardHolderName = editCardHolder.getText().toString();

                // Validasi input kartu kredit
                if (cardNumber.isEmpty() || expiryDate.isEmpty() || securityCode.isEmpty() || cardHolderName.isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Please fill all card details.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // (misalnya mengirim data kartu ke server untuk proses pembayaran)
            } else if (selectedPaymentMethodId == R.id.rb_bank_transfer) {
                paymentMethod = "Bank Transfer";
                // Implementasikan logika pembayaran dengan transfer bank
            } else if (selectedPaymentMethodId == R.id.rb_paypal) {
                paymentMethod = "PayPal";
                // Implementasikan logika pembayaran dengan PayPal
            }

            // Tampilkan pesan konfirmasi pembayaran
            Toast.makeText(PaymentActivity.this, "Payment via: " + paymentMethod, Toast.LENGTH_LONG).show();
        });
    }
}
