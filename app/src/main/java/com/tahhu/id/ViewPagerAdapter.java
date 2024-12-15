package com.tahhu.id;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Ganti dengan fragmen yang sesuai
        switch (position) {
            case 0:
                return new SemuaFragment(); // Ganti dengan fragmen yang sesuai
            case 1:
                return new BelumBayarFragment(); // Ganti dengan fragmen yang sesuai
            case 2:
                return new LunasFragment(); // Ganti dengan fragmen yang sesuai
            default:
                return new SemuaFragment(); // Ganti dengan fragmen default
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Jumlah tab
    }
}
