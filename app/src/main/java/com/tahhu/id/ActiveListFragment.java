package com.tahhu.id;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class ActiveListFragment extends Fragment implements ShoppingItemAdapter.OnItemActionListener {
    private RecyclerView rvActiveItems;
    private TextView tvEmptyActive;
    private ShoppingItemAdapter adapter;
    private ArrayList<ShoppingItem> activeItems;
    private DatabaseReference activeItemsRef;
    private DatabaseReference shoppingListRef;

    public ActiveListFragment(DatabaseReference activeItemsRef) {
        this.activeItemsRef = activeItemsRef;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get shoppingListRef from the parent activity
        shoppingListRef = ((ShoppingListActivity) getActivity()).shoppingListRef;

        View view = inflater.inflate(R.layout.fragment_active_list, container, false);
        rvActiveItems = view.findViewById(R.id.rvActiveItems);
        tvEmptyActive = view.findViewById(R.id.tvEmptyActive);

        activeItems = new ArrayList<>();
        adapter = new ShoppingItemAdapter(activeItems, false, this);
        rvActiveItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvActiveItems.setAdapter(adapter);

        // Firebase listener
        activeItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                activeItems.clear(); // Clear previous data
                for (DataSnapshot data : snapshot.getChildren()) {
                    ShoppingItem item = data.getValue(ShoppingItem.class);
                    if (item != null) {
                        // Tambahkan pengecekan dengan ID unik
                        boolean isItemExists = activeItems.stream()
                                .anyMatch(existingItem -> existingItem.getId().equals(item.getId()));

                        if (!isItemExists) {
                            activeItems.add(item);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                updateEmptyView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        updateEmptyView();
        return view;
    }

    public void addItem(ShoppingItem item) {
        // Cegah duplikasi saat menambahkan item
        boolean isItemExists = activeItems.stream()
                .anyMatch(existingItem -> existingItem.getId().equals(item.getId()));

        if (!isItemExists) {
            activeItems.add(item);
            adapter.notifyItemInserted(activeItems.size() - 1);
            updateEmptyView();
        }
    }

    @Override
    public void onCompleteClick(ShoppingItem item) {
        String originalItemId = item.getId();

        activeItemsRef.child(originalItemId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DatabaseReference completedItemsRef = shoppingListRef.child("completedItems").child(originalItemId);
                item.setCompletionDate(new Date());
                completedItemsRef.setValue(item).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        activeItems.remove(item);
                        adapter.notifyDataSetChanged();
                        updateEmptyView();
                    } else {
                        Toast.makeText(getContext(), "Gagal memindahkan item", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Gagal menghapus dari activeItems", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDeleteClick(ShoppingItem item) {
        // Remove item from Firebase database
        activeItemsRef.child(item.getId()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Remove from UI
                activeItems.remove(item);
                adapter.notifyDataSetChanged();
                updateEmptyView();
            } else {
                Toast.makeText(getContext(), "Gagal menghapus item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEmptyView() {
        if (activeItems.isEmpty()) {
            rvActiveItems.setVisibility(View.GONE);
            tvEmptyActive.setVisibility(View.VISIBLE);
        } else {
            rvActiveItems.setVisibility(View.VISIBLE);
            tvEmptyActive.setVisibility(View.GONE);
        }
    }
}