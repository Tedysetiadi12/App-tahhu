package com.tahhu.coba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OrderPenjemputanActivity extends AppCompatActivity {

    private EditText edtAlamatPenjemputan;
    private Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_penjemputan);

        // Tombol kembali
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke halaman sebelumnya
            }
        });

        // Initialize views
        edtAlamatPenjemputan = findViewById(R.id.edtAlamatPenjemputan);
        btnOrder = findViewById(R.id.btnOrder);

        // Set button click listener
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alamat = edtAlamatPenjemputan.getText().toString().trim();

                // Validate input
                if (alamat.isEmpty()) {
                    Toast.makeText(OrderPenjemputanActivity.this, "Harap masukkan alamat penjemputan", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle order action (e.g., save order to database or call API)
                    Toast.makeText(OrderPenjemputanActivity.this, "Penjemputan berhasil dipesan", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to UCOActivity
                }
            }
        });
    }
}
