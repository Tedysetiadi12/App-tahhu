package com.tahhu.coba;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CompletedTransactionsFragment extends Fragment {

    private RecyclerView completedRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_transactions, container, false);

        completedRecyclerView = view.findViewById(R.id.completedRecyclerView);

        // Mengambil data dari TransactionManager menggunakan getInstance()
        TransactionManager transactionManager = TransactionManager.getInstance(requireContext());
        List<Transaction> completedTransactions = transactionManager.getCompletedTransactions();

        // Menampilkan data di RecyclerView
        CompletedTransactionAdapter adapter = new CompletedTransactionAdapter(completedTransactions);
        completedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        completedRecyclerView.setAdapter(adapter);

        return view;
    }
}
