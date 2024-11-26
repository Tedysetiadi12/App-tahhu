package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
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
            int jumlahMinyakBaru = (jumlahMinyak / 2) ;
            ringkasan = "Anda akan menerima " + jumlahMinyakBaru + " liter minyak baru setelah dikenakan biaya fee sebesar Rp" + fee + ".";

            // Update tvTextView1 untuk Menampilkan Pilihan "Tukar dengan Minyak Baru"
            tvTextView1.setText("Minyak yang diperoleh");
            tvTextView2.setText("Fee");
            tvTextView3.setText("Total yang dibayar");

            // Update tv_total untuk Menampilkan jumlah liter
            tvtotal1.setText(jumlahMinyakBaru + " liter");
            tvFee.setText("Rp" + fee);
            tvtotal2.setText("Rp" + fee);

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
        }

        // Tampilkan ringkasan
        String detailRingkasan = "Jumlah minyak yang ditukar: " + "\n" + jumlahMinyak + " liter\n\n" +
                "Alamat Penjemputan:\n" + alamatPenjemputan + "\n\n" +
                "Lokasi Penukaran:\n" + lokasi;

        tvDetailRingkasan.setText(detailRingkasan);

        // Tombol konfirmasi
        btnKonfirmasi.setOnClickListener(v -> {
            // Proses konfirmasi logika atau transaksi
            // Misalnya, memulai aktivitas baru atau menampilkan pesan sukses
            Toast.makeText(this, "Konfirmasi berhasil!", Toast.LENGTH_SHORT).show();
            // Delay sebelum kembali ke MainActivity
            new Handler().postDelayed(() -> {
                // Mengarahkan ke MainActivity setelah delay
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }, 1000); // Delay 1 detik (1000 ms)
        });
    }
}
