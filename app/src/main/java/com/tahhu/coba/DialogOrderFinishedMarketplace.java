package com.tahhu.coba;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class DialogOrderFinishedMarketplace extends AppCompatActivity {
    private TextView productDetailsTextView;
    private Button downloadPdfButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order_finished_marketplace);

//        productDetailsTextView = findViewById(R.id.productDetailsTextView);
        downloadPdfButton = findViewById(R.id.btnDownloadInvoice);
        Button btnback = findViewById(R.id.checkBookingButton);
        Button btnOrderAgain = findViewById(R.id.orderAgainButton);

        // Terima data dari Intent
        List<CartProduct> cartProductList = (List<CartProduct>) getIntent().getSerializableExtra("cartProductList");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0);
        double finalPrice = getIntent().getDoubleExtra("finalPrice", 0);
        int shippingCost = getIntent().getIntExtra("shippingCost", 0);
        TextView resiTextView = findViewById(R.id.nomor_resi_textview);

        // Generate a random resi number
        Random random = new Random();
        int length = 12;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        String fakeResi = sb.toString();
        resiTextView.setText(fakeResi);

        // Tampilkan detail produk di dialog_order_finished
//        StringBuilder details = new StringBuilder();
//        for (CartProduct cartProduct : cartProductList) {
//            details.append(cartProduct.getProductName())
//                    .append(" x")
//                    .append(cartProduct.getQuantity())
//                    .append(" - ")
//                    .append(cartProduct.getProductPrice())
//                    .append("\n");
//        }
//        details.append("\nShipping Cost: Rp ")
//                .append(String.format("%,.2f", (double) shippingCost))
//                .append("\nTotal Harga: Rp ").append(String.format("%,.2f", totalPrice));
//        productDetailsTextView.setText(details.toString());

        // Tombol untuk mengunduh PDF
        downloadPdfButton.setOnClickListener(v -> {
            generateAndDownloadInvoice(cartProductList, finalPrice, shippingCost);
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(DialogOrderFinishedMarketplace.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnOrderAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(DialogOrderFinishedMarketplace.this, Marketplace.class);
                startActivity(intent);
            }
        });
    }

    private void generateAndDownloadInvoice(List<CartProduct> cartProductList, double finalPrice, int shippingCost) {
        // Membuat dokumen PDF
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        // Halaman pertama
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Random random = new Random();
        int length = 12;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        String fakeResi = sb.toString();

        int y = 50; // Posisi awal Y
        paint.setTextSize(18);
        canvas.drawText("Nomor Resi : " + String.format(fakeResi), 50, y, paint);
        y += 30;

        paint.setTextSize(16);
        canvas.drawText("Invoice", 50, y, paint);
        y += 30;

        // Menulis detail produk
        paint.setTextSize(14);
        for (CartProduct cartProduct : cartProductList) {
            String productName = cartProduct.getProductName();
            String priceString = cartProduct.getProductPrice().replace("Rp", "").replace(",", "").trim();
            double price = Double.parseDouble(priceString);
            int quantity = cartProduct.getQuantity();

            double totalPrices = cartProduct.getQuantity() * price;

            canvas.drawText(
                    productName + " x" + quantity + " - Rp " + String.format("%,.2f", totalPrices),
                    50, y, paint
            );
            y += 20;

        }

        // Menulis total harga dan ongkir
        canvas.drawText("Harga Produk: Rp " + String.format("%,.2f", finalPrice), 50, y, paint);
        y += 20;
        canvas.drawText("Ongkir: Rp " + String.format("%,.2f", (double) shippingCost), 50, y, paint);
        y += 20;
        canvas.drawText("Harga Final: Rp " + String.format("%,.2f", finalPrice + shippingCost), 50, y, paint);

        pdfDocument.finishPage(page);

        // Simpan ke file
        File file = new File(getExternalFilesDir(null), "Invoice.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Invoice berhasil disimpan: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan invoice", Toast.LENGTH_SHORT).show();
        }
        pdfDocument.close();
    }
}
