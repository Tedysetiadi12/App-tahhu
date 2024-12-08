package com.tahhu.coba;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ShoppingListPagerAdapter extends FragmentStateAdapter {

    public ShoppingListPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new ActiveFragment();
        } else {
            return new CompletedFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
