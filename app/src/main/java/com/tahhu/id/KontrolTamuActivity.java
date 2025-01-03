package com.tahhu.id;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class KontrolTamuActivity extends AppCompatActivity {

    private TextInputEditText etNamaTamu, etNomorKTP, etTujuan;
    private Button btnDaftarTamu;
    private ImageView ivQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontrol_tamu);

        initializeViews();
        setClickListeners();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNamaTamu = findViewById(R.id.etNamaTamu);
        etNomorKTP = findViewById(R.id.etNomorKTP);
        etTujuan = findViewById(R.id.etTujuan);
        btnDaftarTamu = findViewById(R.id.btnDaftarTamu);
        ivQRCode = findViewById(R.id.ivQRCode);
    }

    private void setClickListeners() {
        btnDaftarTamu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftarTamu();
            }
        });
    }

    private void daftarTamu() {
        String namaTamu = etNamaTamu.getText().toString().trim();
        String nomorKTP = etNomorKTP.getText().toString().trim();
        String tujuan = etTujuan.getText().toString().trim();

        if (namaTamu.isEmpty() || nomorKTP.isEmpty() || tujuan.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Implement actual guest registration logic here (e.g., send to server)

        // Generate QR code
        String qrCodeData = "Nama: " + namaTamu + "\nKTP: " + nomorKTP + "\nTujuan: " + tujuan;
        generateQRCode(qrCodeData);

        Toast.makeText(this, "Tamu berhasil didaftarkan", Toast.LENGTH_SHORT).show();

        // Clear input fields
        etNamaTamu.setText("");
        etNomorKTP.setText("");
        etTujuan.setText("");
    }

    private void generateQRCode(String data) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                }
            }
            ivQRCode.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}