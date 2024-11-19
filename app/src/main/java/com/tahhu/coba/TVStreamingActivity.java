package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
            "https://rcdn.rctiplus.id/rcti-sdi-avc1_600000=9-mp4a_96000=1.m3u8?auth_key=1732006757-17cfcf1907aae079a64e330d3fc4f14c-0-bf3ecb092cd71b2761cfa9618c97d067",
            "https://manifest.googlevideo.com/api/manifest/hls_variant/expire/1732010177/ei/YQw8Z5KlHe3T3LUPhP-W-Qs/ip/36.72.215.204/id/yNKvkPJl-tg.3/source/yt_live_broadcast/requiressl/yes/xpc/EgVo2aDSNQ%3D%3D/tx/51326655/txs/51326650%2C51326651%2C51326652%2C51326653%2C51326654%2C51326655%2C51326656/hfr/1/playlist_duration/30/manifest_duration/30/maxh/4320/maudio/1/siu/1/spc/qtApAViu5ppEkYXfCLe7908CyIs8V8GqNFxmWyWZIFvKyC6WsxFvMrjVepsOyhdRUTOQTse_5yOc/vprv/1/go/1/rqh/5/pacing/0/nvgoi/1/keepalive/yes/fexp/51299154%2C51312688%2C51326932%2C51347747/dover/11/itag/0/playlist_type/DVR/sparams/expire%2Cei%2Cip%2Cid%2Csource%2Crequiressl%2Cxpc%2Ctx%2Ctxs%2Chfr%2Cplaylist_duration%2Cmanifest_duration%2Cmaxh%2Cmaudio%2Csiu%2Cspc%2Cvprv%2Cgo%2Crqh%2Citag%2Cplaylist_type/sig/AJfQdSswRgIhAP0gf6HI3CmH7jUZ2sJo5w0j8UAsvPUPPjEkwg48ba5_AiEAzqT7s8naoD8d99R0hWiOPZju3LX3DEM4Lm-zWNUzic4%3D/file/index.m3u8",
            "https://cdn09jtedge.indihometv.com/atm/hlsv3/indosiar/indosiar-avc1_1200000=4-mp4a_96000=2.m3u8",
            "https://icdn.rctiplus.id/inews-sdi-avc1_600000=10-mp4a_96000=1.m3u8?auth_key=1732006993-ec8b4148860f4a820fe5426add45db6d-0-f6a402e59805ea24e28eef399f7b1ad7",
            "https://video.detik.com/transtv/smil:transtv.smil/index.m3u8",
            "https://mcdn.rctiplus.id/mnctv-sdi-avc1_800000=7-mp4a_96000=1.m3u8?auth_key=1732006990-3456226567d2ebc143c988fda1b2d3a4-0-53258d4d1bedda8f59f312c07dbc3c0b",
            "https://cdn09jtedge.indihometv.com/joss/133/net/index.m3u8?c",
            "https://video.detik.com/trans7/smil:trans7.smil/index.m3u8",
            "https://manifest.googlevideo.com/api/manifest/hls_variant/expire/1732011601/ei/8RE8Z_TsJ6eNg8UPt9yTyQ8/ip/36.72.215.204/id/nfgnpM28xDA.1/source/yt_live_broadcast/requiressl/yes/xpc/EgVo2aDSNQ%3D%3D/tx/51326655/txs/51326650%2C51326651%2C51326652%2C51326653%2C51326654%2C51326655%2C51326656/hfr/1/playlist_duration/30/manifest_duration/30/maxh/4320/maudio/1/siu/1/spc/qtApAWw0xYblA7Ln5lhXz2g_z-APvquFfOZEe1Yj2AmHTW1Y0Z21baSH-KqGPZsMDIUm2S3gkxlf/vprv/1/go/1/rqh/5/pacing/0/nvgoi/1/keepalive/yes/fexp/51299154%2C51312688%2C51326932%2C51347747/dover/11/itag/0/playlist_type/DVR/sparams/expire%2Cei%2Cip%2Cid%2Csource%2Crequiressl%2Cxpc%2Ctx%2Ctxs%2Chfr%2Cplaylist_duration%2Cmanifest_duration%2Cmaxh%2Cmaudio%2Csiu%2Cspc%2Cvprv%2Cgo%2Crqh%2Citag%2Cplaylist_type/sig/AJfQdSswRAIgPFR6qyGgBVytK2oHE4MSANyMxYscG44wY5gfFQSdsT0CIAQ8yeyuNfhqFOtUargt7ssWIrYhDzD8TJu406jqnevu/file/index.m3u8",
            "https://manifest.googlevideo.com/api/manifest/hls_variant/expire/1732011391/ei/HxE8Z7rUDejp4t4P66_ZqQs/ip/36.72.215.204/id/DOOrIxw5xOw.1/source/yt_live_broadcast/requiressl/yes/xpc/EgVo2aDSNQ%3D%3D/tx/51326655/txs/51326650%2C51326651%2C51326652%2C51326653%2C51326654%2C51326655%2C51326656/hfr/1/playlist_duration/30/manifest_duration/30/maxh/4320/maudio/1/siu/1/spc/qtApAQeOCgeIKZardYoK7K9w4-o2xRF4lCPAwsOcZ3OQM9CfxocXEr3pReBkJHSo_bFQGxoy4CVB/vprv/1/go/1/rqh/5/pacing/0/nvgoi/1/keepalive/yes/fexp/51299154%2C51312688%2C51326932%2C51347747/dover/11/itag/0/playlist_type/DVR/sparams/expire%2Cei%2Cip%2Cid%2Csource%2Crequiressl%2Cxpc%2Ctx%2Ctxs%2Chfr%2Cplaylist_duration%2Cmanifest_duration%2Cmaxh%2Cmaudio%2Csiu%2Cspc%2Cvprv%2Cgo%2Crqh%2Citag%2Cplaylist_type/sig/AJfQdSswRAIgNmTTwScDaJu7Q6CmJf-7SJ8A68v13ZSBIbAIr4KChkUCIDD3whP6fBH_Fo_2crOr-v61ZbZ4YaL_kJNGdiZ5kv0G/file/index.m3u8"
            ,"https://cdn09jtedge.indihometv.com/joss/130/antv/index.m3u8"
            ,"https://gcdn.rctiplus.id/gtv-sdi-avc1_800000=9-mp4a_96000=1.m3u8?auth_key=1732006991-94e3bfdbeddef82afb5f7da639e97e9e-0-c649fbbef0484555a27780c37e9fa4d4"
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
    }
}
