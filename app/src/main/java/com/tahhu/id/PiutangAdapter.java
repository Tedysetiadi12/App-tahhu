package com.tahhu.id;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PiutangAdapter extends RecyclerView.Adapter<PiutangAdapter.PiutangViewHolder> {

    private List<Piutang> piutangList;

    public PiutangAdapter(List<Piutang> piutangList) {
        this.piutangList = piutangList;
    }

    @Override
    public PiutangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new PiutangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PiutangViewHolder holder, int position) {
        Piutang piutang = piutangList.get(position);
        holder.nameText.setText(piutang.getNama());  // Use getNama() for name
        holder.statusText.setText(piutang.getStatus() + " - " + piutang.getJumlah());  // Use getJumlah() for amount
    }

    // Update the list with new data
    public void updateList(List<Piutang> newPiutangList) {
        this.piutangList = newPiutangList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return piutangList.size();
    }

    public static class PiutangViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, statusText;

        public PiutangViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(android.R.id.text1);
            statusText = itemView.findViewById(android.R.id.text2);
        }
    }
}
