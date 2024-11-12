package com.tahhu.coba;
//
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class UcoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uco);
        // Tombol kembali
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke halaman sebelumnya
            }
        });
        // Tombol Tukar Minyak Jelantah
        Button btnTukarMinyak = findViewById(R.id.btn_tukar_minyak);
        btnTukarMinyak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 Tambahkan logika untuk tukar minyak jelantah
                Intent intent = new Intent(UcoActivity.this, TukarMinyakActivity.class);
                startActivity(intent);
            }
        });
        // Tombol Lokasi Penukaran
        Button btnLokasiPenukaran = findViewById(R.id.btn_lokasi_penukaran);
        btnLokasiPenukaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tambahkan logika untuk lokasi penukaran
                Intent intent = new Intent(UcoActivity.this, LokasiPenukaranActivity.class);
                startActivity(intent);
            }
        });
        // Tombol Order Penjemputan
        Button btnOrderPenjemputan = findViewById(R.id.btn_order_penjemputan);
        btnOrderPenjemputan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tambahkan logika untuk order penjemputan
                Intent intent = new Intent(UcoActivity.this, OrderPenjemputanActivity.class);
                startActivity(intent);
            }
        });
    }
}