package com.tahhu.id;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {
    private List<Transaction> transactions = new ArrayList<>();
    private OnTransactionCompleteListener completeListener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", new Locale("id"));

    public interface OnTransactionCompleteListener {
        void onTransactionComplete(String transactionId);
    }

    public TransactionHistoryAdapter(OnTransactionCompleteListener listener) {
        this.completeListener = listener;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_kasir, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        if (transaction != null) {
            try {
                holder.bind(transaction);
            } catch (Exception e) {
                Log.e("TransactionAdapter", "Error binding transaction: " + e.getMessage());
                // Tampilkan placeholder atau handle error
                holder.showErrorState();
            }
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTransactionDate, tvTransactionTotal;
        RecyclerView rvTransactionItems;
        Button btnCompleteTransaction;
        TransactionItemsAdapter itemsAdapter;

        ViewHolder(View view) {
            super(view);
            tvTransactionDate = view.findViewById(R.id.tvTransactionDate);
            tvTransactionTotal = view.findViewById(R.id.tvTransactionTotal);
            rvTransactionItems = view.findViewById(R.id.rvTransactionItems);
            btnCompleteTransaction = view.findViewById(R.id.btnCompleteTransaction);

            // Setup nested RecyclerView for transaction items
            rvTransactionItems.setLayoutManager(new LinearLayoutManager(view.getContext()));
            itemsAdapter = new TransactionItemsAdapter();
            rvTransactionItems.setAdapter(itemsAdapter);
        }

        void bind(Transaction transaction) {
            // Gunakan safe calls untuk menghindari NPE
            tvTransactionDate.setText(transaction.getTimestamp() > 0 ?
                    dateFormat.format(new Date(transaction.getTimestamp())) : "Tanggal tidak tersedia");

            tvTransactionTotal.setText(String.format(Locale.getDefault(),
                    "Total: Rp %,.0f", transaction.getTotal() ));

            List<TransactionItem> items = transaction.getItems();
            if (items != null && !items.isEmpty()) {
                itemsAdapter.setItems(items);
                rvTransactionItems.setVisibility(View.VISIBLE);
            } else {
                rvTransactionItems.setVisibility(View.GONE);
            }

            String status = transaction.getStatus();
            if ("completed".equals(status)) {
                btnCompleteTransaction.setVisibility(View.GONE);
            } else {
                btnCompleteTransaction.setVisibility(View.VISIBLE);
                btnCompleteTransaction.setOnClickListener(v -> {
                    if (completeListener != null && transaction.getId() != null) {
                        completeListener.onTransactionComplete(transaction.getId());
                    }
                });
            }
        }

        void showErrorState() {
            tvTransactionDate.setText("Error loading transaction");
            tvTransactionTotal.setText("-");
            rvTransactionItems.setVisibility(View.GONE);
            btnCompleteTransaction.setVisibility(View.GONE);
        }
    }
}
