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

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private Context context;
    private List<Car> carList;
    private String address;
    private String destination;

    // Modifikasi konstruktor untuk menerima address dan destination
    public CarAdapter(Context context, List<Car> carList, String address, String destination) {
        this.context = context;
        this.carList = carList;
        this.address = address;
        this.destination = destination;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.titleCar.setText(car.getTitle());
        holder.priceCar.setText("Rp. " + car.getPrice());
        holder.detailCar.setText(car.getDetail());
        holder.imageCar.setImageResource(car.getimageResource());

        holder.btnRideNow.setOnClickListener(v -> {
            // Misalnya, membuka halaman pembayaran dengan data address dan destination
            Intent intent = new Intent(context, PaymentActivityRideSharing.class);
            intent.putExtra("selected_vehicle_name", car.getTitle());
            intent.putExtra("selected_vehicle_price", car.getPrice());
            intent.putExtra("selected_vehicle_detail", car.getDetail());
            intent.putExtra("selected_vehicle_image_res_id", car.getimageResource());
            intent.putExtra("address", address);
            intent.putExtra("destination", destination);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCar;
        TextView titleCar, priceCar, detailCar;
        Button btnRideNow;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCar = itemView.findViewById(R.id.imageCar);
            titleCar = itemView.findViewById(R.id.titleCar);
            priceCar = itemView.findViewById(R.id.priceCar);
            detailCar = itemView.findViewById(R.id.detailCar);
            btnRideNow = itemView.findViewById(R.id.btnRideNow);
        }
    }
}
