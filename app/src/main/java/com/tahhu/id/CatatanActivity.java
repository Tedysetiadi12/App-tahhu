package com.tahhu.id;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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

    private ImageButton boldButton, italicButton, underlineButton, numberListButton, bulletListButton;
    private boolean isBoldActive = false, isItalicActive = false, isUnderlineActive = false, isNumberListActive = false, isBulletListActive = false;

    private ImageButton fileButton, photoButton;
    private static final int PICK_FILE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private List<Note> notesList = new ArrayList<>();
    private NotesAdapter adapter;

    private DatabaseReference notesReference;
    private ImageView homeButton, menuMarket, shortVideo, calculator;
    private FloatingActionButton market;

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

        boldButton = findViewById(R.id.boldButton);
        italicButton = findViewById(R.id.italicButton);
        underlineButton = findViewById(R.id.underlineButton);
        numberListButton = findViewById(R.id.numberListButton);
        bulletListButton = findViewById(R.id.bulletListButton);
        fileButton = findViewById(R.id.fileButton);
        photoButton = findViewById(R.id.photoButton);
        richEditor.setHtml("<p style='color: #aaa;'>Isi catatanmu...</p>");
        // Set placeholder text inside RichEditor
        richEditor.setHtml("<p style='color: #aaa;'>Isi catatanmu...</p>");

        homeButton = findViewById(R.id.homeButton);
        menuMarket = findViewById(R.id.menumarket);
        shortVideo = findViewById(R.id.shortvidio);
        calculator = findViewById(R.id.Kalkulator);
        market = findViewById(R.id.Market);

        // Set click listeners
        homeButton.setOnClickListener(v -> navigateTo("home"));
        menuMarket.setOnClickListener(v -> updateActiveIcon("market"));
        shortVideo.setOnClickListener(v -> navigateTo("videos"));
        calculator.setOnClickListener(v -> navigateTo("calculator"));
        market.setOnClickListener(v -> navigateTo("cart"));

        // Add listener to detect if user starts typing
        richEditor.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // If editor is focused, check if the placeholder is present, and if so, remove it
                if (richEditor.getHtml().equals("<p style='color: #aaa;'>Isi catatanmu...</p>")) {
                    richEditor.setHtml(""); // Clear placeholder
                }
            } else {
                // If the editor loses focus and is empty, set the placeholder back
                if (richEditor.getHtml().isEmpty()) {
                    richEditor.setHtml("<p style='color: #aaa;'>Isi catatanmu...</p>");
                }
            }
        });
        // Initialize Firebase reference
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        notesReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("notes");

        fileButton.setOnClickListener(v -> openFilePicker());
        photoButton.setOnClickListener(v -> openImagePicker());

        adapter = new NotesAdapter(notesList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAddNote.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            fabAddNote.setVisibility(View.GONE);
        });

        battalButton.setOnClickListener(v -> {
            formLayout.setVisibility(View.GONE);
            fabAddNote.setVisibility(View.VISIBLE);
        });

        saveButton.setOnClickListener(v -> saveNoteToFirebase());

        boldButton.setOnClickListener(v -> toggleBold());
        italicButton.setOnClickListener(v -> toggleItalic());
        underlineButton.setOnClickListener(v -> toggleUnderline());
        numberListButton.setOnClickListener(v -> toggleNumberList());
        bulletListButton.setOnClickListener(v -> toggleBulletList());

        updateViewVisibility();
        loadNotesFromFirebase();
    }
    private void loadNotesFromFirebase() {
        notesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notesList.clear(); // Bersihkan daftar lama

                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class); // Konversi ke objek Note
                    if (note != null) {
                        notesList.add(note);
                    }
                }

                adapter.notifyDataSetChanged(); // Perbarui adapter
                updateViewVisibility(); // Perbarui tampilan jika data kosong
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CatatanActivity.this, "Gagal memuat catatan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNoteToFirebase() {
        String title = etTitle.getText().toString().trim();
        String content = richEditor.getHtml();

        if (title.isEmpty() || content == null || content.trim().isEmpty()) {
            Toast.makeText(this, "Judul dan Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        String noteId = UUID.randomUUID().toString();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(new Date());

        Note note = new Note(noteId, title, content, timestamp, timestamp, false);

        notesReference.child(noteId).setValue(note)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CatatanActivity.this, "Catatan berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    notesList.add(note);
                    adapter.notifyDataSetChanged();
                    updateViewVisibility();
                    clearForm();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CatatanActivity.this, "Gagal menyimpan catatan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    public void onEditNoteClicked(int position) {
        // Ambil catatan yang dipilih
        Note selectedNote = notesList.get(position);

        // Isi form dengan data catatan yang akan diedit
        etTitle.setText(selectedNote.getTitle());
        richEditor.setHtml(selectedNote.getContent());

        // Tampilkan form
        formLayout.setVisibility(View.VISIBLE);
        fabAddNote.setVisibility(View.GONE);

        // Update tombol simpan untuk memperbarui catatan
        saveButton.setOnClickListener(v -> {
            String updatedTitle = etTitle.getText().toString().trim();
            String updatedContent = richEditor.getHtml();
            if (updatedTitle.isEmpty() || updatedContent == null || updatedContent.trim().isEmpty()) {
                Toast.makeText(this, "Judul dan isi catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(new Date());

            // Update catatan
            selectedNote.setTitle(updatedTitle);
            selectedNote.setContent(updatedContent);
            selectedNote.setUpdatedAt(timestamp);

            // Perbarui catatan di Firebase
            notesReference.child(selectedNote.getId()).setValue(selectedNote)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Catatan berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                        notesList.set(position, selectedNote); // Update list lokal
                        adapter.notifyItemChanged(position);
                        clearForm();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Gagal memperbarui catatan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedUri = data.getData();

            if (requestCode == PICK_FILE_REQUEST) {
                String fileName = getFileName(selectedUri);
                String currentHtml = richEditor.getHtml();
                String newHtml = currentHtml + "<a href='" + selectedUri.toString() + "'>" + fileName + "</a>";
                richEditor.setHtml(newHtml);
                Toast.makeText(this, "File berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            } else if (requestCode == PICK_IMAGE_REQUEST) {
                String currentHtml = richEditor.getHtml();
                String newHtml = currentHtml + "<img src='" + selectedUri.toString() + "' style='max-width: 100%; height: auto;' />";
                richEditor.setHtml(newHtml);
                Toast.makeText(this, "Gambar berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Pemilihan dibatalkan", Toast.LENGTH_SHORT).show();
        }
    }

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

    private void toggleBold() {
        isBoldActive = !isBoldActive;
        richEditor.setBold();
        updateButtonState(boldButton, isBoldActive);
    }

    private void toggleItalic() {
        isItalicActive = !isItalicActive;
        richEditor.setItalic();
        updateButtonState(italicButton, isItalicActive);
    }

    private void toggleUnderline() {
        isUnderlineActive = !isUnderlineActive;
        richEditor.setUnderline();
        updateButtonState(underlineButton, isUnderlineActive);
    }

    private void toggleNumberList() {
        isNumberListActive = !isNumberListActive;
        richEditor.setNumbers();
        updateButtonState(numberListButton, isNumberListActive);
    }

    private void toggleBulletList() {
        isBulletListActive = !isBulletListActive;
        richEditor.setBullets();
        updateButtonState(bulletListButton, isBulletListActive);
    }

    private void updateButtonState(ImageButton button, boolean isActive) {
        if (isActive) {
            button.setColorFilter(getResources().getColor(R.color.active_button_color));
        } else {
            button.setColorFilter(getResources().getColor(R.color.inactive_button_color));
        }
    }

    private void updateViewVisibility() {
        if (notesList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void clearForm() {
        etTitle.setText("");
        richEditor.setHtml("<p style='color: #aaa;'>Isi catatanmu...</p>");
        formLayout.setVisibility(View.GONE);
        fabAddNote.setVisibility(View.VISIBLE);
    }

    private void navigateTo(String destination) {
        Intent intent;
        switch (destination) {
            case "home":
                intent = new Intent(this, MainActivity.class);
                break;
            case "videos":
                intent = new Intent(this, ShortVidio.class);
                break;
            case "calculator":
                // Show calculator dialog
                DiscountCalculatorDialog dialog = new DiscountCalculatorDialog();
                dialog.show(getSupportFragmentManager(), "DiscountCalculatorDialog");
                return;
            case "cart":
                intent = new Intent(this, CartProductActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
        if (!destination.equals("market")) {
            finish(); // Close this activity if navigating away
        }
    }
    private void updateActiveIcon(String activeIcon) {
        int activeColor = getResources().getColor(R.color.white);
        int inactiveColor = getResources().getColor(R.color.inactive_icon);

        homeButton.setColorFilter(activeIcon.equals("home") ? activeColor : inactiveColor);
        menuMarket.setColorFilter(activeIcon.equals("market") ? activeColor : inactiveColor);
        shortVideo.setColorFilter(activeIcon.equals("videos") ? activeColor : inactiveColor);
        calculator.setColorFilter(activeIcon.equals("calculator") ? activeColor : inactiveColor);
        // Note: We don't change the color of the FloatingActionButton (market) as it's always highlighted
    }

    public static class DiscountCalculatorDialog extends DialogFragment {
        private EditText etPrice, etDiscount;
        private TextView tvInitialPrice, tvDiscount, tvFinalPrice;
        private Button btnReset;
        private TextWatcher textWatcher;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_diskon_kalkulator, container, false);

            // Initialize views
            etPrice = view.findViewById(R.id.etPrice);
            etDiscount = view.findViewById(R.id.etDiscount);
            tvInitialPrice = view.findViewById(R.id.tvInitialPrice);
            tvDiscount = view.findViewById(R.id.tvDiscount);
            tvFinalPrice = view.findViewById(R.id.tvFinalPrice);
            btnReset = view.findViewById(R.id.btnReset);

            // Set up text change listener
            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    calculateDiscount();
                }
            };

            // Add text change listeners
            etPrice.addTextChangedListener(textWatcher);
            etDiscount.addTextChangedListener(textWatcher);

            // Set up reset button
            btnReset.setOnClickListener(v -> resetCalculator());

            ImageView closeButton = view.findViewById(R.id.ic_close);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            return view;
        }

        private void calculateDiscount() {
            try {
                double price = Double.parseDouble(etPrice.getText().toString().isEmpty() ? "0" : etPrice.getText().toString());
                double discount = Double.parseDouble(etDiscount.getText().toString().isEmpty() ? "0" : etDiscount.getText().toString());

                double discountAmount = (price * discount) / 100;
                double finalPrice = price - discountAmount;

                // Format currency
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                // Update TextViews
                tvInitialPrice.setText(formatter.format(price).replace("IDR", "Rp"));
                tvDiscount.setText("-" + formatter.format(discountAmount).replace("IDR", "Rp"));
                tvFinalPrice.setText(formatter.format(finalPrice).replace("IDR", "Rp"));
            } catch (NumberFormatException e) {
                // Handle invalid input
                tvInitialPrice.setText("Rp 0");
                tvDiscount.setText("-Rp 0");
                tvFinalPrice.setText("Rp 0");
            }
        }

        private void resetCalculator() {
            etPrice.setText("");
            etDiscount.setText("");
            tvInitialPrice.setText("Rp 0");
            tvDiscount.setText("-Rp 0");
            tvFinalPrice.setText("Rp 0");
        }

        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            if (dialog != null) {
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }
        }
    }
}
