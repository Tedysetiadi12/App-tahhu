package com.tahhu.id;

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

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.BikeViewHolder> {

    private Context context;
    private List<Bike> BikeList;
    private String address, destination, selectionInfo;

    public BikeAdapter(Context context, List<Bike> BikeList, String address, String destination, String selectionInfo) {
        this.context = context;
        this.BikeList = BikeList;
        this.address = address;
        this.destination = destination;
        this.selectionInfo = selectionInfo;
    }

    @NonNull
    @Override
    public BikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bike, parent, false);
        return new BikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BikeViewHolder holder, int position) {
        Bike bike = BikeList.get(position);
        holder.titleBike.setText(bike.getTitle());
        holder.priceBike.setText("Rp. " + bike.getPrice());
        holder.detailBike.setText(bike.getDetail());
        holder.imageBike.setImageResource(bike.getImageResource());

        holder.btnRideNow.setOnClickListener(v -> {
            // Handle Ride Now button click
            Intent intent = new Intent(context, PaymentActivityRideSharing.class);
            intent.putExtra("selected_vehicle_image_res_id", bike.getImageResource());
            intent.putExtra("selected_vehicle_name", bike.getTitle());
            intent.putExtra("selected_vehicle_price", bike.getPrice());
            intent.putExtra("selected_vehicle_detail", bike.getDetail());
            intent.putExtra("address", address);
            intent.putExtra("destination", destination);
            intent.putExtra("selection_info", selectionInfo);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return BikeList.size();
    }

    public static class BikeViewHolder extends RecyclerView.ViewHolder {  // Pastikan nama kelas sesuai
        ImageView imageBike;
        TextView titleBike, priceBike, detailBike;
        Button btnRideNow;

        public BikeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBike = itemView.findViewById(R.id.imageBike);
            titleBike = itemView.findViewById(R.id.titleBike);
            priceBike = itemView.findViewById(R.id.priceBike);
            detailBike = itemView.findViewById(R.id.detailBike);
            btnRideNow = itemView.findViewById(R.id.btnRideNow);
        }
    }
}
