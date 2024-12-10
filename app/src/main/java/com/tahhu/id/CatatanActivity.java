package com.tahhu.id;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;

public class CatatanActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private LinearLayout formLayout;
    private FloatingActionButton fabAddNote;
    private RichEditor richEditor;
    private EditText etTitle;
    private Button saveButton;

    // Toolbar buttons for formatting
    private ImageButton boldButton;
    private ImageButton italicButton;
    private ImageButton underlineButton;
    private ImageButton numberListButton;
    private ImageButton bulletListButton;

    private int editingPosition = -1;

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

        // Initialize toolbar buttons
        boldButton = findViewById(R.id.boldButton);
        italicButton = findViewById(R.id.italicButton);
        underlineButton = findViewById(R.id.underlineButton);
        numberListButton = findViewById(R.id.numberListButton);
        bulletListButton = findViewById(R.id.bulletListButton);

        // Set up RecyclerView
        adapter = new NotesAdapter(notesList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        fabAddNote.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            fabAddNote.setVisibility(View.GONE);
            editingPosition = -1; // Reset editing position when adding new note
        });

        // Show form when FAB is clicked
        fabAddNote.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            fabAddNote.setVisibility(View.GONE);
        });

        // Save note and hide form
        saveButton.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = richEditor.getHtml();

            if (title.isEmpty() || content == null || content.trim().isEmpty()) {
                Toast.makeText(CatatanActivity.this, "Judul dan Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            notesList.add(new Note(title, content));
            adapter.notifyDataSetChanged();
            updateViewVisibility();

            // Clear the form and hide it
            etTitle.setText("");
            richEditor.setHtml("");
            formLayout.setVisibility(View.GONE);
            fabAddNote.setVisibility(View.VISIBLE);
        });

        // Set up button actions
        boldButton.setOnClickListener(v -> richEditor.setBold());
        italicButton.setOnClickListener(v -> richEditor.setItalic());
        underlineButton.setOnClickListener(v -> richEditor.setUnderline());
        numberListButton.setOnClickListener(v -> richEditor.setNumbers());
        bulletListButton.setOnClickListener(v -> richEditor.setBullets());

        updateViewVisibility();


    }
    // Set up editing mode when an item is clicked
    public void onEditNoteClicked(int position) {
        editingPosition = position;
        Note note = notesList.get(position);
        etTitle.setText(note.getTitle());
        richEditor.setHtml(note.getContent());

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
}
