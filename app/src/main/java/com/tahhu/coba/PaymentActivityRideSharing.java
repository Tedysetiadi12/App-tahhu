package com.tahhu.coba;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivityRideSharing extends AppCompatActivity {

    private TextView addressTextView, destinationTextView, vehicleNameTextView, vehiclePriceTextView, totalPriceTextView, feeService, detailVehicleView;
    private ImageView vehicleImageView;
    private RadioGroup radioGroupPayment;
    private Button confirmPaymentButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_ridesharing);

        // Inisialisasi TextView
        vehicleNameTextView = findViewById(R.id.vehicleName);
        vehiclePriceTextView = findViewById(R.id.vehiclePrice);
        vehicleImageView = findViewById(R.id.vehicleImage);
        detailVehicleView = findViewById(R.id.detailVehicle);
        addressTextView = findViewById(R.id.addressTextView);
        destinationTextView = findViewById(R.id.destinationTextView);
        radioGroupPayment = findViewById(R.id.radioGroupPembayaran);
        totalPriceTextView = findViewById(R.id.totalPrice);
        feeService = findViewById(R.id.feeService);
        ImageView iconArrow = findViewById(R.id.icon_arrow2);

        // Mengambil data dari Intent
        String vehicleName = getIntent().getStringExtra("selected_vehicle_name");
        double vehiclePrice = getIntent().getDoubleExtra("selected_vehicle_price", 0);
        int vehicleImageResId = getIntent().getIntExtra("selected_vehicle_image_res_id", R.drawable.home);
        String detailVehicle = getIntent().getStringExtra("selected_vehicle_detail");
        String address = getIntent().getStringExtra("address");
        String destination = getIntent().getStringExtra("destination");
        double serviceFee = 20000;  // Set biaya layanan
        double totalWithServiceFee = vehiclePrice + serviceFee;  // Hitung total dengan biaya layanan


        // Menampilkan data yang diterima pada TextView
        vehicleNameTextView.setText(vehicleName);
        vehiclePriceTextView.setText(("Rp. " + vehiclePrice));
        vehicleImageView.setImageResource(vehicleImageResId);
        detailVehicleView.setText(detailVehicle);
        addressTextView.setText(address);
        destinationTextView.setText(destination);

        feeService.setText("Rp. " + serviceFee);
        totalPriceTextView.setText("Rp. " + totalWithServiceFee);

        // Konfirmasi pembayaran
        Button btnConfirm = findViewById(R.id.confirmPaymentButton);
        btnConfirm.setOnClickListener(v -> {
            int selectedPaymentMethod = radioGroupPayment.getCheckedRadioButtonId();
            if (selectedPaymentMethod == -1) {
                Toast.makeText(PaymentActivityRideSharing.this, "Pilih metode pembayaran terlebih dahulu.", Toast.LENGTH_SHORT).show();
            } else {
                showSuccessDialog();
            }
        });
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

        // Inisialisasi btn_back
        ImageView btnBack = findViewById(R.id.btn_back);
        // Menangani klik pada tombol kembali
        btnBack.setOnClickListener(v -> {
            // Menyelesaikan aktivitas saat tombol kembali diklik
            onBackPressed();
        });

        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        kebabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
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
            Intent intent = new Intent(PaymentActivityRideSharing.this, RatingRideSharingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Menutup aktivitas saat ini
        }, 1000); // Delay selama 2 detik
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(PaymentActivityRideSharing.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(PaymentActivityRideSharing.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(PaymentActivityRideSharing.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }
}
