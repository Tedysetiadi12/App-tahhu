package com.tahhu.id;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
        void onCompleteBooking(Booking booking, float rating, String comment);
        void onTrackBooking(Booking booking);
    }

    public BookingAdapter(Context context, List<Booking> bookings, boolean isActiveBookings, BookingActionListener listener) {
        this.context = context;
        this.bookings = bookings;
        this.isActiveBookings = isActiveBookings;
        this.actionListener = listener;
    }

    private void showRatingDialog(Booking booking) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_rating_security, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        EditText commentEditText = dialogView.findViewById(R.id.commentEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("Berikan Rating")
                .setPositiveButton("Selesai", (dialog, which) -> {
                    float rating = ratingBar.getRating();
                    String comment = commentEditText.getText().toString();
                    actionListener.onCompleteBooking(booking, rating, comment);
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
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
            holder.actionButton.setOnClickListener(v -> showRatingDialog(booking));
            holder.trackButton.setVisibility(View.VISIBLE);
            holder.trackButton.setOnClickListener(v -> actionListener.onTrackBooking(booking));
            holder.ratingContainer.setVisibility(View.GONE);
        } else {
            // Untuk completed bookings, tampilkan rating dan comment
            holder.actionButton.setVisibility(View.GONE);
            holder.trackButton.setVisibility(View.GONE);
            holder.ratingContainer.setVisibility(View.VISIBLE);

            // Set rating
            holder.ratingBarView.setRating(booking.getRating());
            holder.ratingTextView.setText("Rating: " + booking.getRating() + "/5");

            // Set comment jika ada
            if (booking.getComment() != null && !booking.getComment().isEmpty()) {
                holder.commentTextView.setVisibility(View.VISIBLE);
                holder.commentTextView.setText("Komentar: " + booking.getComment());
            } else {
                holder.commentTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView securityNameTextView, securityTypeTextView, periodTextView, priceTextView,
                addressTextView, paymentMethodTextView, ratingTextView, commentTextView;
        RatingBar ratingBarView;
        Button actionButton, trackButton;
        View ratingContainer;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            securityNameTextView = itemView.findViewById(R.id.securityNameTextView);
            securityTypeTextView = itemView.findViewById(R.id.securityTypeTextView);
            periodTextView = itemView.findViewById(R.id.periodTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            paymentMethodTextView = itemView.findViewById(R.id.paymentMethodTextView);
            ratingBarView = itemView.findViewById(R.id.ratingBarView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            actionButton = itemView.findViewById(R.id.actionButton);
            trackButton = itemView.findViewById(R.id.trackButton);
            ratingContainer = itemView.findViewById(R.id.ratingContainer);
        }
    }
}