package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class KirimBarangActivity extends AppCompatActivity {

    private EditText etPengirim, etPenerima, etBerat;
    private RadioGroup rgLayanan;
    private TextView addressTextView, destinationTextView;
    private Button btnLanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirim_barang);

        // Inisialisasi view
        addressTextView = findViewById(R.id.addressTextView);
        destinationTextView = findViewById(R.id.destinationTextView);
        etPengirim = findViewById(R.id.et_pengirim);
        etPenerima = findViewById(R.id.et_penerima);
        etBerat = findViewById(R.id.et_berat);
        rgLayanan = findViewById(R.id.rg_layanan);
        btnLanjut = findViewById(R.id.btn_lanjut);


        // Mengambil data dari Intent
        // Ambil data "address" dan "destination" dari Intent yang dikirimkan
        String addressFromPreviousActivity = getIntent().getStringExtra("address");
        String destinationFromPreviousActivity = getIntent().getStringExtra("destination");
        String address = getIntent().getStringExtra("address");
        String destination = getIntent().getStringExtra("destination");
        String selectionInfo = getIntent().getStringExtra("selection_info");

        addressTextView.setText(address);
        destinationTextView.setText(destination);

        // Tombol Lanjut
        btnLanjut.setOnClickListener(v -> {
            String pengirim = etPengirim.getText().toString();
            String penerima = etPenerima.getText().toString();
            String beratStr = etBerat.getText().toString();
            int layananId = rgLayanan.getCheckedRadioButtonId();

            if (pengirim.isEmpty() || penerima.isEmpty() || beratStr.isEmpty() || layananId == -1) {
                Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            double berat = Double.parseDouble(beratStr);
            String layanan = ((RadioButton) findViewById(layananId)).getText().toString();
            double ongkir = hitungOngkir(berat, layanan);

            // Kirim data ke halaman ringkasan
            Intent intent = new Intent(KirimBarangActivity.this, PaymentKirimBarangActivity.class);
            intent.putExtra("pengirim", pengirim);
            intent.putExtra("penerima", penerima);
            intent.putExtra("address", addressFromPreviousActivity);
            intent.putExtra("destination", destinationFromPreviousActivity);
            intent.putExtra("berat", berat);
            intent.putExtra("layanan", layanan);
            intent.putExtra("ongkir", ongkir);
            intent.putExtra("selection_info", selectionInfo);
            startActivity(intent);
        });
    }

    private double hitungOngkir(double berat, String layanan) {
        double ongkir;
        if (berat < 5) {
            ongkir = 15000;
        } else if (berat <= 10) {
            ongkir = 25000;
        } else {
            ongkir = 45000;
        }

        // Tambahkan biaya layanan
        if ("Reguler".equalsIgnoreCase(layanan)) {
            ongkir += 15000;
        } else if ("Ekspres".equalsIgnoreCase(layanan)) {
            ongkir += 25000;
        }

        return ongkir;
    }
}
