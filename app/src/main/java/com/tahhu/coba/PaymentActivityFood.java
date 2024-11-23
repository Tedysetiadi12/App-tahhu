package com.tahhu.coba;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.os.Handler;
import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.List;

public class PaymentActivityFood extends AppCompatActivity {

    private TextView tvFinalData, tvServicesFee, tvFinalTotal;
    private EditText etBillAddress;
    private List<FoodItem> finalItems;
    private int finalTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_food);

        tvServicesFee = findViewById(R.id.tv_services_fee);
        tvFinalTotal = findViewById(R.id.tv_final_total);
        ImageView btnBack = findViewById(R.id.back_to_cekout);

        finalItems = getIntent().getParcelableArrayListExtra("finalItems");
        finalTotal = getIntent().getIntExtra("finalTotal", 0);

        displayFinalData();

        Button btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> {
            if (!finalItems.isEmpty()) {
                // Tampilkan dialog animasi centang
                showSuccessDialog();
            } else {
                // Jika tidak ada item, beri notifikasi error
                Toast.makeText(PaymentActivityFood.this, "Tidak ada item untuk diproses.", Toast.LENGTH_SHORT).show();
            }
        });

        // Menangani klik pada tombol kembali
        btnBack.setOnClickListener(v -> {
            // Menyelesaikan aktivitas saat tombol kembali diklik
            onBackPressed();
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
            Intent intent = new Intent(PaymentActivityFood.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Menutup aktivitas saat ini
        }, 2000); // Delay selama 2 detik
    }

    private void displayFinalData() {
        LinearLayout parentLayout = findViewById(R.id.parent_final_data); // LinearLayout untuk menampung item
        parentLayout.removeAllViews(); // Kosongkan layout sebelumnya
        int serviceFee = 30000;  // Set biaya layanan
        int totalWithServiceFee = finalTotal + serviceFee;  // Hitung total dengan biaya layanan

        for (FoodItem item : finalItems) {
            // Layout horizontal untuk setiap item
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(16, 16, 16, 16);

            // Tambahkan gambar makanan
            ImageView itemImage = new ImageView(this);
            itemImage.setLayoutParams(new LinearLayout.LayoutParams(100, 100)); // Ukuran gambar
            itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            itemImage.setImageResource(item.getImageResId()); // Set gambar dari FoodItem
            itemLayout.addView(itemImage);

            // Layout vertikal untuk nama dan quantity
            LinearLayout textLayout = new LinearLayout(this);
            textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1)); // Bobot 1 untuk memenuhi sisa ruang
            textLayout.setOrientation(LinearLayout.VERTICAL);
            textLayout.setPadding(16, 0, 0, 0); // Padding kiri

            // Nama makanan
            TextView itemName = new TextView(this);
            itemName.setText(item.getName());
            itemName.setTextSize(16);
            itemName.setTextColor(getResources().getColor(R.color.black));
            itemName.setTypeface(ResourcesCompat.getFont(this, R.font.googlesans_medium));
            textLayout.addView(itemName);

            // Quantity
            TextView itemQuantity = new TextView(this);
            itemQuantity.setText("x" + item.getQuantity());
            itemQuantity.setTextSize(14);
            itemQuantity.setTextColor(getResources().getColor(R.color.black));
            itemQuantity.setTypeface(ResourcesCompat.getFont(this, R.font.googlesans_medium));
            textLayout.addView(itemQuantity);

            // Tambahkan layout teks ke layout item
            itemLayout.addView(textLayout);

            // Harga total di kanan
            TextView priceText = new TextView(this);
            priceText.setText("Rp " + (item.getPrice() * item.getQuantity()));
            priceText.setTextSize(14);
            priceText.setTextColor(getResources().getColor(R.color.black));
            priceText.setTypeface(ResourcesCompat.getFont(this, R.font.googlesans_medium));
            priceText.setGravity(Gravity.END | Gravity.CENTER_VERTICAL); // Posisi kanan dan vertikal tengah
            LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            priceParams.gravity = Gravity.END; // Pastikan posisi di kanan
            priceText.setLayoutParams(priceParams);

            // Tambahkan harga ke layout item
            itemLayout.addView(priceText);

            // Tambahkan layout item ke parent layout
            parentLayout.addView(itemLayout);
        }

        // Update biaya layanan dan total akhir
        tvServicesFee.setText("Rp " + serviceFee);
        tvFinalTotal.setText("Rp " + totalWithServiceFee);
    }


}
