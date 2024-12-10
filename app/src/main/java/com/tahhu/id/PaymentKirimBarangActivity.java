package com.tahhu.id;

import android.annotation.SuppressLint;
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

public class PaymentKirimBarangActivity extends AppCompatActivity {

    private TextView namaPengirim, namaPenerima, tvTotalPrice, biayaBerat, biayaLayanan, addressTextView, destinationTextView, layananTextView;
    private RadioGroup rgMetodePembayaran;
    private EditText etInputPembayaran;
    private Button btnBayar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_kirim_barang);

        // Inisialisasi TextView
        addressTextView = findViewById(R.id.addressTextView);
        destinationTextView = findViewById(R.id.destinationTextView);
        namaPengirim = findViewById(R.id.namaPengirim);
        namaPenerima = findViewById(R.id.namaPenerima);
        btnBayar = findViewById(R.id.btn_bayar_sekarang);
        rgMetodePembayaran = findViewById(R.id.rDpaymentBarang);
        etInputPembayaran = findViewById(R.id.et_input_pembayaran);
        tvTotalPrice = findViewById(R.id.totalPrice);
        biayaBerat = findViewById(R.id.biayaBerat);
        biayaLayanan = findViewById(R.id.biayaLayanan);
        layananTextView = findViewById(R.id.layanan);

        ImageView iconArrow = findViewById(R.id.icon_arrow3);

        // Ambil data dari Intent
        Intent intent = getIntent();
        String pengirim = intent.getStringExtra("pengirim");
        String penerima = intent.getStringExtra("penerima");
        String address = getIntent().getStringExtra("address");
        String destination = getIntent().getStringExtra("destination");
        String layananText = intent.getStringExtra("selection_info");
        double berat = intent.getDoubleExtra("berat", 0);
        String layanan = intent.getStringExtra("layanan");
        double ongkir = intent.getDoubleExtra("ongkir", 0);

        // Tampilkan ringkasan
        String ringkasan = "Pengirim: " + pengirim +
                "\nPenerima: " + penerima ;

        namaPengirim.setText(pengirim);
        namaPenerima.setText(penerima);
        addressTextView.setText(address);
        destinationTextView.setText(destination);
        layananTextView.setText(layananText);
        biayaBerat.setText(berat + " kg");
        biayaLayanan.setText("Rp. " + (layanan.equalsIgnoreCase("Reguler") ? 15000 : 25000));
        tvTotalPrice.setText("Rp. " + ongkir);

        // Tombol Bayar Sekarang
        btnBayar.setOnClickListener(v -> {
            int metodeId = rgMetodePembayaran.getCheckedRadioButtonId();
            if (metodeId == -1) {
                Toast.makeText(this, "Pilih metode pembayaran!", Toast.LENGTH_SHORT).show();
                return;
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
                    for (int i = 0; i < rgMetodePembayaran.getChildCount(); i++) {
                        View child = rgMetodePembayaran.getChildAt(i);
                        if (child != rbOne && child != rbTwo) {
                            child.setVisibility(View.GONE);
                        }
                    }
                    iconArrow.setImageResource(R.drawable.ic_arrowdown); // Ganti ke ikon panah ke bawah
                } else {
                    // Tampilkan semua radio button
                    for (int i = 0; i < rgMetodePembayaran.getChildCount(); i++) {
                        View child = rgMetodePembayaran.getChildAt(i);
                        child.setVisibility(View.VISIBLE);
                    }
                    iconArrow.setImageResource(R.drawable.ic_arrowup); // Ganti ke ikon panah ke atas
                }
                isExpanded = !isExpanded;
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
            Intent intent = new Intent(PaymentKirimBarangActivity.this, DialogOrderFinishedKirimBarang.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Menutup aktivitas saat ini
        }, 1000); // Delay selama 2 detik
    }
}
