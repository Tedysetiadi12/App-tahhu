package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.Date;
import java.util.Locale;

public class ShoppingListActivity extends AppCompatActivity {
    private EditText etItemName, etQuantity, etPrice, etNotes;
    private Button btnSelectCategory, btnAddToList, btnViewCalendar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TextView tvTotalAmount;
    private String selectedCategory = "";

    private ActiveListFragment activeListFragment;
    private CompletedListFragment completedListFragment;

    private double totalCompletedAmount = 0; // Total pengeluaran selesai

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        initializeViews();
        setupViewPager();
        setupListeners();

        activeListFragment = new ActiveListFragment();
        completedListFragment = new CompletedListFragment();
    }

    private void initializeViews() {
        etItemName = findViewById(R.id.etItemName);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        etNotes = findViewById(R.id.etNotes);
        btnSelectCategory = findViewById(R.id.btnSelectCategory);
        btnAddToList = findViewById(R.id.btnAddToList);
//        btnViewCalendar = findViewById(R.id.btnViewCalendar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        updateTotalAmount();
    }

    private void setupViewPager() {
        ShoppingListPagerAdapter adapter = new ShoppingListPagerAdapter(this);
        viewPager.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Belum Selesai"));
        tabLayout.addTab(tabLayout.newTab().setText("Selesai"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    private void setupListeners() {
        btnSelectCategory.setOnClickListener(v -> showCategoryDialog());
        btnAddToList.setOnClickListener(v -> addItemToList());
//        btnViewCalendar.setOnClickListener(v -> openCalendarActivity());
    }

    private void showCategoryDialog() {
        String[] categories = {"Daging", "Ikan", "Bahan Makanan", "Sayuran", "Buah", "Lainnya"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Kategori");

        builder.setSingleChoiceItems(categories, -1, (dialog, which) -> {
            selectedCategory = categories[which];
            btnSelectCategory.setText(selectedCategory);
            dialog.dismiss();
        });

        builder.show();
    }

    private void addItemToList() {
        String name = etItemName.getText().toString();
        String quantityStr = etQuantity.getText().toString();
        String priceStr = etPrice.getText().toString();
        String notes = etNotes.getText().toString();

        if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty() || selectedCategory.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityStr);
        double price = Double.parseDouble(priceStr);

        ShoppingItem item = new ShoppingItem(name, quantity, price, selectedCategory, notes);
        activeListFragment.addItem(item);

        clearInputs();
    }

    private void clearInputs() {
        etItemName.setText("");
        etQuantity.setText("");
        etPrice.setText("");
        etNotes.setText("");
        btnSelectCategory.setText("Pilih Kategori");
        selectedCategory = "";
    }

    private void updateTotalAmount() {
        String formattedAmount = String.format(Locale.getDefault(), "Rp %.2f", totalCompletedAmount);
        tvTotalAmount.setText(formattedAmount);
    }

    public void markItemAsComplete(ShoppingItem item) {
        item.setCompletionDate(new Date());
        completedListFragment.addItem(item);
        totalCompletedAmount += (item.getPrice() * item.getQuantity());
        updateTotalAmount();
        CalenderActivity.addSpending(item);
    }
    public void deleteItem(ShoppingItem item, boolean isCompleted) {
        if (isCompleted) {
            totalCompletedAmount -= (item.getPrice() * item.getQuantity());
            updateTotalAmount();
        }
    }

    private void openCalendarActivity() {
        Intent intent = new Intent(this, CalenderActivity.class);
        startActivity(intent);
    }

    class ShoppingListPagerAdapter extends FragmentStateAdapter {
        public ShoppingListPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return position == 0 ? activeListFragment : completedListFragment;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
