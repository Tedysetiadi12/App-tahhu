package com.tahhu.coba;
package com.tahhu.coba;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymeentUcoActivity extends AppCompatActivity {

    private TextView txtJumlahMinyak, txtKategoriTukar, txtLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_uco);

        // Inisialisasi komponen
        txtJumlahMinyak = findViewById(R.id.txtJumlahMinyak);
        txtKategoriTukar = findViewById(R.id.txtKategoriTukar);
        txtLokasi = findViewById(R.id.txtLokasi);

        // Ambil data dari Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String jumlahMinyak = extras.getString("jumlahMinyak");
            String kategoriTukar = extras.getString("kategoriTukar");
            String lokasi = extras.getString("lokasi");

            // Set data ke TextView
            txtJumlahMinyak.setText("Jumlah Minyak: " + jumlahMinyak + " liter");
            txtKategoriTukar.setText("Kategori Tukar: " + kategoriTukar);
            txtLokasi.setText("Lokasi Terdekat: " + lokasi);
        } else {
            Toast.makeText(this, "Data tidak tersedia!", Toast.LENGTH_SHORT).show();
        }
    }
}
