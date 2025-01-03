package com.tahhu.id;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MarketplaceWargasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMarketplace;
    private FloatingActionButton fabAddListing;
    public List<MarketplaceItem> marketplaceItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace_wargas);

        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupFab();
    }

    private void initializeViews() {
        recyclerViewMarketplace = findViewById(R.id.recyclerViewMarketplace);
        fabAddListing = findViewById(R.id.fabAddListing);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        marketplaceItems = new ArrayList<>();
        // Add sample data
        marketplaceItems.add(new MarketplaceItem("Sepeda Bekas", "Rp 500.000", "Sepeda gunung bekas pakai, masih mulus"));
        marketplaceItems.add(new MarketplaceItem("Jasa Potong Rumput", "Rp 100.000", "Potong rumput halaman rumah"));
        marketplaceItems.add(new MarketplaceItem("Kue Kering Lebaran", "Rp 50.000", "Aneka kue kering untuk lebaran"));

        MarketplaceAdapter adapter = new MarketplaceAdapter(marketplaceItems);
        recyclerViewMarketplace.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewMarketplace.setAdapter(adapter);
    }

    private void setupFab() {
        fabAddListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMarketplaceItemDialog();
            }
        });
    }

    private void showAddMarketplaceItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_marketplace_item, null);
        builder.setView(dialogView);

        final EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        final EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        final EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);

        builder.setTitle("Tambah Item Baru")
                .setPositiveButton("Tambah", (dialog, id) -> {
                    String title = editTextTitle.getText().toString();
                    String price = editTextPrice.getText().toString();
                    String description = editTextDescription.getText().toString();

                    if (!title.isEmpty() && !price.isEmpty() && !description.isEmpty()) {
                        MarketplaceItem newItem = new MarketplaceItem(title, price, description);
                        marketplaceItems.add(newItem);
                        recyclerViewMarketplace.getAdapter().notifyItemInserted(marketplaceItems.size() - 1);
                    }
                })
                .setNegativeButton("Batal", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static class MarketplaceItem {
        String title;
        String price;
        String description;

        public MarketplaceItem(String title, String price, String description) {
            this.title = title;
            this.price = price;
            this.description = description;
        }
    }


    public static class MarketplaceAdapter extends RecyclerView.Adapter<MarketplaceAdapter.ViewHolder> {
        private List<MarketplaceItem> marketplaceItems;

        public MarketplaceAdapter(List<MarketplaceItem> marketplaceItems) {
            this.marketplaceItems = marketplaceItems;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_marketplace, parent, false);
            return new ViewHolder(view);
        }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MarketplaceItem item = marketplaceItems.get(position);
            holder.titleTextView.setText(item.title);
            holder.priceTextView.setText(item.price);
            holder.descriptionTextView.setText(item.description);
        }

        @Override
        public int getItemCount() {
            return marketplaceItems.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            TextView priceTextView;
            TextView descriptionTextView;

            ViewHolder(View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.titleTextView);
                priceTextView = itemView.findViewById(R.id.priceTextView);
                descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            }
        }
    }

}

