package com.tahhu.id;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class PaymentUcoActivity extends AppCompatActivity {
    private TextView tvDetailRingkasan, tvtotal1, tvtotal2, tvFee;
    private Button btnKonfirmasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_uco);

        // Inisialisasi komponen
        tvDetailRingkasan = findViewById(R.id.tvDetailRingkasan);
        btnKonfirmasi = findViewById(R.id.btnKonfirmasi);
        tvtotal1 = findViewById(R.id.tv_total1);
        tvFee = findViewById(R.id.tv_fee);
        tvtotal2 = findViewById(R.id.tv_total2);
        TextView tvTextView1 = findViewById(R.id.tvTextView1);
        TextView tvTextView2 = findViewById(R.id.tvTextView2);
        TextView tvTextView3 = findViewById(R.id.tvTextView3);

        // Menambahkan referensi untuk RadioGroup
        TextView pilihanpayment = findViewById(R.id.idBayar);
        TextView terimaUang = findViewById(R.id.idTerima);
        RadioGroup radioGroupPaymentOptions = findViewById(R.id.radioGroupPaymentOptions);

        // Ambil data dari Intent
        int jumlahMinyak = getIntent().getIntExtra("jumlahMinyak", 0);
        String alamatPenjemputan = getIntent().getStringExtra("alamatPenjemputan");
        String lokasi = getIntent().getStringExtra("lokasi");
        String pilihanPenukaran = getIntent().getStringExtra("pilihanPenukaran");

        // Variabel biaya fee
        int fee = 0;
        String ringkasan;

        // Logika hasil tukar dan biaya fee
        if (pilihanPenukaran.equals("Tukar dengan Minyak Baru")) {
            fee = 10000;
            int jumlahMinyakBaru = (jumlahMinyak / 2);
            ringkasan = "Anda akan menerima " + jumlahMinyakBaru + " liter minyak baru setelah dikenakan biaya fee sebesar Rp" + fee + ".";

            // Update tvTextView1 untuk Menampilkan Pilihan "Tukar dengan Minyak Baru"
            tvTextView1.setText("Minyak yang diperoleh");
            tvTextView2.setText("Fee");
            tvTextView3.setText("Total yang dibayar");

            // Update tv_total untuk Menampilkan jumlah liter
            tvtotal1.setText(jumlahMinyakBaru + " liter");
            tvFee.setText("Rp" + fee);
            tvtotal2.setText("Rp" + fee);

            // Sembunyikan RadioGroup Payment jika Tukar Minyak
            terimaUang.setVisibility(View.GONE);
            pilihanpayment.setVisibility(View.VISIBLE);
//            radioGroupPaymentOptions.setVisibility(View.GONE);

            btnKonfirmasi.setOnClickListener(v -> {
                int selectedRadioButtonId = radioGroupPaymentOptions.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    String selectedPaymentOption = selectedRadioButton.getText().toString();
                    Toast.makeText(PaymentUcoActivity.this, "Kamu memilih: " + selectedPaymentOption, Toast.LENGTH_SHORT).show();
                    showSuccessDialog();
                } else {
                    Toast.makeText(PaymentUcoActivity.this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            fee = 5000;
            int jumlahUang = ((jumlahMinyak / 2) * 15000);
            int jumlahUangTotal = ((jumlahMinyak / 2) * 15000) - fee;
            ringkasan = "Anda akan menerima uang sebesar Rp" + jumlahUang + " setelah dikenakan biaya fee sebesar Rp" + fee + ".";

            // Update tvTextView1 untuk Menampilkan Pilihan "Tukar dengan Uang"
            tvTextView1.setText("Harga minyak");
            tvTextView2.setText("Fee");
            tvTextView3.setText("Uang yang diperoleh");

            // Update tv_total untuk Menampilkan nominal uang
            tvtotal1.setText("Rp" + jumlahUang);
            tvFee.setText("Rp" + fee);
            tvtotal2.setText("Rp" + jumlahUangTotal);

            // Tampilkan RadioGroup Payment jika Tukar Uang
            pilihanpayment.setVisibility(View.GONE);
            terimaUang.setVisibility(View.VISIBLE);
//            radioGroupPaymentOptions.setVisibility(View.VISIBLE);

            btnKonfirmasi.setOnClickListener(v -> {
                int selectedRadioButtonId = radioGroupPaymentOptions.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    String selectedPaymentOption = selectedRadioButton.getText().toString();
                    Toast.makeText(PaymentUcoActivity.this, "Kamu memilih: " + selectedPaymentOption, Toast.LENGTH_SHORT).show();
                    showSuccessDialog();
                } else {
                    Toast.makeText(PaymentUcoActivity.this, "Pilih metode penerimaan", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Tampilkan ringkasan
        String detailRingkasan = "Jumlah minyak yang ditukar: " + "\n" + jumlahMinyak + " liter\n\n" +
                "Alamat Penjemputan:\n" + alamatPenjemputan + "\n\n" +
                "Lokasi Penukaran:\n" + lokasi;

        tvDetailRingkasan.setText(detailRingkasan);

    }
    private void showSuccessDialog() {
        // Buat dialog
        Dialog successDialog = new Dialog(this);
        successDialog.setContentView(R.layout.dialog_payment_success);
        successDialog.setCancelable(false); // Dialog tidak bisa ditutup dengan back button
        // Misalnya, memulai aktivitas baru atau menampilkan pesan sukses
        // Tampilkan dialog
        successDialog.show();

        // Jalankan delay untuk redirect setelah beberapa detik
        new Handler().postDelayed(() -> {
            successDialog.dismiss(); // Tutup dialog
            // Redirect ke halaman Home
            Intent intent = new Intent(PaymentUcoActivity.this, DialogOrderFinishedUco.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Menutup aktivitas saat ini
        }, 1000); // Delay selama 2 detik
    }

}