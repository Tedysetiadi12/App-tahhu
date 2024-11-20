package com.tahhu.coba;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class SecurityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        findViewById(R.id.btn_office_security).setOnClickListener(v -> openDurationActivity("Pengamanan Kantor"));
        findViewById(R.id.btn_personal_security).setOnClickListener(v -> openDurationActivity("Pengamanan Pribadi"));
        ViewPager2 bannerViewPager = findViewById(R.id.bannerViewPager);
        TabLayout dotsIndicator = findViewById(R.id.dotsIndicator);

        List<Integer> banners = Arrays.asList(
                R.drawable.banner1,
                R.drawable.benner1
        );

        BannerAdapter bannerAdapter = new BannerAdapter(banners);
        bannerViewPager.setAdapter(bannerAdapter);

        new TabLayoutMediator(dotsIndicator, bannerViewPager, (tab, position) -> {
            // No specific text needed for dots
        }).attach();
    }

    private void openDurationActivity(String securityType) {
        Intent intent = new Intent(this, DurationActivity.class);
        intent.putExtra("SECURITY_TYPE", securityType);
        startActivity(intent);
    }
}
