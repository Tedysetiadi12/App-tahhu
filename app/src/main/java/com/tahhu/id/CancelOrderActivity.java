package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CancelOrderActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private EditText etOtherReason;
    private Button btnSubmit;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);

        // Initialize views
        radioGroup = findViewById(R.id.radioGroup);
        etOtherReason = findViewById(R.id.etOtherReason);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        // Back button click listener
        btnBack.setOnClickListener(v -> finish());

        // Submit button click listener
        btnSubmit.setOnClickListener(v -> showCancelConfirmationDialog());
    }

    private void showCancelConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_cancel_confirmation, null);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Find the OK button in dialog
        Button btnOk = dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            // Navigate back to FoodOrder activity
            Intent intent = new Intent(CancelOrderActivity.this, FoodActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear activity stack
            startActivity(intent);
            finish();
        });

        dialog.show();
    }
}