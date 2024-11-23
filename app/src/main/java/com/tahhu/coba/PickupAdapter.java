package com.tahhu.coba;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PickupAdapter extends RecyclerView.Adapter<PickupAdapter.PickupViewHolder> {

    private Context context;
    private List<Pickup> PickupList;
    private String address;
    private String destination;

    public PickupAdapter(Context context, List<Pickup> PickupList, String address, String destination) {
        this.context = context;
        this.PickupList = PickupList;
        this.address = address;
        this.destination = destination;
    }

    @NonNull
    @Override
    public PickupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pickup, parent, false);
        return new PickupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PickupViewHolder holder, int position) {
        Pickup pickup = PickupList.get(position);
        holder.titlePickup.setText(pickup.getTitle());
        holder.pricePickup.setText("Rp" + pickup.getPrice());
        holder.imagePickup.setImageResource(pickup.getImageResource());

        holder.btnRideNow.setOnClickListener(v -> {
            // Handle Ride Now button click
            Intent intent = new Intent(context, PaymentActivityRideSharing.class);
            intent.putExtra("selected_vehicle_image_res_id", pickup.getImageResource());
            intent.putExtra("selected_vehicle_name", pickup.getTitle());
            intent.putExtra("selected_vehicle_price", pickup.getPrice());
            intent.putExtra("address", address);
            intent.putExtra("destination", destination);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return PickupList.size();
    }

    public static class PickupViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePickup;
        TextView titlePickup, pricePickup;
        Button btnRideNow;

        public PickupViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePickup = itemView.findViewById(R.id.imagePickup);
            titlePickup = itemView.findViewById(R.id.titlePickup);
            pricePickup = itemView.findViewById(R.id.pricePickup);
            btnRideNow = itemView.findViewById(R.id.btnRideNow);
        }
    }
}
