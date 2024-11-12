package com.tahhu.coba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TukarMinyakActivity extends AppCompatActivity {

    private EditText edtJumlahMinyak;
    private Button btnTukar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_minyak);
        // Tombol kembali
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke halaman sebelumnya
            }
        });
        // Initialize views
        edtJumlahMinyak = findViewById(R.id.edtJumlahMinyak);
        btnTukar = findViewById(R.id.btnTukar);

        // Set button click listener
        btnTukar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jumlahMinyak = edtJumlahMinyak.getText().toString().trim();

                // Validate input
                if (jumlahMinyak.isEmpty()) {
                    Toast.makeText(TukarMinyakActivity.this, "Harap masukkan jumlah minyak jelantah", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle Tukar action (e.g., call API or save to database)
                    Toast.makeText(TukarMinyakActivity.this, "Minyak jelantah berhasil ditukar", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to UCOActivity
                }
            }
        });
    }
}
