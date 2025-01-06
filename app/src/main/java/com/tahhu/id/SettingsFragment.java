package com.tahhu.id;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    private EditText ssidEditText;
    private EditText passwordEditText;
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ssidEditText = view.findViewById(R.id.ssid_edit_text);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        saveButton = view.findViewById(R.id.save_button);

        saveButton.setOnClickListener(v -> saveSettings());

        return view;
    }

    private void saveSettings() {
        String ssid = ssidEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!ssid.isEmpty() && !password.isEmpty()) {
            // TODO: Implement actual saving of Wi-Fi settings
            Toast.makeText(getContext(), "Settings saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
}