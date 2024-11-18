package com.tahhu.coba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivityRideSharing extends AppCompatActivity {

    private TextView addressTextView, destinationTextView, vehicleNameTextView, vehiclePriceTextView;
    private ImageView vehicleImageView;
    private RadioGroup paymentMethodGroup;
    private Button confirmPaymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_ridesharing);

        // Inisialisasi TextView
        vehicleNameTextView = findViewById(R.id.vehicleName);
        vehiclePriceTextView = findViewById(R.id.vehiclePrice);
        vehicleImageView = findViewById(R.id.vehicleImage);
        addressTextView = findViewById(R.id.addressTextView);
        destinationTextView = findViewById(R.id.destinationTextView);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        // Mengambil data dari Intent

        String vehicleName = getIntent().getStringExtra("selected_vehicle_name");
        double vehiclePrice = getIntent().getDoubleExtra("selected_vehicle_price", 0.0);
        int vehicleImageResId = getIntent().getIntExtra("selected_vehicle_image_res_id", R.drawable.home);
        String address = getIntent().getStringExtra("address");
        String destination = getIntent().getStringExtra("destination");

        // Menampilkan data yang diterima pada TextView
        vehicleNameTextView.setText(vehicleName);
        vehiclePriceTextView.setText(String.format("Rp %.2f", vehiclePrice));
        vehicleImageView.setImageResource(vehicleImageResId);
        addressTextView.setText(address);
        destinationTextView.setText(destination);

        // Konfirmasi pembayaran
        confirmPaymentButton.setOnClickListener(v -> {
            int selectedPaymentMethod = paymentMethodGroup.getCheckedRadioButtonId();
            if (selectedPaymentMethod == -1) {
                Toast.makeText(PaymentActivityRideSharing.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            } else {
                processPayment();
            }
        });
    }

    private void processPayment() {
        // Logic untuk memproses pembayaran
        Toast.makeText(this, "Payment Confirmed!", Toast.LENGTH_SHORT).show();
    }
}
