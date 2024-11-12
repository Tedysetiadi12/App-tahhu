package com.tahhu.coba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.tahhu.coba.R;

public class TukarMinyakActivity extends AppCompatActivity {

    private EditText edtJumlahMinyak;
    private Spinner spinnerKategoriTukar;
    private Button btnTukar;
    private Button btnLihatLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_minyak);

        // Inisialisasi komponen
        edtJumlahMinyak = findViewById(R.id.edtJumlahMinyak);
        spinnerKategoriTukar = findViewById(R.id.spinnerKategoriTukar);
        btnTukar = findViewById(R.id.btnTukar);
        btnLihatLokasi = findViewById(R.id.btnLihatLokasi);

        // Listener untuk Button Tukar
        btnTukar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validasi input jumlah minyak
                String jumlahMinyakText = edtJumlahMinyak.getText().toString();
                if (jumlahMinyakText.isEmpty()) {
                    Toast.makeText(TukarMinyakActivity.this, "Masukkan jumlah minyak terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                }

                double jumlahMinyak = Double.parseDouble(jumlahMinyakText);
                if (jumlahMinyak <= 0) {
                    Toast.makeText(TukarMinyakActivity.this, "Jumlah minyak harus lebih dari 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Ambil kategori yang dipilih dari Spinner
                String kategoriTukar = spinnerKategoriTukar.getSelectedItem().toString();
                // Implementasikan logika tukar minyak berdasarkan jumlah dan kategori
                // Misalnya: menampilkan pesan konfirmasi
                Toast.makeText(TukarMinyakActivity.this, "Tukar " + jumlahMinyak + " liter minyak untuk kategori: " + kategoriTukar, Toast.LENGTH_LONG).show();
            }
        });

        // Listener untuk Button Lihat Lokasi
        btnLihatLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementasikan logika untuk melihat lokasi penukaran terdekat
                // Misalnya, navigasi ke activity lain atau buka peta
                Toast.makeText(TukarMinyakActivity.this, "Menampilkan lokasi penukaran terdekat", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
