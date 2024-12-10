package com.tahhu.id;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class SecurityQuestionsActivity extends AppCompatActivity {

    private EditText securityAnswer1, securityAnswer2;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_questions);

        // Initialize views
        securityAnswer1 = findViewById(R.id.securityAnswer1);
        securityAnswer2 = findViewById(R.id.securityAnswer2);
        saveButton = findViewById(R.id.saveSecurityQuestionsButton);
        ImageView backButton = findViewById(R.id.backButton);

        // Set up back button functionality
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes the activity and returns to the previous one
            }
        });

        // Save button functionality
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSecurityQuestions();
            }
        });
    }

    /**
     * Validates and saves security questions.
     */
    private void saveSecurityQuestions() {
        String answer1 = securityAnswer1.getText().toString().trim();
        String answer2 = securityAnswer2.getText().toString().trim();

        if (answer1.isEmpty()) {
            Toast.makeText(this, "Please answer the first question", Toast.LENGTH_SHORT).show();
            return;
        }

        if (answer2.isEmpty()) {
            Toast.makeText(this, "Please answer the second question", Toast.LENGTH_SHORT).show();
            return;
        }

        // Here, you would save the answers to a database or shared preferences
        // For this example, we'll just show a success message
        Toast.makeText(this, "Security questions saved successfully!", Toast.LENGTH_SHORT).show();

        // Optionally finish the activity after saving
        finish();
    }
}
