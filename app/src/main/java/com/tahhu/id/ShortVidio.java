package com.tahhu.id;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.Player;
import java.util.ArrayList;
import java.util.List;

public class ShortVidio extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<Video> videoList;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_vidio);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
       ImageView btn_back = findViewById(R.id.btn_back_short);
        videoList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        // Simulasi pengambilan video dari server atau URL
        new Handler().postDelayed(() -> {
            videoList.add(new Video("https://www.dropbox.com/scl/fi/wrsu8ah6bz2fr013danab/videoplayback.mp4?rlkey=g8novmmj42btzm3pyeyk2m6bs&st=is79s430&raw=1", 100, 20));
            videoList.add(new Video("https://www.dropbox.com/scl/fi/wrsu8ah6bz2fr013danab/videoplayback.mp4?rlkey=g8novmmj42btzm3pyeyk2m6bs&st=is79s430&raw=1", 100, 20));
            videoList.add(new Video("https://www.dropbox.com/scl/fi/wrsu8ah6bz2fr013danab/videoplayback.mp4?rlkey=g8novmmj42btzm3pyeyk2m6bs&st=is79s430&raw=1", 150, 25));

            videoAdapter = new VideoAdapter(this, videoList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(videoAdapter);

            progressBar.setVisibility(View.GONE);
        }, 2000);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // Ketika RecyclerView berhenti digulir, hentikan video yang sedang diputar
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (videoAdapter != null) {
                        videoAdapter.stopAllVideos(); // Stop video yang sedang diputar
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Deteksi item video yang muncul di layar dan mulai memutarnya
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                // Mulai video untuk item yang muncul di layar
                for (int i = firstVisibleItem; i <= lastVisibleItem; i++) {
                    if (videoAdapter != null) {
                        VideoAdapter.VideoViewHolder viewHolder = (VideoAdapter.VideoViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                        if (viewHolder != null) {
                            // Pastikan video diputar ketika terlihat
                            Player player = viewHolder.playerView.getPlayer();
                            if (player != null && !player.isPlaying()) {
                                player.play();
                            }
                        }
                    }
                }
            }
        });

        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        kebabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    // Metode untuk menampilkan PopupMenu
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(ShortVidio.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(ShortVidio.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(ShortVidio.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Pastikan video dihentikan saat activity ditutup
        if (videoAdapter != null) {
            videoAdapter.stopAllVideos(); // Hentikan semua video
        }
    }
}
