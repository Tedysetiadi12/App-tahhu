package com.tahhu.id;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private Context context;
    private List<Booking> bookings;
    private boolean isActiveBookings;
    private BookingActionListener actionListener;

    public interface BookingActionListener {
        void onCompleteBooking(Booking booking);
        void onTrackBooking(Booking booking);
    }

    public BookingAdapter(Context context, List<Booking> bookings, boolean isActiveBookings, BookingActionListener listener) {
        this.context = context;
        this.bookings = bookings;
        this.isActiveBookings = isActiveBookings;
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.securityNameTextView.setText("Nama Security: " + booking.getSecurityName());
        holder.securityTypeTextView.setText("Type Security: " + booking.getSecurityType());
        holder.periodTextView.setText("Periode Pemesanan: " + booking.getPeriod());
        holder.priceTextView.setText("Harga: " + booking.getPrice());
        holder.addressTextView.setText("Alamat: " + booking.getAddress());
        holder.paymentMethodTextView.setText("Metode Pembayaran: " + booking.getPaymentMethod());


        if (isActiveBookings) {
            holder.actionButton.setText("Complete");
            holder.actionButton.setOnClickListener(v -> actionListener.onCompleteBooking(booking));
            holder.trackButton.setVisibility(View.VISIBLE);
            holder.trackButton.setOnClickListener(v -> actionListener.onTrackBooking(booking));
        } else {
            holder.actionButton.setVisibility(View.GONE);
            holder.trackButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView securityNameTextView, securityTypeTextView, periodTextView, priceTextView, addressTextView, paymentMethodTextView;
        Button actionButton, trackButton;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            securityNameTextView = itemView.findViewById(R.id.securityNameTextView);
            securityTypeTextView = itemView.findViewById(R.id.securityTypeTextView);
            periodTextView = itemView.findViewById(R.id.periodTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            paymentMethodTextView = itemView.findViewById(R.id.paymentMethodTextView);
            actionButton = itemView.findViewById(R.id.actionButton);
            trackButton = itemView.findViewById(R.id.trackButton);
        }
    }
}

