package com.tahhu.coba;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;


public class PaymentActivity extends AppCompatActivity {
    private TextView totalPricePayment;
    private RadioGroup radioGroupPayment;
    private EditText editCardNumber, editExpiryDate, editSecurityNumber, editCardHolder;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Inisialisasi view dari XML
        totalPricePayment = findViewById(R.id.totalPricePayment);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        editCardNumber = findViewById(R.id.editCardNumber);
        editExpiryDate = findViewById(R.id.editExpiryDate);
        editSecurityNumber = findViewById(R.id.editSecurityNumber);
        editCardHolder = findViewById(R.id.editCardHolder);
        Button btnProceedPayment = findViewById(R.id.btnProceedPayment);

        // Ambil totalPrice yang dikirimkan dari CartProductActivity
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        totalPricePayment.setText("Rp " + String.format("%,.2f", totalPrice));

        btnProceedPayment.setOnClickListener(v -> {
            int selectedPaymentMethodId = radioGroupPayment.getCheckedRadioButtonId();
            if (selectedPaymentMethodId == -1) {
                Toast.makeText(this, "Please select a payment method.", Toast.LENGTH_SHORT).show();
                return;
            }

            showPaymentConfirmationDialog();
        });

        requestStoragePermission();
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
        textMessage.setText("Congrats! Your order has been placed successfully.");

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
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
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
        canvas.drawText("Total Price: Rp " + String.format("%,.2f", totalPrice), 20, 50, paint);
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
