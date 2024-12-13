package com.tahhu.id;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText emailEditText, passwordEditText;
    private MaterialButton loginButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> loginUser());

        TextView signUpTextView = findViewById(R.id.signUpTextView);
        SpannableString spannable = new SpannableString("Don't have an account? Sign Up");
        spannable.setSpan(new ForegroundColorSpan(Color.RED), 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Warna "Sign Up"
        spannable.setSpan(new UnderlineSpan(), 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Garis bawah "Sign Up"
        signUpTextView.setText(spannable);

        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign in with Firebase Authentication using email and password
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Successfully signed in
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Proceed to the main screen or user profile
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        // Failed to sign in
                        Toast.makeText(LoginActivity.this, "Authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

