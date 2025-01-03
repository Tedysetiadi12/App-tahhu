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
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.List;

public class AgendaWargaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAgenda;
    private FloatingActionButton fabAddAgenda;
    private List<AgendaItem> agendaItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_warga);

        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupFab();
    }

    private void initializeViews() {
        recyclerViewAgenda = findViewById(R.id.recyclerViewAgenda);
        fabAddAgenda = findViewById(R.id.fabAddAgenda);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        agendaItems = new ArrayList<>();
        // Add sample data
        agendaItems.add(new AgendaItem("Arisan Bulanan", "15 Juni 2023", "Rumah Pak RT"));
        agendaItems.add(new AgendaItem("Olahraga Bersama", "20 Juni 2023", "Lapangan Komplek"));
        agendaItems.add(new AgendaItem("Bazar Makanan", "25 Juni 2023", "Balai Warga"));

        AgendaAdapter adapter = new AgendaAdapter(agendaItems);
        recyclerViewAgenda.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAgenda.setAdapter(adapter);
    }

    private void setupFab() {
        fabAddAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAgendaDialog();
            }
        });
    }


        private void showAddAgendaDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_agenda, null);
            builder.setView(dialogView);

            final EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
            final EditText editTextDate = dialogView.findViewById(R.id.editTextDate);
            final EditText editTextLocation = dialogView.findViewById(R.id.editTextLocation);

            builder.setTitle("Tambah Agenda Baru")
                    .setPositiveButton("Tambah", (dialog, id) -> {
                        String title = editTextTitle.getText().toString();
                        String date = editTextDate.getText().toString();
                        String location = editTextLocation.getText().toString();

                        if (!title.isEmpty() && !date.isEmpty() && !location.isEmpty()) {
                            AgendaItem newItem = new AgendaItem(title, date, location);
                            agendaItems.add(newItem);
                            recyclerViewAgenda.getAdapter().notifyItemInserted(agendaItems.size() - 1);
                        }
                    })
                    .setNegativeButton("Batal", (dialog, id) -> dialog.cancel());

            AlertDialog dialog = builder.create();
            dialog.show();
        }

     private static class AgendaItem {
         String title;
         String date;
         String location;

         AgendaItem(String title, String date, String location) {
             this.title = title;
             this.date = date;
             this.location = location;
         }
     }

    public static class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {
        private List<AgendaItem> agendaItems;

        public AgendaAdapter(List<AgendaItem> agendaItems) {
            this.agendaItems = agendaItems;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AgendaItem item = agendaItems.get(position);
            holder.titleTextView.setText(item.title);
            holder.dateTextView.setText(item.date);
            holder.locationTextView.setText(item.location);
        }

        @Override
        public int getItemCount() {
            return agendaItems.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            TextView dateTextView;
            TextView locationTextView;

            ViewHolder(View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.titleTextView);
                dateTextView = itemView.findViewById(R.id.dateTextView);
                locationTextView = itemView.findViewById(R.id.locationTextView);
            }
        }
    }

}

