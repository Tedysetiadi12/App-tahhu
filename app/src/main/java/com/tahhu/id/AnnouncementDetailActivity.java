package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.button.MaterialButton;

public class AnnouncementDetailActivity extends AppCompatActivity {

    private TextView announcementTitle;
    private Chip categoryChip;
    private TextView dateText;
    private TextView announcementContent;
    private MaterialButton shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_detail);

        announcementTitle = findViewById(R.id.announcementTitle);
        categoryChip = findViewById(R.id.categoryChip);
        dateText = findViewById(R.id.dateText);
        announcementContent = findViewById(R.id.announcementContent);

        shareButton = findViewById(R.id.shareButton);

        // Mengambil data yang diteruskan dari Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String category = intent.getStringExtra("category");
        String timestamp = intent.getStringExtra("timestamp");
        String content = intent.getStringExtra("content");

        // Menampilkan data di UI
        announcementTitle.setText(title);
        categoryChip.setText(category);
        dateText.setText(timestamp);
        announcementContent.setText(content);

        // Menambahkan listener untuk tombol bagikan
        shareButton.setOnClickListener(v -> {
            String shareText = title + "\n" + content;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Bagikan melalui"));
        });
    }
}
