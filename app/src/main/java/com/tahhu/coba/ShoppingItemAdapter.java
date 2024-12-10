package com.tahhu.coba;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder> {
    private List<ShoppingItem> items;
    private boolean isCompleted;
    private OnItemActionListener listener;

    public interface OnItemActionListener {
        void onCompleteClick(ShoppingItem item);
        void onDeleteClick(ShoppingItem item);
    }

    public ShoppingItemAdapter(List<ShoppingItem> items, boolean isCompleted, OnItemActionListener listener) {
        this.items = items;
        this.isCompleted = isCompleted;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shopping_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingItem item = items.get(position);
        holder.tvItemDate.setText(item.getDate());
        holder.tvItemName.setText(item.getName());
        holder.tvItemDetails.setText(String.format("Jumlah: %d\nHarga: Rp %.2f\nKategori: %s",
                item.getQuantity(), item.getPrice(), item.getCategory()));

        holder.tvItemNotes.setText(String.format("Catatan: %s", item.getNotes()));

        if (isCompleted) {
            holder.btnComplete.setVisibility(View.GONE);
        } else {
            holder.btnComplete.setOnClickListener(v -> listener.onCompleteClick(item));
        }

        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemDate, tvItemName, tvItemDetails, tvItemNotes;
        Button btnComplete, btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvItemDate = itemView.findViewById(R.id.tvItemDate);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemDetails = itemView.findViewById(R.id.tvItemDetails);
            tvItemNotes = itemView.findViewById(R.id.tvItemNotes);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}