package com.tahhu.id;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;
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

        // Set up delete button
        holder.deleteButton.setOnClickListener(v -> {
            notesList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notesList.size());
        });
        holder.editButton.setOnClickListener(v -> {
            // Calling the method in the activity to handle edit functionality
            ((CatatanActivity) context).onEditNoteClicked(position);
        });

        holder.ViewButton.setOnClickListener(v -> {
            String contentWithImages = notesList.get(position).getContent();

            // Tampilkan elemen gambar kembali di dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Lihat Gambar");
            builder.setMessage(Html.fromHtml(contentWithImages)); // Tampilkan dengan gambar
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
        TextView tvTitle, tvContent;
        ImageButton deleteButton, editButton, ViewButton;

        public NoteViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            deleteButton = itemView.findViewById(R.id.btnDelete);
            editButton = itemView.findViewById(R.id.btnEdit);
            ViewButton = itemView.findViewById(R.id.btnViewImage);

        }
    }
}