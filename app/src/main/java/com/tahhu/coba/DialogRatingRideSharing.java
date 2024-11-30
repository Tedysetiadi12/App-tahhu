package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DialogRatingRideSharing extends AppCompatActivity {

    private TextView vehiclePriceTextView, selectionInfoTextView, feeServiceTextView, totalPriceTextView, paymentMethodTextView;
    private RatingBar ratingBar;
    private Button giveRateButton;
    private TextView toggleDetailsButton;
    private View detailsLayout;
    private View closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rating_ridesharing);

        // Inisialisasi TextView dari layout
        vehiclePriceTextView = findViewById(R.id.tv_vehiclePrice);
        selectionInfoTextView = findViewById(R.id.tv_selection_info);
        feeServiceTextView = findViewById(R.id.tv_fee_service);
        totalPriceTextView = findViewById(R.id.tv_total_price);
        paymentMethodTextView  = findViewById(R.id.tv_payment_method);

        // Ambil data dari Intent
        double vehiclePrice = getIntent().getDoubleExtra("selected_vehicle_price", 0.0);
        String selectionInfo = getIntent().getStringExtra("selection_info");
        String feeService = getIntent().getStringExtra("fee_service");
        String totalPrice = getIntent().getStringExtra("total_price");
        String selectedPaymentMethod = getIntent().getStringExtra("selected_payment_method");

        if (paymentMethodTextView != null) {
            paymentMethodTextView.setText("Metode Pembayaran: " + selectedPaymentMethod);
        }

        // Tampilkan data pada TextView
        vehiclePriceTextView.setText("Biaya Layanan Rp. " + vehiclePrice);
        selectionInfoTextView.setText("Layanan: " + selectionInfo);
        feeServiceTextView.setText("Biaya Tambahan: " + feeService);
        totalPriceTextView.setText("Total Biaya: " + totalPrice);

        ratingBar = findViewById(R.id.ratingBar);
        giveRateButton = findViewById(R.id.giveRateButton);
        toggleDetailsButton = findViewById(R.id.toggleDetailsButton);
        detailsLayout = findViewById(R.id.detailsLayout);
        closeButton = findViewById(R.id.closeButton);
        giveRateButton.setEnabled(false);
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            giveRateButton.setEnabled(rating > 0);
        });

        giveRateButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            Toast.makeText(this, "Rating submitted: " + rating, Toast.LENGTH_SHORT).show();
            // Here you would typically send the rating to your backend
            Intent intent = new Intent(DialogRatingRideSharing.this, MainActivity.class);
            startActivity(intent);
        });

        toggleDetailsButton.setOnClickListener(v -> {
            if (detailsLayout.getVisibility() == View.VISIBLE) {
                detailsLayout.setVisibility(View.GONE);
                toggleDetailsButton.setText("Show details");
            } else {
                detailsLayout.setVisibility(View.VISIBLE);
                toggleDetailsButton.setText("Hide details");
            }
        });

        closeButton.setOnClickListener(v -> {
                Intent intent = new Intent(DialogRatingRideSharing.this, MainActivity.class);
                startActivity(intent);
    });
    }
}

