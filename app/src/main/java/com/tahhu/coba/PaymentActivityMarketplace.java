package com.tahhu.coba;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.widget.RadioButton;

public class PaymentActivityMarketplace extends AppCompatActivity {
    private TextView totalPriceView, shippingCostView, finalPriceView;
    private static final int REQUEST_ADD_ADDRESS = 1;
    private TextView addressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_marketplace);

        // Inisialisasi view dari XML
        ImageView iconArrow = findViewById(R.id.icon_arrow);
        finalPriceView = findViewById(R.id.totalPriceView);
        shippingCostView = findViewById(R.id.shippingCostView);
        totalPriceView = findViewById(R.id.finalPriceView);

        // Menerima data dari Intent
        Intent intent = getIntent();
        List<CartProduct> cartProductList =
                (List<CartProduct>) getIntent().getSerializableExtra("cartProductList");

        RecyclerView paymentRecyclerView = findViewById(R.id.paymentRecyclerView);
        PaymentAdapter paymentAdapter = new PaymentAdapter(this, cartProductList);
        paymentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentRecyclerView.setAdapter(paymentAdapter);

        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0);
        int shippingCost = getIntent().getIntExtra("shippingCost", 0);

        shippingCostView.setText("Rp " + String.format("%,d", shippingCost));
        totalPriceView.setText("Rp " + String.format("%,.2f", totalPrice));

        // Menghitung harga final (totalPrice + shippingCost)
        double finalPrice = totalPrice - shippingCost;
        finalPriceView.setText("Rp" + String.format("%,.2f", finalPrice));

        // Inisialisasi RadioGroup dan Button
        RadioGroup radioGroupPayment = findViewById(R.id.radioGroupPayment);
        Button btnProceedPayment = findViewById(R.id.btnProceedPayment);

        // Menambahkan click listener pada btnProceedPayment
        btnProceedPayment.setOnClickListener(v -> {
            int selectedRadioId = radioGroupPayment.getCheckedRadioButtonId();
            if (selectedRadioId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedRadioId);
                String selectedPaymentMethod = selectedRadioButton.getText().toString();
                Toast.makeText(PaymentActivityMarketplace.this, "Kamu memilih: " + selectedPaymentMethod, Toast.LENGTH_SHORT).show();

                // Panggil dialog dengan data produk dan harga akhir
                showSuccessDialog(cartProductList, totalPrice, finalPrice, shippingCost);
            } else {
                Toast.makeText(PaymentActivityMarketplace.this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show();
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

        Button btnAddAddress = findViewById(R.id.btnAddAddress);
        addressView = findViewById(R.id.addressView); // TextView untuk menampilkan alamat

        // Klik tombol Tambah Alamat
        btnAddAddress.setOnClickListener(v -> {
            Intent alamat = new Intent(PaymentActivityMarketplace.this, AddAddressActivity.class);
            startActivityForResult(alamat, REQUEST_ADD_ADDRESS);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_ADDRESS && resultCode == RESULT_OK) {
            // Ambil data dari AddAddressActivity
            String recipientName = data.getStringExtra("recipientName");
            String address = data.getStringExtra("address");
            String city = data.getStringExtra("city");
            String district = data.getStringExtra("district");

            // Tampilkan data di TextView dan sembunyikan tombol Tambah Alamat
            addressView.setText(String.format("Nama: %s\nalamat: %s\nKota: %s\nKecamatan: %s", recipientName, address, city, district));
            findViewById(R.id.btnAddAddress).setVisibility(View.GONE);
            addressView.setVisibility(View.VISIBLE);
        }
    }

    private void showSuccessDialog(List<CartProduct> cartProductList, double totalPrice, double finalPrice, int shippingCost) {
        // Buat dialog
        Dialog successDialog = new Dialog(this);
        successDialog.setContentView(R.layout.dialog_payment_success);
        successDialog.setCancelable(false); // Dialog tidak bisa ditutup dengan back button
        successDialog.show();

        // Jalankan delay untuk redirect setelah beberapa detik
        new Handler().postDelayed(() -> {
            successDialog.dismiss(); // Tutup dialog

            // Intent ke DialogOrderFinishedActivity
            Intent intent = new Intent(PaymentActivityMarketplace.this, DialogOrderFinishedMarketplace.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            // Kirim data produk dan harga akhir ke halaman berikutnya
            intent.putExtra("cartProductList", (Serializable) cartProductList);
            intent.putExtra("totalPrice", totalPrice);
            intent.putExtra("finalPrice", finalPrice);
            intent.putExtra("shippingCost", shippingCost);
            startActivity(intent);
            finish(); // Menutup aktivitas saat ini
        }, 1000); // Delay selama 2 detik
    }
}
