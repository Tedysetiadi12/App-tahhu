package com.tahhu.coba;

import android.content.Intent;
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
    private Spinner spinnerKategoriTukar, spinnerLihatLokasi;
    private Button btnTukar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_minyak);

        // Inisialisasi komponen
        edtJumlahMinyak = findViewById(R.id.edtJumlahMinyak);
        spinnerKategoriTukar = findViewById(R.id.spinnerKategoriTukar);
        btnTukar = findViewById(R.id.btnTukar);
        spinnerLihatLokasi = findViewById(R.id.spinnerLihatLokasi);

        // Listener untuk Button Tukar
        btnTukar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validasi input
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

                String kategoriTukar = spinnerKategoriTukar.getSelectedItem().toString();
                String lokasi = spinnerLihatLokasi.getSelectedItem().toString();

                if (kategoriTukar.isEmpty() || lokasi.isEmpty()) {
                    Toast.makeText(TukarMinyakActivity.this, "Pilih kategori dan lokasi terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Hitung jumlah minyak yang valid berdasarkan rasio 2:1
                int jumlahMinyakValid = (int) Math.floor(jumlahMinyak / 2); // Pembulatan ke bawah

                if (jumlahMinyakValid <= 0) {
                    Toast.makeText(TukarMinyakActivity.this, "Jumlah minyak terlalu sedikit untuk ditukar", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Pindah ke PaymentUcoActivity
                Intent intent = new Intent(TukarMinyakActivity.this, PaymentUcoActivity.class);
                intent.putExtra("jumlahMinyak", String.valueOf(jumlahMinyakValid)); // Kirim hasil yang valid
                intent.putExtra("kategoriTukar", kategoriTukar);
                intent.putExtra("lokasi", lokasi);
                startActivity(intent);
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
