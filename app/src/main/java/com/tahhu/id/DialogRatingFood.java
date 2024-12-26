package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class DialogRatingFood extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button giveRateButton;
    private TextView toggleDetailsButton;
    private View closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rating_food);

        ratingBar = findViewById(R.id.ratingBar);
        giveRateButton = findViewById(R.id.giveRateButton);
        closeButton = findViewById(R.id.closeButton);
        giveRateButton.setEnabled(false);
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            giveRateButton.setEnabled(rating > 0);
        });

        giveRateButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            Toast.makeText(this, "Rating submitted: " + rating, Toast.LENGTH_SHORT).show();
            // Here you would typically send the rating to your backend
            Intent intent = new Intent(DialogRatingFood.this, MainActivity.class);
            startActivity(intent);
        });


        closeButton.setOnClickListener(v -> {
            Intent intent = new Intent(DialogRatingFood.this, MainActivity.class);
            startActivity(intent);
        });
    }
}

