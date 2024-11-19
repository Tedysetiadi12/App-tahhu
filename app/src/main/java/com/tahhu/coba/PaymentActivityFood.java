package com.tahhu.coba;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.os.Handler;
import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;
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

        tvFinalData = findViewById(R.id.tv_final_data);
        tvServicesFee = findViewById(R.id.tv_services_fee);
        tvFinalTotal = findViewById(R.id.tv_final_total);

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
        StringBuilder dataBuilder = new StringBuilder();
        int serviceFee = 30000;  // Set service fee amount
        int totalWithServiceFee = finalTotal + serviceFee;  // Calculate the total including service fee

        // Display the items in the final list
        for (FoodItem item : finalItems) {
            dataBuilder.append(item.getName()).append(" x").append(item.getQuantity())
                    .append(" = Rp ").append(item.getPrice() * item.getQuantity()).append("\n");
        }

        // Set final data and service fee
        tvFinalData.setText(dataBuilder.toString());
        tvServicesFee.setText("Rp " + serviceFee);

        // Set the final total including the service fee
        tvFinalTotal.setText("Rp " + totalWithServiceFee);
    }
}
