package com.tahhu.coba;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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
        RadioGroup radioGroupPaymentOptions = findViewById(R.id.radioGroupPaymentOptions);
        TextView pilihanpayment = findViewById(R.id.idPayment);

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
            radioGroupPaymentOptions.setVisibility(View.GONE);

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
            radioGroupPaymentOptions.setVisibility(View.VISIBLE);
            pilihanpayment.setVisibility(View.VISIBLE);
        }

        // Tampilkan ringkasan
        String detailRingkasan = "Jumlah minyak yang ditukar: " + "\n" + jumlahMinyak + " liter\n\n" +
                "Alamat Penjemputan:\n" + alamatPenjemputan + "\n\n" +
                "Lokasi Penukaran:\n" + lokasi;

        tvDetailRingkasan.setText(detailRingkasan);

        // Tombol konfirmasi
        btnKonfirmasi.setOnClickListener(v -> {
            // Proses konfirmasi logika atau transaksi
            showSuccessDialog();
        });

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
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Menutup aktivitas saat ini
        }, 2000); // Delay selama 2 detik
    }

}
