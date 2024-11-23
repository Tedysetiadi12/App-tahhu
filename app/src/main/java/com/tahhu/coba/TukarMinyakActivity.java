package com.tahhu.coba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        kebabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        // Inisialisasi btn_back
        ImageView btnBack = findViewById(R.id.btn_back);

        // Menangani klik pada tombol kembali
        btnBack.setOnClickListener(v -> {
            // Menyelesaikan aktivitas saat tombol kembali diklik
            onBackPressed();
        });
    }

    // Metode untuk menampilkan PopupMenu
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(TukarMinyakActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(TukarMinyakActivity.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(TukarMinyakActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }
}
