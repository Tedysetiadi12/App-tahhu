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

public class CompletedFragment extends Fragment {
    private RecyclerView recyclerViewCompleted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed, container, false);
        recyclerViewCompleted = view.findViewById(R.id.recyclerViewCompleted);

        // Set RecyclerView
        recyclerViewCompleted.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Transaction> completedTransactions = TransactionManager.getInstance(requireContext()).getCompletedTransactions();
        recyclerViewCompleted.setAdapter(new CompletedTransactionAdapter(completedTransactions));
        return view;
    }
}