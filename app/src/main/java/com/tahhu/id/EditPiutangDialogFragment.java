package com.tahhu.id;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditPiutangDialogFragment extends DialogFragment {

    private static final String ARG_PIUTANG = "piutang";
    private DatabaseReference databaseReference;

    public static EditPiutangDialogFragment newInstance(Piutang piutang) {
        EditPiutangDialogFragment fragment = new EditPiutangDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PIUTANG, piutang); // Menggunakan kunci yang konsisten
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_piutang, null);

        EditText edtNama = view.findViewById(R.id.edtNama);
        EditText edtJumlah = view.findViewById(R.id.edtJumlah);
        AppCompatButton btnSelectType = view.findViewById(R.id.btnSelectType);
        EditText edtDeskripsi = view.findViewById(R.id.edtDeskripsi);
        EditText edtCatatan = view.findViewById(R.id.edtCatatan);
        AppCompatButton btnSelectStatus = view.findViewById(R.id.btnSelectStatus);
        TextView txtTanggal = view.findViewById(R.id.txtTanggal);
        TextView txtTanggalJatuhTempo = view.findViewById(R.id.txtTanggalJatuhTempo);
        Button btnSimpan = view.findViewById(R.id.btnSimpan);
        Button btnBatal = view.findViewById(R.id.btnBatal);
        Piutang piutang = (Piutang) getArguments().getSerializable(ARG_PIUTANG);

        if (piutang == null) {
            Toast.makeText(getActivity(), "Data Piutang tidak ditemukan", Toast.LENGTH_SHORT).show();
            dismiss();
            return null; // Atau tangani dengan cara lain
        }

        // Prepopulate fields
        edtNama.setText(piutang.getNama());
        edtJumlah.setText(String.valueOf(piutang.getJumlah()));
        edtDeskripsi.setText(piutang.getDeskripsi());
        edtCatatan.setText(piutang.getCatatan());
        txtTanggal.setText(piutang.getTanggal());
        txtTanggalJatuhTempo.setText(piutang.getJatuhTempo());
        btnSelectType.setText(piutang.getType());
        btnSelectStatus.setText(piutang.getStatus());

        // Populate spinner with values
        // Populate spinner values
        List<String> typeList = Arrays.asList("Piutang", "Utang");
        List<String> statusList = Arrays.asList("Belum Lunas", "Lunas");

        // Date pickers
        txtTanggal.setOnClickListener(v -> showDatePickerDialog(txtTanggal));
        txtTanggalJatuhTempo.setOnClickListener(v -> showDatePickerDialog(txtTanggalJatuhTempo));

        // Single choice dialogs
        btnSelectType.setOnClickListener(v -> showSingleChoiceDialog("Pilih Tipe", typeList, btnSelectType.getText().toString(),
                selectedItem -> btnSelectType.setText(selectedItem)));
        btnSelectStatus.setOnClickListener(v -> showSingleChoiceDialog("Pilih Status", statusList, btnSelectStatus.getText().toString(),
                selectedItem -> btnSelectStatus.setText(selectedItem)));

        // Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("piutang");


        btnSimpan.setOnClickListener(v -> {
            try {
                String updatedNama = edtNama.getText().toString();
                double updatedJumlah = Double.parseDouble(edtJumlah.getText().toString());
                String updatedDeskripsi = edtDeskripsi.getText().toString();
                String updatedCatatan = edtCatatan.getText().toString();
                String updatedType = btnSelectType.getText().toString();
                String updatedStatus = btnSelectStatus.getText().toString();
                String updatedTanggal = txtTanggal.getText().toString();
                String updatedTanggalJatuhTempo = txtTanggalJatuhTempo.getText().toString();

                // Menampilkan konfirmasi dialog sebelum menyimpan
                new AlertDialog.Builder(getActivity())
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah Anda yakin ingin mengubah data Piutang?")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            // Update Piutang object
                            piutang.setNama(updatedNama);
                            piutang.setJumlah(String.valueOf(updatedJumlah));
                            piutang.setDeskripsi(updatedDeskripsi);
                            piutang.setCatatan(updatedCatatan);
                            piutang.setType(updatedType);
                            piutang.setStatus(updatedStatus);
                            piutang.setTanggal(updatedTanggal);
                            piutang.setJatuhTempo(updatedTanggalJatuhTempo);

                            // Save to database
                            databaseReference.child(piutang.getId()).setValue(piutang)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Piutang berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                        } else {
                                            Toast.makeText(getActivity(), "Gagal memperbarui Piutang", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        })
                        .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Jumlah harus berupa angka", Toast.LENGTH_SHORT).show();
            }
        });


        btnBatal.setOnClickListener(v -> dismiss());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void showSingleChoiceDialog(String title, List<String> items, String currentSelection, OnSingleChoiceSelectedListener listener) {
        CharSequence[] options = items.toArray(new CharSequence[0]);
        int selectedIndex = items.indexOf(currentSelection);

        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setSingleChoiceItems(options, selectedIndex, (dialog, which) -> {
                    listener.onItemSelected(options[which].toString());
                    dialog.dismiss();
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
    /**
     * Helper function to show a DatePickerDialog and set the selected date to the target TextView.
     */
    private void showDatePickerDialog(TextView target) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    target.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public interface OnSingleChoiceSelectedListener {
        void onItemSelected(String selectedItem);
    }
}
