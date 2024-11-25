package com.tahhu.coba;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PaymentUcoActivity extends AppCompatActivity {

    private TextView txtJumlahMinyak, txtKategoriTukar, txtLokasi, txtTotalFee, txtFee, txtService;
    private RadioGroup rgPaymentOptions;
    private RadioButton rbPickup, rbDelivery;
    private EditText edtDeliveryAddress;

    private Button btnConfirmPayment;

    private static final int SERVICE_FEE = 10000;
    private static final int DELIVERY_CHARGE = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_uco);

        // Inisialisasi komponen
        txtJumlahMinyak = findViewById(R.id.txtJumlahMinyak);
        txtKategoriTukar = findViewById(R.id.txtKategoriTukar);
        txtLokasi = findViewById(R.id.txtLokasi);
        txtTotalFee = findViewById(R.id.txtTotalFee);
        txtFee = findViewById(R.id.txtFee);
        txtService = findViewById(R.id.txtService);
        rgPaymentOptions = findViewById(R.id.rgPaymentOptions);
        rbPickup = findViewById(R.id.rbPickup);
        rbDelivery = findViewById(R.id.rbDelivery);
        edtDeliveryAddress = findViewById(R.id.edtDeliveryAddress);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        // Ambil data dari Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String jumlahMinyak = extras.getString("jumlahMinyak");
            String kategoriTukar = extras.getString("kategoriTukar");
            String lokasi = extras.getString("lokasi");

            txtJumlahMinyak.setText(jumlahMinyak + " liter");
            txtKategoriTukar.setText(kategoriTukar);
            txtLokasi.setText(lokasi);
        } else {
            Toast.makeText(this, "Data tidak tersedia!", Toast.LENGTH_SHORT).show();
        }

        // Listener untuk RadioGroup
        rgPaymentOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbPickup) {
                edtDeliveryAddress.setVisibility(View.GONE); // Sembunyikan EditText
                txtTotalFee.setText("Rp. " + SERVICE_FEE);
                txtFee.setText("Rp. " + SERVICE_FEE);
                txtService.setText("Rp. " + 0);

            } else if (checkedId == R.id.rbDelivery) {
                edtDeliveryAddress.setVisibility(View.VISIBLE); // Tampilkan EditText
                txtTotalFee.setText("Rp. " + (SERVICE_FEE + DELIVERY_CHARGE));
                txtFee.setText("Rp. " + SERVICE_FEE);
                txtService.setText("Rp. " + DELIVERY_CHARGE);
            }
        });


        // Listener untuk Tombol Konfirmasi
        btnConfirmPayment.setOnClickListener(v -> {
            if (rgPaymentOptions.getCheckedRadioButtonId() == -1) {
                Toast.makeText(PaymentUcoActivity.this, "Pilih metode pembayaran terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (rbDelivery.isChecked()) {
                String alamat = edtDeliveryAddress.getText().toString();
                if (alamat.isEmpty()) {
                    Toast.makeText(PaymentUcoActivity.this, "Masukkan alamat untuk pengantaran", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            String metodePembayaran = (rbPickup.isChecked()) ? "Ambil Sendiri" : "Diantar";
            showSuccessDialog();
        });
    }

    private void showSuccessDialog() {
        // Buat dialog
        Dialog successDialog = new Dialog(this);
        successDialog.setContentView(R.layout.dialog_payment_success);
        successDialog.setCancelable(false); // Dialog tidak bisa ditutup dengan back button

        // Tampilkan dialog
        successDialog.show();

        // Jalankan delay untuk redirect setelah beberapa detik
        new Handler().postDelayed(() -> {
            successDialog.dismiss(); // Tutup dialog
            // Redirect ke halaman Home
            Intent intent = new Intent(PaymentUcoActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Menutup aktivitas saat ini
        }, 2000); // Delay selama 2 detik
    }
}
