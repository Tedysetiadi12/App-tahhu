package com.tahhu.coba;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.RadioButton;


public class PaymentActivityMarketplace extends AppCompatActivity {
    private TextView totalPriceView, shippingCostView, finalPriceView;
    private RadioGroup radioGroupPayment;
    private Button btnProceedPayment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_marketplace);


        // Inisialisasi view dari XML
        Button btnProceedPayment = findViewById(R.id.btnProceedPayment);
        ImageView iconArrow = findViewById(R.id.icon_arrow);
        finalPriceView = findViewById(R.id.totalPriceView);
        shippingCostView = findViewById(R.id.shippingCostView);
        totalPriceView = findViewById(R.id.finalPriceView);

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

        btnProceedPayment.setEnabled(false);
        RadioGroup radioGroupPayment = findViewById(R.id.radioGroupPayment);

        // Menambahkan listener untuk tombol konfirmasi pembayaran
        radioGroupPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Mengecek apakah ada RadioButton yang dipilih
                if (checkedId != -1) {
                    // Jika ada yang dipilih, aktifkan tombol Proceed Payment
                    btnProceedPayment.setEnabled(true);
                } else {
                    // Jika tidak ada yang dipilih, tombol tetap dinonaktifkan
                    btnProceedPayment.setEnabled(false);
                }
            }
        });

        // Menambahkan click listener pada btnProceedPayment
        btnProceedPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioId = radioGroupPayment.getCheckedRadioButtonId();
                if (selectedRadioId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioId);
                    String selectedPaymentMethod = selectedRadioButton.getText().toString();
                    Toast.makeText(PaymentActivityMarketplace.this, "Kamu memilih: " + selectedPaymentMethod, Toast.LENGTH_SHORT).show();
                    // Lakukan tindakan selanjutnya, seperti melanjutkan ke halaman pembayaran
                    showPaymentConfirmationDialog();
                } else {
                    // Radio button belum dipilih
                    Toast.makeText(PaymentActivityMarketplace.this, "Pilih metode pembayaran terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestStoragePermission();

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

    }


    private void showPaymentConfirmationDialog() {
        // Inflate custom dialog layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_payment_confirmation, null);
        ImageView imageView = dialogView.findViewById(R.id.imageConfirmation);
        Button btnDownloadInvoice = dialogView.findViewById(R.id.btnDownloadInvoice);
        TextView textMessage = dialogView.findViewById(R.id.textConfirmation);
        Button btnback = dialogView.findViewById(R.id.back_beranda);

        // Set image and message
        imageView.setImageResource(R.drawable.ic_success); // Replace with your image resource
        textMessage.setText("Selamat! Pesanan Anda Berhasil Untuk Dibayar.");

        // Create dialog
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        // Handle download invoice button
        btnDownloadInvoice.setOnClickListener(v -> {
            createInvoicePdf();
        });

        alertDialog.show();

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(PaymentActivityMarketplace.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createInvoicePdf() {
        // Create a new PDF document
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        // Create a page
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Add content to PDF
        paint.setTextSize(12);
        paint.setColor(Color.BLACK);
        canvas.drawText("Invoice", 20, 20, paint);
        try {
            String finalPriceText = finalPriceView.getText().toString().replaceAll("[^\\d.]", ""); // Hanya angka
            double finalPrice = Double.parseDouble(finalPriceText);
            canvas.drawText("Total Price: Rp " + String.format("%,.2f", finalPrice), 20, 50, paint);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error formatting price for PDF", Toast.LENGTH_SHORT).show();
        }
        canvas.drawText("Thank you for your order!", 20, 80, paint);

        pdfDocument.finishPage(page);

        // Save PDF to internal storage
        File directory = new File(getExternalFilesDir(null), "Invoices");
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdirs();
            if (!dirCreated) {
                Toast.makeText(this, "Failed to create directory for invoices", Toast.LENGTH_LONG).show();
                return;
            }
        }

        File file = new File(directory, "invoice.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Invoice saved to: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save invoice: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            pdfDocument.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Tambahkan ini untuk memanggil metode super
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) { // Hanya diperlukan untuk Android 10 ke bawah
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }
}
