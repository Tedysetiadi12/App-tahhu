package com.tahhu.id;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

public class SecurityServices extends AppCompatActivity {
    private ViewPager2 bannerViewPager;
    private TabLayout dotsIndicator;
    private Button btnOfficeSecurityy, btnPersonalSecurity;
    private ImageView btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_services);

        bannerViewPager = findViewById(R.id.bannerViewPager);
        dotsIndicator = findViewById(R.id.dotsIndicator);
        btnOfficeSecurityy = findViewById(R.id.btn_office_security);
        btnPersonalSecurity = findViewById(R.id.btn_personal_security);
        btnHistory = findViewById(R.id.btn_history);

        // Set up banner ViewPager
        List<Integer> banners = Arrays.asList(
                R.drawable.banner1
        );

        BannerAdapter bannerAdapter = new BannerAdapter(banners);
        bannerViewPager.setAdapter(bannerAdapter);

        new TabLayoutMediator(dotsIndicator, bannerViewPager, (tab, position) -> {
            // No specific text needed for dots
        }).attach();

        btnOfficeSecurityy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecurityServices.this, SecurityTypeActivity.class);
                intent.putExtra("securityType", "Kantor");
                startActivity(intent);
            }
        });

        btnPersonalSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecurityServices.this, SecurityTypeActivity.class);
                intent.putExtra("securityType", "Pribadi");
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecurityServices.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}

