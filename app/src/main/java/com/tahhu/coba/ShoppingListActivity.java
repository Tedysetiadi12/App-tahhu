package com.tahhu.coba;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        ImageView searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });
        // Set up ViewPager with fragments
        ShoppingListPagerAdapter pagerAdapter = new ShoppingListPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // TabLayoutMediator to link TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Active");
            } else {
                tab.setText("Completed");
            }
        }).attach();
    }
    private void showSearchDialog() {
        // Inflate custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_search, null);
        EditText searchEditText = dialogView.findViewById(R.id.search_edit_text);

        // Build AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Search")
                .setView(dialogView)
                .setPositiveButton("Search", (dialog, which) -> {
                    String query = searchEditText.getText().toString();
                    if (!query.isEmpty()) {
                        performSearch(query);
                    } else {
                        Toast.makeText(this, "Search query cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performSearch(String query) {
        // Handle search logic here
        Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_SHORT).show();
        // Example: Filter products based on the query
    }
}
