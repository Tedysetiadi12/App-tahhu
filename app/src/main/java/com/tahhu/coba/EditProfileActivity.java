package com.tahhu.coba;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName, editLastName, editEmail;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Bind Views
        editName = findViewById(R.id.edit_name);
        editLastName = findViewById(R.id.edit_lastname);
        editEmail = findViewById(R.id.edit_email);
        saveButton = findViewById(R.id.save_button);
        ImageView backToSettings = findViewById(R.id.btn_back);

        // Receive Intent Data
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String lastName = intent.getStringExtra("lastname");
        String email = intent.getStringExtra("email");

        // Set existing data to fields
        editName.setText(name);
        editLastName.setText(lastName);
        editEmail.setText(email);

        // Back Button Click Listener
        backToSettings.setOnClickListener(view -> finish());

        saveButton.setOnClickListener(view -> {
            String updatedName = editName.getText().toString().trim();
            String updatedLastName = editLastName.getText().toString().trim();
            String updatedEmail = editEmail.getText().toString().trim();

            if (updatedName.isEmpty() || updatedLastName.isEmpty() || updatedEmail.isEmpty()) {
                Toast.makeText(EditProfileActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pass back updated data
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedName", updatedName);
            resultIntent.putExtra("updatedLastName", updatedLastName);
            resultIntent.putExtra("updatedEmail", updatedEmail);
            setResult(RESULT_OK, resultIntent);

            finish();
        });
    }
}
