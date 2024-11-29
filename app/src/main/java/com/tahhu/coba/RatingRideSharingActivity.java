package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RatingRideSharingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button giveRateButton;
    private TextView toggleDetailsButton;
    private View detailsLayout;
    private View closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_ridesharing);

        ratingBar = findViewById(R.id.ratingBar);
        giveRateButton = findViewById(R.id.giveRateButton);
        toggleDetailsButton = findViewById(R.id.toggleDetailsButton);
        detailsLayout = findViewById(R.id.detailsLayout);
        closeButton = findViewById(R.id.closeButton);

        giveRateButton.setEnabled(false);

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            giveRateButton.setEnabled(rating > 0);
        });

        giveRateButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            Toast.makeText(this, "Rating submitted: " + rating, Toast.LENGTH_SHORT).show();
            // Here you would typically send the rating to your backend
            Intent intent = new Intent(RatingRideSharingActivity.this, MainActivity.class);
            startActivity(intent);
        });

        toggleDetailsButton.setOnClickListener(v -> {
            if (detailsLayout.getVisibility() == View.VISIBLE) {
                detailsLayout.setVisibility(View.GONE);
                toggleDetailsButton.setText("Show details");
            } else {
                detailsLayout.setVisibility(View.VISIBLE);
                toggleDetailsButton.setText("Hide details");
            }
        });

        closeButton.setOnClickListener(v -> {
                Intent intent = new Intent(RatingRideSharingActivity.this, MainActivity.class);
                startActivity(intent);
    });
    }
}

