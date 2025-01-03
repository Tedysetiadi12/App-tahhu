package com.tahhu.id;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PengumumanActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private PengumumanPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengumuman);

        initializeViews();
        setupViewPager();
        setupTabLayout();
    }

    private void initializeViews() {


    viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }

    private void setupViewPager() {
        pagerAdapter = new PengumumanPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
    }

    private void setupTabLayout() {
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Pengumuman");
                            break;
                        case 1:
                            tab.setText("Berita");
                            break;
                    }
                }
        );
        mediator.attach();
    }

    // ViewPager Adapter
    private static class PengumumanPagerAdapter extends FragmentStateAdapter {
        public PengumumanPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new PengumumanFragment();
                case 1:
                    return new BeritaFragment();
                default:
                    return new PengumumanFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public static class BeritaFragment extends Fragment {

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                return inflater.inflate(R.layout.fragment_berita, container, false);
            }
        }
    }
}

