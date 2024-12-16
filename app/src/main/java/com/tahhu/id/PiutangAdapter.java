package com.tahhu.id;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class PiutangAdapter extends RecyclerView.Adapter<PiutangAdapter.PiutangViewHolder> {

    private List<Piutang> piutangList;
    private DatabaseReference databaseReference;
    public PiutangAdapter(List<Piutang> piutangList) {
        this.piutangList = piutangList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("piutang");
    }
    @Override
    public PiutangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_piutang, parent, false);
        return new PiutangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PiutangViewHolder holder, int position) {

        if (position < 0 || position >= piutangList.size()) {
            // If position is out of bounds, do not proceed
            return;
        }

        Piutang piutang = piutangList.get(position);
        holder.nameText.setText(piutang.getNama());
        holder.statusText.setText(piutang.getStatus() + " - " + piutang.getJumlah());

        // Hide the "Bayar" button if the status is "Lunas"
        if ("Lunas".equals(piutang.getStatus())) {
            holder.btnBayar.setVisibility(View.GONE);
        }

        holder.btnBayar.setOnClickListener(v -> {
            // Disable the button to prevent multiple clicks during the update
            holder.btnBayar.setEnabled(false);

            // Update the Piutang status to "Lunas"
            piutang.setStatus("Lunas");

            // Update the status in Firebase
            databaseReference.child(piutang.getId()).setValue(piutang).addOnCompleteListener(task -> {
                // This block will ensure we handle UI updates on the main thread
                if (task.isSuccessful()) {
                    // Refresh the list to remove the item from the "Belum Lunas" fragment
                    if (position >= 0 && position < piutangList.size()) {
                        piutangList.remove(position);
                        notifyItemRemoved(position);
                    } else {
                        Log.e("PiutangAdapter", "Invalid position or list is empty");
                    }

                    // Optionally, you can show a success message here
                    Toast.makeText(v.getContext(), "Piutang dibayar", Toast.LENGTH_SHORT).show();
                } else {
                    // In case of failure, re-enable the "Bayar" button
                    holder.btnBayar.setEnabled(true);
                    Toast.makeText(v.getContext(), "Gagal memperbarui status", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Action for "Edit"
        holder.btnEdit.setOnClickListener(v -> {
            // Open edit dialog or activity (implement separately)
            Piutang piutangedit = piutangList.get(position);

            // Show Edit Piutang dialog
            EditPiutangDialogFragment dialogFragment = EditPiutangDialogFragment.newInstance(piutangedit);
            dialogFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "editPiutangDialog");
        });

        // Action for "Hapus"
        holder.btnHapus.setOnClickListener(v -> {
            // Remove Piutang from Firebase and update the list
            databaseReference.child(piutang.getId()).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Check if the position is still valid before removing
                    if (position >= 0 && position < piutangList.size()) {
                        piutangList.remove(position);
                        notifyItemRemoved(position);
                    Toast.makeText(v.getContext(), "Piutang dihapus", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(v.getContext(), "Gagal menghapus Piutang", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Update the list with new data
    public void updateList(List<Piutang> newPiutangList) {
        this.piutangList.clear(); // Clear previous data
        this.piutangList.addAll(newPiutangList); // Add new data
        if (!piutangList.isEmpty()) {
            notifyDataSetChanged(); // Notify the adapter that the data has changed
        } else {
            Log.e("PiutangAdapter", "piutangList is empty!");
        }
    }

    @Override
    public int getItemCount() {
        return piutangList.size();
    }

    public static class PiutangViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, statusText;
        Button btnBayar;
        ImageView btnEdit, btnHapus;

        public PiutangViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            statusText = itemView.findViewById(R.id.statusText);
            btnBayar = itemView.findViewById(R.id.btnBayar);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnHapus = itemView.findViewById(R.id.btnHapus);
        }
    }
}
