package com.tahhu.id;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList; import java.util.Date;
public class CompletedListFragment extends Fragment implements ShoppingItemAdapter.OnItemActionListener {
    private RecyclerView rvCompletedItems; private TextView tvEmptyCompleted;
    private ShoppingItemAdapter adapter; private ArrayList<ShoppingItem> completedItems = new ArrayList<>();
    private DatabaseReference completedItemsRef;
    private DatabaseReference shoppingListRef;
    public CompletedListFragment(DatabaseReference completedItemsRef) {
        this.completedItemsRef = completedItemsRef; }
    public DatabaseReference getCompletedItemsRef() {
        return completedItemsRef; }
    private String lastDeletedItemId = null;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        shoppingListRef = ((ShoppingListActivity) getActivity()).
                shoppingListRef; View view = inflater.inflate(R.layout.fragment_completed_list, container, false);
        rvCompletedItems = view.findViewById(R.id.rvCompletedItems);
        tvEmptyCompleted = view.findViewById(R.id.tvEmptyCompleted);
        completedItems = new ArrayList<>();
        adapter = new ShoppingItemAdapter(completedItems, true, this);
        rvCompletedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCompletedItems.setAdapter(adapter);
        completedItemsRef.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) { completedItems.clear();
                for (DataSnapshot data : snapshot.getChildren()) { ShoppingItem item = data.getValue(ShoppingItem.class);
                    if (item != null && !item.getId().equals(lastDeletedItemId)) {
                        completedItems.add(item); } }
                adapter.notifyDataSetChanged();
                updateEmptyView(); }
            @Override

            public void onCancelled(
                    @NonNull
                    DatabaseError error) {}
        });

        updateEmptyView();
        return view;}public void addItem(ShoppingItem item) {

        boolean isItemExists = completedItems.stream() .anyMatch(existingItem -> existingItem.getId().equals(item.getId()));
        if (!isItemExists) {
            completedItems.add(item); adapter.notifyItemInserted(completedItems.size() - 1);
            double amountToAdd = item.getPrice() * item.getQuantity(); (
                    (ShoppingListActivity) getActivity()).updateTotalAmountAfterAddition(amountToAdd);
            updateEmptyView(); } }
    @Override public void onCompleteClick(ShoppingItem item) { }
    @Override public void onDeleteClick(ShoppingItem item) {
        lastDeletedItemId = item.getId(); DatabaseReference itemRef = shoppingListRef.child("completedItems").child(item.getId());
        itemRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                completedItems.removeIf(existingItem -> existingItem.getId().equals(item.getId()));
                adapter.notifyDataSetChanged(); updateEmptyView();
                double amountToSubtract = item.getPrice() * item.getQuantity();
                ((ShoppingListActivity) getActivity()).updateTotalAmountAfterDeletion(amountToSubtract);
                Toast.makeText(getContext(), "Item berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "Gagal menghapus item", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateEmptyView()
    {
        if (rvCompletedItems == null || tvEmptyCompleted == null)
        {
            Log.e("Debug", "RecyclerView or TextView is not initialized");
            return; }
        if (completedItems.isEmpty()) {
            rvCompletedItems.setVisibility(View.GONE);
            tvEmptyCompleted.setVisibility(View.VISIBLE);
        } else { rvCompletedItems.setVisibility(View.VISIBLE);
            tvEmptyCompleted.setVisibility(View.GONE);
        }
    }
}