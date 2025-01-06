package com.tahhu.id;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class GuestManagementFragment extends Fragment {
    private EditText guestNameEditText;
    private Button addGuestButton;
    private ListView guestListView;
    private List<String> guestList;
    private GuestListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_management, container, false);

        guestNameEditText = view.findViewById(R.id.guest_name_edit_text);
        addGuestButton = view.findViewById(R.id.add_guest_button);
        guestListView = view.findViewById(R.id.guest_list_view);

        guestList = new ArrayList<>();
        adapter = new GuestListAdapter(getContext(), guestList);
        guestListView.setAdapter(adapter);

        addGuestButton.setOnClickListener(v -> addGuest());

        return view;
    }

    private void addGuest() {
        String guestName = guestNameEditText.getText().toString().trim();
        if (!guestName.isEmpty()) {
            guestList.add(guestName);
            adapter.notifyDataSetChanged();
            guestNameEditText.setText("");
        }
    }
}
