package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // UI elements
    private EditText emailEditText, usernameEditText, phoneEditText, addressEditText, passwordEditText;
    private MaterialButton signUpButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize FirebaseAuth and Firebase Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        loginButton = findViewById(R.id.loginButton);

        signUpButton.setOnClickListener(v -> signUpUser());
        loginButton.setOnClickListener(v -> startActivity
                (new Intent(SignUpActivity.this, LoginActivity.class)
                ));
    }

    private void signUpUser() {
        String email = emailEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validasi input: Pastikan tidak ada field yang kosong
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(SignUpActivity.this, "Harap isi semua kolom yang wajib", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi format email
        if (!isValidEmail(email)) {
            emailEditText.setError("Harap masukkan alamat email yang valid");
            emailEditText.requestFocus();
            return;
        }

        // Validasi password minimal 6 karakter
        if (password.length() < 6) {
            passwordEditText.setError("Password harus terdiri dari minimal 6 karakter");
            passwordEditText.requestFocus();
            return;
        }

        // Cek apakah email sudah terdaftar
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().getSignInMethods().isEmpty()) {
                            // Jika email sudah terdaftar
                            emailEditText.setError("Email ini sudah terdaftar");
                            emailEditText.requestFocus();
                        } else {
                            // Cek apakah username sudah terdaftar
                            createUser(email, password, username, phone, address);
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Gagal memeriksa email", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void createUser(String email, String password, String username, String phone, String address) {
        // Create new user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Pendaftaran berhasil
                        FirebaseUser user = mAuth.getCurrentUser();
                        User newUser = new User(username, email, phone, address);

                        if (user != null) {
                            mDatabase.child("users").child(user.getUid()).setValue(newUser)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            // Sukses menyimpan data pengguna
                                            Toast.makeText(SignUpActivity.this, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                            finish();
                                        } else {
                                            // Gagal menyimpan data pengguna
                                            Toast.makeText(SignUpActivity.this, "Gagal menyimpan data pengguna", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Autentikasi gagal
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Terjadi kesalahan";
                        Toast.makeText(SignUpActivity.this, "Autentikasi gagal: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    // Fungsi untuk validasi format email menggunakan regex
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return email.matches(emailPattern);
    }


}
