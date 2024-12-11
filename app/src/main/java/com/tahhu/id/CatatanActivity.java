package com.tahhu.id;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import jp.wasabeef.richeditor.RichEditor;

public class CatatanActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private LinearLayout formLayout;
    private FloatingActionButton fabAddNote;
    private RichEditor richEditor;
    private EditText etTitle;
    private Button saveButton, battalButton;

    // Toolbar buttons for formatting
    private ImageButton boldButton, italicButton, underlineButton, numberListButton, bulletListButton;
    private boolean isBoldActive = false, isItalicActive = false, isUnderlineActive = false, isNumberListActive = false, isBulletListActive = false;

    private int editingPosition = -1;

    private ImageButton fileButton, photoButton;
    private static final int PICK_FILE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private List<Note> notesList = new ArrayList<>();
    private NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catatan);

        // Initialize views
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.emptyView);
        formLayout = findViewById(R.id.formLayout);
        fabAddNote = findViewById(R.id.fabAddNote);
        richEditor = findViewById(R.id.richEditor);
        etTitle = findViewById(R.id.etTitle);
        saveButton = findViewById(R.id.saveButton);
        battalButton = findViewById(R.id.battalButton);

        // Initialize toolbar buttons
        boldButton = findViewById(R.id.boldButton);
        italicButton = findViewById(R.id.italicButton);
        underlineButton = findViewById(R.id.underlineButton);
        numberListButton = findViewById(R.id.numberListButton);
        bulletListButton = findViewById(R.id.bulletListButton);
        fileButton = findViewById(R.id.fileButton);
        photoButton = findViewById(R.id.photoButton);

// Tambahkan listener untuk file
        fileButton.setOnClickListener(v -> openFilePicker());

// Tambahkan listener untuk foto
        photoButton.setOnClickListener(v -> openImagePicker());


        // Set up RecyclerView
        adapter = new NotesAdapter(notesList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load notes from SharedPreferences
        loadNotes();

        richEditor.setHtml(""); // Set empty content initially
        richEditor.setPlaceholder("Isi catatanmu...");

        fabAddNote.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            fabAddNote.setVisibility(View.GONE);
            editingPosition = -1; // Reset editing position when adding new note
        });

        battalButton.setOnClickListener(v -> {
            formLayout.setVisibility(View.GONE);
            fabAddNote.setVisibility(View.VISIBLE);
        });
        adapter.setNotes(notesList);
        // Save note and hide form
        saveButton.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = richEditor.getHtml();

            if (title.isEmpty() || content == null || content.trim().isEmpty()) {
                Toast.makeText(CatatanActivity.this, "Judul dan Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Hapus elemen gambar dari HTML menggunakan regex
            String contentWithoutImages = content.replaceAll("<img[^>]*>", "");

            // Periksa apakah sedang mengedit atau menambah catatan baru
            if (editingPosition != -1) {
                notesList.get(editingPosition).setTitle(title);
                notesList.add(new Note(title, content));
                    // Simpan tanpa gambar
            } else {
                notesList.add(new Note(title, content));
            }

            saveNotes(); // Save updated notes to SharedPreferences
            adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
            updateViewVisibility(); // Update visibility of views

            // Clear the form and hide it
            etTitle.setText("");
            richEditor.setHtml("");
            formLayout.setVisibility(View.GONE);
            fabAddNote.setVisibility(View.VISIBLE);
        });

        // Set up button actions for formatting
        boldButton.setOnClickListener(v -> toggleBold());
        italicButton.setOnClickListener(v -> toggleItalic());
        underlineButton.setOnClickListener(v -> toggleUnderline());
        numberListButton.setOnClickListener(v -> toggleNumberList());
        bulletListButton.setOnClickListener(v -> toggleBulletList());

        updateViewVisibility();
    }
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Mengizinkan semua tipe file
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // Hanya gambar
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedUri = data.getData();

            if (requestCode == PICK_FILE_REQUEST) {
                // Mendapatkan nama file
                String fileName = getFileName(selectedUri);

                // Menambahkan tautan file dengan nama file
                String currentHtml = richEditor.getHtml();
                String newHtml = currentHtml + "<a href='" + selectedUri.toString() + "'>" + fileName + "</a>";
                richEditor.setHtml(newHtml);
                Toast.makeText(this, "File berhasil ditambahkan!", Toast.LENGTH_SHORT).show();

            } else if (requestCode == PICK_IMAGE_REQUEST) {
                // Menambahkan gambar ke editor
                String currentHtml = richEditor.getHtml();
                String newHtml = currentHtml + "<img src='" + selectedUri.toString() + "' style='max-width: 100%; height: auto;' />";
                richEditor.setHtml(newHtml);
                Toast.makeText(this, "Gambar berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Pemilihan dibatalkan", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper untuk mendapatkan nama file
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }


    // Toggle Bold state and apply to the editor
    private void toggleBold() {
        isBoldActive = !isBoldActive;
        richEditor.setBold();
        updateButtonState(boldButton, isBoldActive);
    }

    // Toggle Italic state and apply to the editor
    private void toggleItalic() {
        isItalicActive = !isItalicActive;
        richEditor.setItalic();
        updateButtonState(italicButton, isItalicActive);
    }

    // Toggle Underline state and apply to the editor
    private void toggleUnderline() {
        isUnderlineActive = !isUnderlineActive;
        richEditor.setUnderline();
        updateButtonState(underlineButton, isUnderlineActive);
    }

    // Toggle Numbered List state and apply to the editor
    private void toggleNumberList() {
        isNumberListActive = !isNumberListActive;
        richEditor.setNumbers();
        updateButtonState(numberListButton, isNumberListActive);
    }

    // Toggle Bullet List state and apply to the editor
    private void toggleBulletList() {
        isBulletListActive = !isBulletListActive;
        richEditor.setBullets();
        updateButtonState(bulletListButton, isBulletListActive);
    }

    // Update button state (active or inactive)
    private void updateButtonState(ImageButton button, boolean isActive) {
        if (isActive) {
            button.setColorFilter(getResources().getColor(R.color.active_button_color)); // Use color for active state
        } else {
            button.setColorFilter(getResources().getColor(R.color.inactive_button_color)); // Use color for inactive state
        }
    }

    // Set up editing mode when an item is clicked
    public void onEditNoteClicked(int position) {
        editingPosition = position;
        Note note = notesList.get(position);
        etTitle.setText(note.getTitle());
        // Simpan konten asli di editor
        String originalContent = note.getContent();

        // Tampilkan gambar kembali (tidak perlu modifikasi di sini, HTML asli dipertahankan)
        richEditor.setHtml(originalContent);
        formLayout.setVisibility(View.VISIBLE);
        fabAddNote.setVisibility(View.GONE);
    }

    // Update visibility based on notes list
    private void updateViewVisibility() {
        if (notesList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    // Save notes list to SharedPreferences
    private void saveNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences("NotesApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert notesList to JSON string using Gson
        Gson gson = new Gson();
        String jsonNotes = gson.toJson(notesList);
        editor.putString("notesList", jsonNotes);
        editor.apply();
        Log.d("NotesApp", "Notes saved: " + jsonNotes);
    }

    // Load notes list from SharedPreferences
    @SuppressLint("NotifyDataSetChanged")
    private void loadNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences("NotesApp", MODE_PRIVATE);
        String jsonNotes = sharedPreferences.getString("notesList", null);

        if (jsonNotes != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Note>>() {}.getType();
            notesList = gson.fromJson(jsonNotes, type);
            adapter.notifyDataSetChanged(); // Notify adapter after loading notes
        }
    }
}
