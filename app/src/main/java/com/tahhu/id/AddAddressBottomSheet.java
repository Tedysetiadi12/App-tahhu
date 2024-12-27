package com.tahhu.id;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.maps.MapView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddAddressBottomSheet extends BottomSheetDialogFragment {

    private MapView mapView;
    private EditText etRecipientName, etCity, etDistrict, etAddress;
    private Button btnSaveAddress;
    private OnAddressSaveListener listener;

    // Interface untuk mendengarkan event penyimpanan alamat
    public interface OnAddressSaveListener {
        void onAddressSaved(String recipientName, String city, String district, String address);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddressSaveListener) {
            listener = (OnAddressSaveListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAddressSaveListener");
        }
    }
    public void setOnAddressSaveListener(OnAddressSaveListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_add_address_bottom_sheet, container, false);

        // Inisialisasi view
        etRecipientName = view.findViewById(R.id.etRecipientName);
        etCity = view.findViewById(R.id.etCity);
        etDistrict = view.findViewById(R.id.etDistrict);
        etAddress = view.findViewById(R.id.etAddress);
        btnSaveAddress = view.findViewById(R.id.btnSaveAddress);
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        // Tombol untuk menyimpan alamat
        btnSaveAddress.setOnClickListener(v -> saveAddress());
        return view;
    }

    private void saveAddress() {
        // Ambil data dari input field
        String recipientName = etRecipientName.getText().toString();
        String city = etCity.getText().toString();
        String district = etDistrict.getText().toString();
        String address = etAddress.getText().toString();

        // Kirim data ke listener
        if (listener != null) {
            listener.onAddressSaved(recipientName, city, district, address);
        }
        dismiss(); // Tutup bottom sheet
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
