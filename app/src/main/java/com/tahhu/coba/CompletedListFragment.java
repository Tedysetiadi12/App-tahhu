package com.tahhu.coba;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CompletedListFragment extends Fragment implements ShoppingItemAdapter.OnItemActionListener {
    private RecyclerView rvCompletedItems;
    private TextView tvEmptyCompleted;
    private ShoppingItemAdapter adapter;
    private ArrayList<ShoppingItem> completedItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_list, container, false);
        rvCompletedItems = view.findViewById(R.id.rvCompletedItems);
        tvEmptyCompleted = view.findViewById(R.id.tvEmptyCompleted);

        completedItems = new ArrayList<>();
        adapter = new ShoppingItemAdapter(completedItems, true, this);
        rvCompletedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCompletedItems.setAdapter(adapter);

        updateEmptyView();
        return view;
    }

    public void addItem(ShoppingItem item) {
        completedItems.add(item);
        adapter.notifyItemInserted(completedItems.size() - 1);
        updateEmptyView();
    }

    @Override
    public void onCompleteClick(ShoppingItem item) {
        // This method won't be called for completed items
    }

    @Override
    public void onDeleteClick(ShoppingItem item) {
        if (getActivity() instanceof ShoppingListActivity) {
            ((ShoppingListActivity) getActivity()).deleteItem(item, true);
        }
        completedItems.remove(item);
        adapter.notifyDataSetChanged();
        updateEmptyView();
    }

    private void updateEmptyView() {
        if (completedItems.isEmpty()) {
            rvCompletedItems.setVisibility(View.GONE);
            tvEmptyCompleted.setVisibility(View.VISIBLE);
        } else {
            rvCompletedItems.setVisibility(View.VISIBLE);
            tvEmptyCompleted.setVisibility(View.GONE);
        }
    }
}