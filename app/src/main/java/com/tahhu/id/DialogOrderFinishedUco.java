package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Random;

public class DialogOrderFinishedUco extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order_finished_uco);

        Button btnback = findViewById(R.id.checkBookingButton);
        Button btnOrderAgain = findViewById(R.id.orderAgainButton);
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

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(DialogOrderFinishedUco.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnOrderAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(DialogOrderFinishedUco.this, UcoActivity.class);
                startActivity(intent);
            }
        });
    }
}
