package com.tahhu.coba;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class TVStreamingActivity extends AppCompatActivity {

    private ExoPlayer exoPlayer;
    private PlayerView playerView;
    private GridView gridView;

    // Data untuk 5 channel
    private String[] channelNames = {"RCTI", "TV One", "Indosiar", "TransTV", "NET TV", "Trans 7"};
    private int[] channelImages = {
            R.drawable.ic_rcti, R.drawable.ic_tvone, R.drawable.ic_indosiar,
            R.drawable.ic_transtv, R.drawable.play, R.drawable.ic_trans7
    };
    private String[] channelUrls = {
            "https://rcdn.rctiplus.id/rcti-sdi-avc1_600000=9-mp4a_96000=1.m3u8?auth_key=1731948795-4faa20d83f0608e25035e729649f03b9-0-e11f7ccb92269b4f02f233af3ce21c09",
            "https://manifest.googlevideo.com/api/manifest/hls_variant/expire/1731953346/ei/Yi47Z9TtKdrg9fwP0IfRuAs/ip/36.72.215.204/id/yNKvkPJl-tg.3/source/yt_live_broadcast/requiressl/yes/xpc/EgVo2aDSNQ%3D%3D/tx/51326655/txs/51326650%2C51326651%2C51326652%2C51326653%2C51326654%2C51326655%2C51326656/hfr/1/playlist_duration/30/manifest_duration/30/maxh/4320/maudio/1/siu/1/spc/qtApAVRmOIBovr4rtbNMQyN1Zm0rVbfKld013yn6U0SWJaQFdX1pVLEPV42KqKtmF5DFk8dnqNEE/vprv/1/go/1/rqh/5/pacing/0/nvgoi/1/keepalive/yes/fexp/51299154%2C51312688%2C51326932%2C51347747/dover/11/itag/0/playlist_type/DVR/sparams/expire%2Cei%2Cip%2Cid%2Csource%2Crequiressl%2Cxpc%2Ctx%2Ctxs%2Chfr%2Cplaylist_duration%2Cmanifest_duration%2Cmaxh%2Cmaudio%2Csiu%2Cspc%2Cvprv%2Cgo%2Crqh%2Citag%2Cplaylist_type/sig/AJfQdSswRQIhAM6kozan35mQC9PJ71b5UHtCjkkMpST8zNDK2THGObnlAiBciU4Kqq3kbp45T5fze9UMoPzL9iy-K1rPGe7HmSr1Sg%3D%3D/file/index.m3u8",
            "",
            "",
            "https://cdn09jtedge.indihometv.com/joss/133/net/index.m3u8?c",
            "https://video.detik.com/trans7/smil:trans7.smil/index.m3u8"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_stream);

        // Inisialisasi PlayerView dan GridView
        playerView = findViewById(R.id.player_view);
        gridView = findViewById(R.id.gridview_channels);

        // Inisialisasi ExoPlayer
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        // Inisialisasi ChannelAdapter dengan data channel
        ChannelAdapter adapter = new ChannelAdapter(this, channelNames, channelImages, channelUrls);
        gridView.setAdapter(adapter);

        // Menangani klik channel
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedChannelUrl = channelUrls[position];

                // Menyiapkan dan memulai streaming
                MediaItem mediaItem = MediaItem.fromUri(selectedChannelUrl);
                exoPlayer.setMediaItem(mediaItem);
                exoPlayer.prepare();
                exoPlayer.play();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Melepaskan ExoPlayer
        exoPlayer.release();
    }
}

