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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class LayananVolunteerActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVolunteer;
    private FloatingActionButton fabAddVolunteer;
    private List<VolunteerItem> volunteerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan_volunteer);

        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupFab();
    }

    private void initializeViews() {
        recyclerViewVolunteer = findViewById(R.id.recyclerViewVolunteer);
        fabAddVolunteer = findViewById(R.id.fabAddVolunteer);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        volunteerItems = new ArrayList<>();
        // Add sample data
        volunteerItems.add(new VolunteerItem("Donasi Buku", "Donasi buku bekas untuk perpustakaan warga", "20 Juni 2023"));
        volunteerItems.add(new VolunteerItem("Kerja Bakti", "Membersihkan selokan di RT 03", "25 Juni 2023"));
        volunteerItems.add(new VolunteerItem("Pengajar Sukarela", "Mengajar les gratis untuk anak SD", "Setiap Sabtu"));

        VolunteerAdapter adapter = new VolunteerAdapter(volunteerItems);
        recyclerViewVolunteer.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVolunteer.setAdapter(adapter);
    }

    private void setupFab() {
        fabAddVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddVolunteerServiceDialog();
            }
        });
    }

    private void showAddVolunteerServiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_volunteer_service, null);
        builder.setView(dialogView);

        final EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        final EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        final EditText editTextDate = dialogView.findViewById(R.id.editTextDate);

        builder.setTitle("Tambah Layanan Volunteer Baru")
                .setPositiveButton("Tambah", (dialog, id) -> {
                    String title = editTextTitle.getText().toString();
                    String description = editTextDescription.getText().toString();
                    String date = editTextDate.getText().toString();

                    if (!title.isEmpty() && !description.isEmpty() && !date.isEmpty()) {
                        VolunteerItem newItem = new VolunteerItem(title, description, date);
                        volunteerItems.add(newItem);
                        recyclerViewVolunteer.getAdapter().notifyItemInserted(volunteerItems.size() - 1);
                    }
                })
                .setNegativeButton("Batal", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static class VolunteerItem {
        String title;
        String description;
        String date;

        VolunteerItem(String title, String description, String date) {
            this.title = title;
            this.description = description;
            this.date = date;
        }
    }

    // TODO: Implement VolunteerAdapter class for RecyclerView
    public static class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.ViewHolder> {
        private List<VolunteerItem> volunteerItems;

        public VolunteerAdapter(List<VolunteerItem> volunteerItems) {
            this.volunteerItems = volunteerItems;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_volunteer, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            VolunteerItem item = volunteerItems.get(position);
            holder.titleTextView.setText(item.title);
            holder.descriptionTextView.setText(item.description);
            holder.dateTextView.setText(item.date);
        }

        @Override
        public int getItemCount() {
            return volunteerItems.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            TextView descriptionTextView;
            TextView dateTextView;

            ViewHolder(View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.titleTextView);
                descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
                dateTextView = itemView.findViewById(R.id.dateTextView);
            }
        }
    }
}

