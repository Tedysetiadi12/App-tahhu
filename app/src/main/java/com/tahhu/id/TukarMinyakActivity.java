package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class TukarMinyakActivity extends AppCompatActivity {
    private EditText edtJumlahMinyak, edtAlamatPenjemputan;
    private RadioGroup radioGroup;
    private MaterialAutoCompleteTextView spinnerLokasi;
    private Button btnTukar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_minyak);

        // Inisialisasi komponen
        edtJumlahMinyak = findViewById(R.id.edtJumlahMinyak);
        edtAlamatPenjemputan = findViewById(R.id.edtAlamatPenjemputan);
        radioGroup = findViewById(R.id.radioGroup);
        spinnerLokasi = findViewById(R.id.spinnerLihatLokasi);
        btnTukar = findViewById(R.id.btnTukar);

        // Atur Adapter untuk spinnerLokasi
        String[] lokasiTerdekat = getResources().getStringArray(R.array.lokasi_terdekat);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line, // Layout bawaan
                lokasiTerdekat
        );
        spinnerLokasi.setAdapter(adapter);

        // Tombol Tukar
        btnTukar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jumlahMinyakStr = edtJumlahMinyak.getText().toString();
                String alamatPenjemputan = edtAlamatPenjemputan.getText().toString();
                int selectedOptionId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedOption = findViewById(selectedOptionId);

                if (jumlahMinyakStr.isEmpty() || alamatPenjemputan.isEmpty()) {
                    Toast.makeText(TukarMinyakActivity.this, "Harap isi semua data!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int jumlahMinyak = Integer.parseInt(jumlahMinyakStr);
                if (jumlahMinyak % 2 != 0 || jumlahMinyak < 2) {
                    Toast.makeText(TukarMinyakActivity.this, "Jumlah minyak harus kelipatan 2 dan minimal 2 liter!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String lokasi = spinnerLokasi.getText().toString();
                String pilihanPenukaran = selectedOption.getText().toString();

                // Kirim data ke halaman payment
                Intent intent = new Intent(TukarMinyakActivity.this, PaymentUcoActivity.class);
                intent.putExtra("jumlahMinyak", jumlahMinyak);
                intent.putExtra("alamatPenjemputan", alamatPenjemputan);
                intent.putExtra("lokasi", lokasi);
                intent.putExtra("pilihanPenukaran", pilihanPenukaran);
                startActivity(intent);
            }
        });
    }
}

