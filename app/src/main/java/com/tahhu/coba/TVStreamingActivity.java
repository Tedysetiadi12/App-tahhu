package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TVStreamingActivity extends AppCompatActivity {

    private String[] channelNames = {
                "RCTI", "TV One", "Indosiar","inews", "TransTV","MNCTV",
                "NET TV", "Trans 7", "MetroTV", " KompasTV", "antv","gtv"
    };
    private int[] channelImages = {
            R.drawable.rcti, R.drawable.ic_tvone, R.drawable.ic_indosiar,R.drawable.inews,
            R.drawable.ic_transtv,R.drawable.mnctv, R.drawable.nettv, R.drawable.ic_trans7,
            R.drawable.metrotv, R.drawable.kompastv,R.drawable.antv,R.drawable.gtv,
    };
    private String[] channelUrls = {
            "https://rcti-cutv.rctiplus.id/rcti-sdi-avc1_800000=7-mp4a_96000=1.m3u8",
            "", // TV One
            "", // Indosiar

            "", // inews
            "https://video.detik.com/transtv/smil:transtv.smil/index.m3u8",
            "", // MNCTV

            "https://cdn09jtedge.indihometv.com/joss/133/net/index.m3u8?c",
            "https://video.detik.com/trans7/smil:trans7.smil/index.m3u8",
            "https://edge.medcom.id/live-edge/smil:metro.smil/playlist.m3u8",

            "https://cdn09jtedge.indihometv.com/atm/hlsv3/KOMPAS_TV/KOMPAS_TV-avc1_1500000=10-mp4a_96000=2.m3u8",
            "https://cdn09jtedge.indihometv.com/joss/130/antv/index.m3u8",
            "", // Gtv
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_stream);
        GridView gridView = findViewById(R.id.gridview_channels);

        // Set adapter to GridView
        ChannelAdapter adapter = new ChannelAdapter(this, channelNames, channelImages, channelUrls);
        gridView.setAdapter(adapter);

        // Handle click events
        gridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(TVStreamingActivity.this, FullScreenActivity.class);
            intent.putExtra("CHANNEL_URL", channelUrls[position]);
            startActivity(intent);
        });

        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        kebabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        // Inisialisasi btn_back
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            // Menyelesaikan aktivitas saat tombol kembali diklik
            onBackPressed();
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(TVStreamingActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(TVStreamingActivity.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(TVStreamingActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }
}
