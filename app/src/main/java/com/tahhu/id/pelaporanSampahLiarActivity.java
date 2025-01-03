package com.tahhu.id;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class pelaporanSampahLiarActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private EditText inputLokasiSampah, inputDeskripsi;
    private Button btnAddImage, btnlaporkan;
    private ImageView laporanImage;
    private CardView cardViewLaporan;
    private TextView laporanLokasi, laporanDeskripsi, laporanStatus;
    LinearLayout linearLayout;

    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pelaporan_sampah_liar);
        inputLokasiSampah = findViewById(R.id.inputLokasiSampah);
        inputDeskripsi = findViewById(R.id.inputDeskripsi);
        btnAddImage = findViewById(R.id.btnAddImage);
        btnlaporkan = findViewById(R.id.btnLihatJadwal);
        laporanImage = findViewById(R.id.laporanImage);
        cardViewLaporan = findViewById(R.id.cardViewLaporan);
        laporanLokasi = findViewById(R.id.laporanLokasi);
        laporanDeskripsi = findViewById(R.id.laporanDeskripsi);
        laporanStatus = findViewById(R.id.laporanStatus);
        linearLayout = findViewById(R.id.linearLayoutfForm);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Menutup aktivitas
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Gallery or Camera for image
                chooseImage();
            }
        });

        btnlaporkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lokasi = inputLokasiSampah.getText().toString();
                String deskripsi = inputDeskripsi.getText().toString();

                if (lokasi.isEmpty() || deskripsi.isEmpty() || imageUri == null) {
                    Toast.makeText(pelaporanSampahLiarActivity.this, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(pelaporanSampahLiarActivity.this, "Berhasil Melaporkan", Toast.LENGTH_SHORT).show();
                // Show Laporan CardView
                inputLokasiSampah.setText("");
                inputDeskripsi.setText("");
                laporanLokasi.setText("Lokasi: " + lokasi);
                laporanDeskripsi.setText("Deskripsi: " + deskripsi);
                laporanStatus.setText("Status: Sedang Diproses");
                laporanImage.setImageURI(imageUri);
                cardViewLaporan.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }
        });

    }

    private void chooseImage() {
        // Intent to choose image from gallery or camera
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                laporanImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}