package com.tahhu.id;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActiveListFragment extends Fragment implements ShoppingItemAdapter.OnItemActionListener {
    private RecyclerView rvActiveItems;
    private TextView tvEmptyActive;
    private ShoppingItemAdapter adapter;
    private ArrayList<ShoppingItem> activeItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_list, container, false);
        rvActiveItems = view.findViewById(R.id.rvActiveItems);
        tvEmptyActive = view.findViewById(R.id.tvEmptyActive);

        activeItems = new ArrayList<>();
        adapter = new ShoppingItemAdapter(activeItems, false, this);
        rvActiveItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvActiveItems.setAdapter(adapter);

        updateEmptyView();
        return view;
    }

    public void addItem(ShoppingItem item) {
        activeItems.add(item);
        adapter.notifyItemInserted(activeItems.size() - 1);
        updateEmptyView();
    }

    @Override
    public void onCompleteClick(ShoppingItem item) {
        if (getActivity() instanceof ShoppingListActivity) {
            ((ShoppingListActivity) getActivity()).markItemAsComplete(item);
        }
        activeItems.remove(item);
        adapter.notifyDataSetChanged();
        updateEmptyView();
    }

    @Override
    public void onDeleteClick(ShoppingItem item) {
        if (getActivity() instanceof ShoppingListActivity) {
            ((ShoppingListActivity) getActivity()).deleteItem(item, false);
        }
        activeItems.remove(item);
        adapter.notifyDataSetChanged();
        updateEmptyView();
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