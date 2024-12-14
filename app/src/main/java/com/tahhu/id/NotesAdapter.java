package com.tahhu.id;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notesList;
    private Context context;

    public NotesAdapter(List<Note> notesList, Context context) {
        this.notesList = notesList;
        this.context = context;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.tvTitle.setText(note.getTitle());
        // Use Html.fromHtml to display formatted content
        holder.tvContent.setText(Html.fromHtml(note.getContent()));
        String formattedDate = formatTanggal(note.getUpdatedAt());
        holder.tvUpdatedAt.setText(formattedDate);

        // Array drawable untuk latar belakang
        int[] backgrounds = new int[]{
                R.drawable.note_bg_1,
                R.drawable.note_bg_2,
                R.drawable.note_bg_3
        };

        // Tetapkan latar belakang berdasarkan posisi
        int backgroundIndex = position % backgrounds.length;
        holder.itemView.setBackgroundResource(backgrounds[backgroundIndex]);

        // Set up pin button
        holder.pinButton.setImageResource(note.isPinned() ? R.drawable.pinned : R.drawable.ic_pin);
        holder.pinButton.setOnClickListener(v -> {
            note.setPinned(!note.isPinned());
            if (note.isPinned()) {
                // Move the pinned item to the top
                notesList.remove(position);
                notesList.add(0, note);
            } else {
                // Restore the item to its original position
                notesList.remove(position);
                notesList.add(position, note);
            }
            notifyDataSetChanged(); // Update the RecyclerView after changing the pinned status
            updateNoteInDatabase(note); // Update the status in Firebase
        });


        // Set up delete button
        holder.deleteButton.setOnClickListener(v -> {
            // Konfirmasi penghapusan dengan dialog
            new AlertDialog.Builder(context)
                    .setTitle("Hapus Catatan")
                    .setMessage("Apakah Anda yakin ingin menghapus catatan ini?")
                    .setPositiveButton("Hapus", (dialog, which) -> {
                        deleteNoteFromDatabase(position); // Hapus dari database
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });

        holder.editButton.setOnClickListener(v -> {
            // Calling the method in the activity to handle edit functionality
            ((CatatanActivity) context).onEditNoteClicked(position);
        });

        // Set up view button (for displaying content)
        holder.ViewButton.setOnClickListener(v -> {
            String contentWithImages = notesList.get(position).getContent();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Rincian");
            builder.setMessage(Html.fromHtml(contentWithImages)); // Show images in content
            builder.setPositiveButton("Tutup", null);
            builder.show();
        });

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
    public void setNotes(List<Note> notes) {
        this.notesList = notes;
        notifyDataSetChanged();
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvUpdatedAt;
        ImageButton deleteButton, editButton, ViewButton,pinButton;

        public NoteViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvUpdatedAt = itemView.findViewById(R.id.tvUpdatedAt);
            deleteButton = itemView.findViewById(R.id.btnDelete);
            editButton = itemView.findViewById(R.id.btnEdit);
            ViewButton = itemView.findViewById(R.id.btnViewImage);
            pinButton = itemView.findViewById(R.id.btnPin);

        }
    }

    private String formatTanggal(String isoDate) {
        try {
            // Parsing tanggal ISO-8601
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Date date = isoFormat.parse(isoDate);

            // Format tanggal baru (contoh: "12 Desember 2024")
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return isoDate; // Jika gagal, tampilkan format asli
        }
    }
    private void deleteNoteFromDatabase(int position) {
        Note noteToDelete = notesList.get(position);

        // Lokasi referensi catatan di Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notes").child(noteToDelete.getId());

        databaseReference.removeValue().addOnSuccessListener(aVoid -> {
            // Jika berhasil, hapus dari RecyclerView
            notesList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notesList.size());
        }).addOnFailureListener(e -> {
            // Tampilkan pesan jika gagal
            Toast.makeText(context, "Gagal menghapus catatan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    private void updateNoteInDatabase(Note note) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notes").child(note.getId());
        databaseReference.setValue(note).addOnFailureListener(e -> {
            Toast.makeText(context, "Gagal memperbarui status pin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


}
