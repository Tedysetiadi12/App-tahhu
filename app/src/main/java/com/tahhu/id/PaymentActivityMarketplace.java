package com.tahhu.id;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.widget.RadioButton;

import com.google.android.material.bottomsheet.BottomSheetDialog;


public class PaymentActivityMarketplace extends AppCompatActivity implements AddAddressBottomSheet.OnAddressSaveListener {
    private TextView totalPriceView, shippingCostView, finalPriceView;
    private static final int REQUEST_ADD_ADDRESS = 1;
    private TextView addressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_marketplace);

        // Inisialisasi view dari XML
        ImageView iconArrow = findViewById(R.id.icon_arrow);
        finalPriceView = findViewById(R.id.finalPriceView);
        shippingCostView = findViewById(R.id.shippingCostView);
        totalPriceView = findViewById(R.id.totalPriceView);

        // Menerima data dari Intent
        Intent intent = getIntent();
        List<CartProduct> cartProductList =
                (List<CartProduct>) getIntent().getSerializableExtra("cartProductList");

        RecyclerView paymentRecyclerView = findViewById(R.id.paymentRecyclerView);
        PaymentAdapter paymentAdapter = new PaymentAdapter(this, cartProductList);
        paymentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentRecyclerView.setAdapter(paymentAdapter);

        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0);
        int shippingCost = getIntent().getIntExtra("shippingCost", 0);

        shippingCostView.setText("Rp " + String.format("%,d", shippingCost));
        totalPriceView.setText("Rp " + String.format("%,.2f", totalPrice));

        // Menghitung harga final (totalPrice + shippingCost)
        double finalPrice = totalPrice - shippingCost;
        finalPriceView.setText("Rp" + String.format("%,.2f", finalPrice));

        // Inisialisasi RadioGroup dan Button
        RadioGroup radioGroupPayment = findViewById(R.id.radioGroupPayment);
        Button btnProceedPayment = findViewById(R.id.btnProceedPayment);

        // Menambahkan click listener pada btnProceedPayment
        btnProceedPayment.setOnClickListener(v -> {
            int selectedRadioId = radioGroupPayment.getCheckedRadioButtonId();
            if (selectedRadioId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedRadioId);
                String selectedPaymentMethod = selectedRadioButton.getText().toString();
                Toast.makeText(PaymentActivityMarketplace.this, "Kamu memilih: " + selectedPaymentMethod, Toast.LENGTH_SHORT).show();

                // Panggil dialog dengan data produk dan harga akhir
                showSuccessDialog(cartProductList, totalPrice, finalPrice, shippingCost);
            } else {
                Toast.makeText(PaymentActivityMarketplace.this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show();
            }
        });


        iconArrow.setOnClickListener(new View.OnClickListener() {
            private boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                RadioButton rbOne = findViewById(R.id.rb_dana);
                RadioButton rbTwo = findViewById(R.id.rb_gopay);

                if (isExpanded) {
                    // Sembunyikan semua radio button kecuali rbOne dan rbTwo
                    for (int i = 0; i < radioGroupPayment.getChildCount(); i++) {
                        View child = radioGroupPayment.getChildAt(i);
                        if (child != rbOne && child != rbTwo) {
                            child.setVisibility(View.GONE);
                        }
                    }
                    iconArrow.setImageResource(R.drawable.ic_arrowdown); // Ganti ke ikon panah ke bawah
                } else {
                    // Tampilkan semua radio button
                    for (int i = 0; i < radioGroupPayment.getChildCount(); i++) {
                        View child = radioGroupPayment.getChildAt(i);
                        child.setVisibility(View.VISIBLE);
                    }
                    iconArrow.setImageResource(R.drawable.ic_arrowup); // Ganti ke ikon panah ke atas
                }
                isExpanded = !isExpanded;
            }
        });

        LinearLayout btnAddAddress = findViewById(R.id.btnAddAddress);
        addressView = findViewById(R.id.addressView); // TextView untuk menampilkan alamat

        // Klik tombol Tambah Alamat
        btnAddAddress.setOnClickListener(v -> {
            AddAddressBottomSheet bottomSheet = new AddAddressBottomSheet();
            bottomSheet.setOnAddressSaveListener(this); // Set listener ke PaymentActivityMarketplace
            bottomSheet.show(getSupportFragmentManager(), "AddAddressBottomSheet");
        });


        LinearLayout linearLayoutEstimasi = findViewById(R.id.linearLayoutestimasi);
        linearLayoutEstimasi.setOnClickListener(v -> showShippingOptions());


    }

    private void showShippingOptions() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.layout_nama_popup, null);
        LinearLayout pilihanContainer = bottomSheetView.findViewById(R.id.pilihanContainer);

        // Data pengiriman
        String[] jenisPengiriman = {"Standar", "Ekonomi", "Cargo"};
        String[] estimasiPengiriman = {
                "Estimasi Tiba: " + getDateRange(3),
                "Estimasi Tiba: " + getDateRange(4),
                "Estimasi Tiba: " + getDateRange(4)
        };
        int[] hargaPengiriman = {0, 0, 19000};

        for (int i = 0; i < jenisPengiriman.length; i++) {
            View pilihanView = LayoutInflater.from(this).inflate(R.layout.layout_pilihan_item, pilihanContainer, false);

            TextView jenisPengirimanText = pilihanView.findViewById(R.id.jenisPengiriman);
            TextView estimasiPengirimanText = pilihanView.findViewById(R.id.estimasiPengiriman);
            TextView hargaPengirimanText = pilihanView.findViewById(R.id.hargaPengiriman);
            ImageView checkIcon = pilihanView.findViewById(R.id.checkIcon);

            // Set detail untuk setiap item
            jenisPengirimanText.setText(jenisPengiriman[i]);
            estimasiPengirimanText.setText(estimasiPengiriman[i]);
            hargaPengirimanText.setText("Rp " + String.format("%,d", hargaPengiriman[i]));

            // Klik untuk memilih
            int finalI = i;
            pilihanView.setOnClickListener(optionView -> {
                for (int j = 0; j < pilihanContainer.getChildCount(); j++) {
                    View child = pilihanContainer.getChildAt(j);
                    ImageView icon = child.findViewById(R.id.checkIcon);
                    if (child == optionView) {
                        icon.setVisibility(View.VISIBLE); // Tampilkan centang pada opsi yang dipilih
                    } else {
                        icon.setVisibility(View.GONE); // Sembunyikan centang lainnya
                    }
                }

                // Untuk mencoret harga estimasi yang ada sebelumnya
                TextView texhargaestimasi = findViewById(R.id.texhargaestimasi);
                String hargaLama = texhargaestimasi.getText().toString();
                SpannableString spanString = new SpannableString(hargaLama);
                spanString.setSpan(new StrikethroughSpan(), 0, hargaLama.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                texhargaestimasi.setText(spanString);

                // Untuk mengganti teks harga dan estimasi
                TextView texHargaEstimasiDiskon = findViewById(R.id.texhargaestimasidiskon);
                texHargaEstimasiDiskon.setVisibility(finalI == 2 ? View.GONE : View.VISIBLE); // Sembunyikan jika Cargo dipilih

                TextView product = findViewById(R.id.product);

                texhargaestimasi.setText("Rp " + String.format("%,d", hargaPengiriman[finalI]));
                product.setText(estimasiPengiriman[finalI]);
                // Perbarui ongkir dan total harga
                updateShippingCost(hargaPengiriman[finalI]);
                Toast.makeText(this, "Anda memilih: " + jenisPengiriman[finalI], Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss(); // Tutup dialog setelah memilih
            });

            pilihanContainer.addView(pilihanView); // Tambahkan opsi ke container
        }

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void updateShippingCost(int selectedShippingCost) {
        shippingCostView.setText("Rp " + String.format("%,d", selectedShippingCost));
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0);
        double finalPrice = totalPrice + selectedShippingCost;
        finalPriceView.setText("Rp " + String.format("%,.2f", finalPrice));
    }

    private String getDateRange(int daysFromNow) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        calendar.add(Calendar.DAY_OF_YEAR, daysFromNow);
        String startDate = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Tanggal akhir
        String endDate = dateFormat.format(calendar.getTime());
        return startDate + " - " + endDate;
    }

    // Implementasikan listener untuk menerima data alamat
    @Override
    public void onAddressSaved(String recipientName, String city, String district, String address) {
        addressView.setText(String.format("Nama: %s\nAlamat: %s\nKota: %s\nKecamatan: %s", recipientName, address, city, district));
        findViewById(R.id.btnAddAddress).setVisibility(View.GONE);
        addressView.setVisibility(View.VISIBLE);
    }


    private void showSuccessDialog(List<CartProduct> cartProductList, double totalPrice, double finalPrice, int shippingCost) {
        // Buat dialog
        Dialog successDialog = new Dialog(this);
        successDialog.setContentView(R.layout.dialog_payment_success);
        successDialog.setCancelable(false); // Dialog tidak bisa ditutup dengan back button
        successDialog.show();

        // Jalankan delay untuk redirect setelah beberapa detik
        new Handler().postDelayed(() -> {
            successDialog.dismiss(); // Tutup dialog

            // Intent ke DialogOrderFinishedActivity
            Intent intent = new Intent(PaymentActivityMarketplace.this, DialogOrderFinishedMarketplace.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            // Kirim data produk dan harga akhir ke halaman berikutnya
            intent.putExtra("cartProductList", (Serializable) cartProductList);
            intent.putExtra("totalPrice", totalPrice);
            intent.putExtra("finalPrice", finalPrice);
            intent.putExtra("shippingCost", shippingCost);
            startActivity(intent);
            finish(); // Menutup aktivitas saat ini
        }, 1000); // Delay selama 2 detik
    }
}
