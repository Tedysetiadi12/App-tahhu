package com.tahhu.coba;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentKirimBarangActivity extends AppCompatActivity {

    private TextView namaPengirim, namaPenerima, tvTotalPrice, biayaBerat, biayaLayanan, addressTextView, destinationTextView;
    private RadioGroup rgMetodePembayaran;
    private EditText etInputPembayaran;
    private Button btnBayar;

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
        rgMetodePembayaran = findViewById(R.id.rg_metode_pembayaran);
        etInputPembayaran = findViewById(R.id.et_input_pembayaran);
        tvTotalPrice = findViewById(R.id.totalPrice);
        biayaBerat = findViewById(R.id.biayaBerat);
        biayaLayanan = findViewById(R.id.biayaLayanan);

        // Ambil data dari Intent
        Intent intent = getIntent();
        String pengirim = intent.getStringExtra("pengirim");
        String penerima = intent.getStringExtra("penerima");
        String address = getIntent().getStringExtra("address");
        String destination = getIntent().getStringExtra("destination");
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
            Intent intent = new Intent(PaymentKirimBarangActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Menutup aktivitas saat ini
        }, 2000); // Delay selama 2 detik
    }
}
