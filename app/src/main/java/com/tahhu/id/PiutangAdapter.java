package com.tahhu.id;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PiutangAdapter extends ListAdapter<Piutang, PiutangAdapter.PiutangViewHolder> {

    private DatabaseReference databaseReference;

    private static final DiffUtil.ItemCallback<Piutang> DIFF_CALLBACK = new DiffUtil.ItemCallback<Piutang>() {
        @Override
        public boolean areItemsTheSame(@NonNull Piutang oldItem, @NonNull Piutang newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Piutang oldItem, @NonNull Piutang newItem) {
            return oldItem.equals(newItem);
        }
    };

    public PiutangAdapter() {
        super(DIFF_CALLBACK);
        this.databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("piutang");
    }

    @NonNull
    @Override
    public PiutangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_piutang, parent, false);
        return new PiutangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PiutangViewHolder holder, int position) {
        Piutang piutang = getItem(position);
        holder.bind(piutang, position);
    }

    public class PiutangViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, statusText;
        Button btnBayar;
        ImageView btnEdit, btnHapus;

        public PiutangViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            statusText = itemView.findViewById(R.id.statusText);
            btnBayar = itemView.findViewById(R.id.btnBayar);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnHapus = itemView.findViewById(R.id.btnHapus);
        }
        public void removeItem(Piutang piutang) {
            List<Piutang> currentList = new ArrayList<>(getCurrentList());
            currentList.remove(piutang);
            submitList(currentList);
        }
        public void bind(final Piutang piutang, final int position) {
            nameText.setText(piutang.getNama());
            statusText.setText(piutang.getStatus() + " - " + piutang.getJumlah());

            if ("Lunas".equals(piutang.getStatus())) {
                btnBayar.setVisibility(View.GONE);
            } else {
                btnBayar.setVisibility(View.VISIBLE);
            }

            btnBayar.setOnClickListener(v -> {
                btnBayar.setEnabled(false);
                piutang.setStatus("Lunas");

                databaseReference.child(piutang.getId()).setValue(piutang).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Piutang> updatedList = new ArrayList<>(getCurrentList());
                        updatedList.set(position, piutang);
                        submitList(updatedList);
                        Toast.makeText(v.getContext(), "Piutang dibayar", Toast.LENGTH_SHORT).show();
                    } else {
                        btnBayar.setEnabled(true);
                        Toast.makeText(v.getContext(), "Gagal memperbarui status", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            btnEdit.setOnClickListener(v -> {
                EditPiutangDialogFragment dialogFragment = EditPiutangDialogFragment.newInstance(piutang);
                dialogFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "editPiutangDialog");
            });

            btnHapus.setOnClickListener(v -> {
                databaseReference.child(piutang.getId()).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        removeItem(piutang);
                        Toast.makeText(v.getContext(), "Piutang dihapus", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "Gagal menghapus Piutang", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }

    public void updateList(List<Piutang> newPiutangList) {
        submitList(newPiutangList);
    }
}