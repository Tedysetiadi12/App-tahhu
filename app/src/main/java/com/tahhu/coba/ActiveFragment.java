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

public class ActiveFragment extends Fragment {
    private RecyclerView recyclerViewActive;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active, container, false);
        recyclerViewActive = view.findViewById(R.id.recyclerViewActive);

        // Set RecyclerView
        recyclerViewActive.setLayoutManager(new LinearLayoutManager(getContext()));
        List<CartProduct> activeProducts = CartManager.getInstance().getCartProducts();
        recyclerViewActive.setAdapter(new CartProductAdapter(activeProducts, () -> {}));
        return view;
    }
}
