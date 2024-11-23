package com.tahhu.coba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserAddPaymentMethodActivity extends AppCompatActivity {

    // Declare views
    private EditText cardNumberEditText, cardHolderEditText, expiryDateEditText, cvvEditText;
    private Button addPaymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_addpayment_method);

        // Initialize views
        ImageView backButton = findViewById(R.id.btn_back);
        cardNumberEditText = findViewById(R.id.cardNumber);
        cardHolderEditText = findViewById(R.id.cardholderName);
        expiryDateEditText = findViewById(R.id.expiryDate);
        cvvEditText = findViewById(R.id.cvv);
        addPaymentButton = findViewById(R.id.savePaymentMethodButton);

        backButton.setOnClickListener(v -> finish());

        // Set click listener for the add payment button
        addPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPaymentMethod();
            }
        });
    }

    // Method to handle adding payment method
    private void addPaymentMethod() {
        String cardNumber = cardNumberEditText.getText().toString();
        String cardHolder = cardHolderEditText.getText().toString();
        String expiryDate = expiryDateEditText.getText().toString();
        String cvv = cvvEditText.getText().toString();

        // Basic validation checks
        if (cardNumber.isEmpty() || cardHolder.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(UserAddPaymentMethodActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();

        } else {
            // Proceed with saving the payment method or any logic
            Toast.makeText(UserAddPaymentMethodActivity.this, "Payment Method Added", Toast.LENGTH_SHORT).show();
            // Optionally, you can add logic to save the data, for instance, into a database or API call.
            finish(); // Closes the activity and returns to the previous one
        }
    }
}
