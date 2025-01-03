package com.tahhu.id;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import android.Manifest;

public class LayananKeluhanActivity extends AppCompatActivity {
    private EditText etJudul, etDeskripsi;
    private Button btnKirim;
    private MaterialButton btnUploadGambar;
    private ImageView ivGambarKeluhan;
    private List<Keluhan> keluhanList;
    private KeluhanAdapter keluhanAdapter;
    private DatabaseReference keluhanReference;
    private String userId;
    private Uri imageUri;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan_keluhan);

        // Inisialisasi komponen
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView historyIcon = findViewById(R.id.history_icon);
        historyIcon.setOnClickListener(v -> {
            Intent intent = new Intent(LayananKeluhanActivity.this, RiwayatKeluhanActivity.class);
            startActivity(intent);
        });

        // Inisialisasi lainnya
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        keluhanReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("keluhan");

        etJudul = findViewById(R.id.etJudul);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        btnKirim = findViewById(R.id.btnKirim);
        btnUploadGambar = findViewById(R.id.btnUploadGambar);
        ivGambarKeluhan = findViewById(R.id.ivGambarKeluhan);

        keluhanList = new ArrayList<>();
        keluhanAdapter = new KeluhanAdapter(keluhanList);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                openFileChooser();
            } else {
                Toast.makeText(this, "Permission denied. Cannot access images.", Toast.LENGTH_SHORT).show();
            }
        });

        btnUploadGambar.setOnClickListener(v -> checkAndRequestPermissions());
        btnKirim.setOnClickListener(v -> kirimKeluhan());
    }


    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_MEDIA_IMAGES) ==
                    PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                // Take persistable URI permission
                getContentResolver().takePersistableUriPermission(imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Display the selected image
                ivGambarKeluhan.setImageURI(imageUri);
                ivGambarKeluhan.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(this, "Gagal memuat gambar: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void kirimKeluhan() {
        final String judul = etJudul.getText().toString().trim();
        final String deskripsi = etDeskripsi.getText().toString().trim();

        if (judul.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        String keluhanId = keluhanReference.push().getKey();
        String imageUriString = (imageUri != null) ? imageUri.toString() : null;

        // Simpan keluhan dengan path gambar lokal
        Keluhan keluhan = new Keluhan(keluhanId, judul, deskripsi, "Menunggu", imageUriString);
        keluhanReference.child(keluhanId).setValue(keluhan)
                .addOnSuccessListener(aVoid -> {
                    clearForm();
                    Toast.makeText(LayananKeluhanActivity.this,
                            "Keluhan berhasil dikirim", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LayananKeluhanActivity.this,
                            "Gagal menyimpan data: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void clearForm() {
        etJudul.setText("");
        etDeskripsi.setText("");
        ivGambarKeluhan.setImageResource(android.R.color.transparent);
        ivGambarKeluhan.setVisibility(View.GONE);
        imageUri = null;
    }

    public static class Keluhan {
        private String id;
        private String judul;
        private String deskripsi;
        private String status;
        private String imageUrl;

        public Keluhan() {
            // Required empty constructor for Firebase
        }

        public Keluhan(String id, String judul, String deskripsi, String status, String imageUrl) {
            this.id = id;
            this.judul = judul;
            this.deskripsi = deskripsi;
            this.status = status;
            this.imageUrl = imageUrl;
        }

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getJudul() { return judul; }
        public void setJudul(String judul) { this.judul = judul; }
        public String getDeskripsi() { return deskripsi; }
        public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }

    public static class KeluhanAdapter extends RecyclerView.Adapter<KeluhanAdapter.KeluhanViewHolder> {
        private List<Keluhan> keluhanList;
        private Context context;

        public KeluhanAdapter(List<Keluhan> keluhanList) {
            this.keluhanList = keluhanList;
        }

        @NonNull
        @Override
        public KeluhanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.item_keluhan, parent, false);
            return new KeluhanViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull KeluhanViewHolder holder, int position) {
            Keluhan keluhan = keluhanList.get(position);
            holder.tvJudul.setText(keluhan.getJudul());
            holder.tvDeskripsi.setText(keluhan.getDeskripsi());
            holder.tvStatus.setText(keluhan.getStatus());

            String imageUriString = keluhan.getImageUrl();
            if (imageUriString != null && !imageUriString.isEmpty()) {
                Uri imageUri = Uri.parse(imageUriString);
                Glide.with(context)
                        .load(imageUri)
                        .error(R.drawable.ic_user1)
                        .into(holder.ivGambarKeluhan);
                holder.ivGambarKeluhan.setVisibility(View.VISIBLE);
            } else {
                holder.ivGambarKeluhan.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return keluhanList.size();
        }

        static class KeluhanViewHolder extends RecyclerView.ViewHolder {
            TextView tvJudul, tvDeskripsi, tvStatus;
            ImageView ivGambarKeluhan;

            KeluhanViewHolder(View itemView) {
                super(itemView);
                tvJudul = itemView.findViewById(R.id.tvJudul);
                tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                ivGambarKeluhan = itemView.findViewById(R.id.ivGambarKeluhan);
                ivGambarKeluhan.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }
    }
}