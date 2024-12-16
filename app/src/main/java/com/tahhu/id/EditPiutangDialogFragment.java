package com.tahhu.id;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class EditPiutangDialogFragment extends DialogFragment {

    private static final String ARG_PIUTANG = "piutang";
    private Piutang piutang;
    private DatabaseReference databaseReference;

    public static EditPiutangDialogFragment newInstance(Piutang piutang) {
        EditPiutangDialogFragment fragment = new EditPiutangDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("piutang_key", (Serializable) piutang); // Use a key to pass the object
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the dialog layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_piutang, null);

        // Initialize views
        EditText edtNama = view.findViewById(R.id.edtNama);
        EditText edtJumlah = view.findViewById(R.id.edtJumlah);
        Spinner spinnerType = view.findViewById(R.id.spinnerType);
        EditText edtDeskripsi = view.findViewById(R.id.edtDeskripsi);
        EditText edtCatatan = view.findViewById(R.id.edtCatatan);
        Spinner spinnerStatus = view.findViewById(R.id.spinnerStatus);
        Button btnSimpan = view.findViewById(R.id.btnSimpan);
        Button btnBatal = view.findViewById(R.id.btnBatal);

        // Get the passed Piutang object
        Piutang piutang = (Piutang) getArguments().getSerializable("piutang_key");

        if (piutang == null) {
            Toast.makeText(getActivity(), "Data Piutang tidak ditemukan", Toast.LENGTH_SHORT).show();
            dismiss();
            return null;  // or handle appropriately
        }

        // Pre-populate fields with current piutang data
        edtNama.setText(piutang.getNama());
        edtJumlah.setText(String.valueOf(piutang.getJumlah()));
        edtDeskripsi.setText(piutang.getDeskripsi());
        edtCatatan.setText(piutang.getCatatan());

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("piutang");

        // Save updated data
        btnSimpan.setOnClickListener(v -> {
            // Get the updated values
            String updatedNama = edtNama.getText().toString();
            double updatedJumlah = Double.parseDouble(edtJumlah.getText().toString());
            String updatedDeskripsi = edtDeskripsi.getText().toString();
            String updatedCatatan = edtCatatan.getText().toString();
            String updatedStatus = spinnerStatus.getSelectedItem().toString();

            // Update Piutang object
            piutang.setNama(updatedNama);
            piutang.setJumlah(String.valueOf(updatedJumlah));
            piutang.setDeskripsi(updatedDeskripsi);
            piutang.setCatatan(updatedCatatan);
            piutang.setStatus(updatedStatus);

            // Update the database
            databaseReference.child(piutang.getId()).setValue(piutang)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Piutang berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            dismiss(); // Close the dialog
                        } else {
                            Toast.makeText(getActivity(), "Gagal memperbarui Piutang", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Close the dialog without saving
        btnBatal.setOnClickListener(v -> dismiss());

        // Build and return the dialog
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
}
