package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private TextView tvStatus;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);


        // Ambil data dari Intent
        int jumlahMinyak = getIntent().getIntExtra("jumlahMinyak", 0);
        String pilihanPenukaran = getIntent().getStringExtra("pilihanPenukaran");
        String alamatPenjemputan = getIntent().getStringExtra("alamatPenjemputan");
        String lokasi = getIntent().getStringExtra("lokasi");
        String hasilTukar = getIntent().getStringExtra("hasilTukar");
        int fee = getIntent().getIntExtra("fee", 0);

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerViewOrderHistory);
        tvStatus = findViewById(R.id.tvStatus);

        // Tambahkan data pesanan ke daftar
        orderList = new ArrayList<>();
        String detailPesanan = "Jumlah Minyak: " + jumlahMinyak + " liter\n" +
                "Pilihan Penukaran: " + pilihanPenukaran + "\n" +
                "Alamat: " + alamatPenjemputan + "\n" +
                "Lokasi: " + lokasi + "\n" +
                "Hasil Tukar: " + hasilTukar + "\n" +
                "Fee: Rp" + fee;

        orderList.add(new Order("Pesanan Baru", detailPesanan, "Sedang Diproses"));

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderHistoryAdapter(orderList);
        recyclerView.setAdapter(adapter);

        // Tampilkan status jika tidak ada data
        if (orderList.isEmpty()) {
            tvStatus.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvStatus.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}

// Model untuk Order
class Order {
    private String title;
    private String details;
    private String status;

    public Order(String title, String details, String status) {
        this.title = title;
        this.details = details;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getStatus() {
        return status;
    }
}

// Adapter untuk RecyclerView
class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private final List<Order> orderList;

    public OrderHistoryAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_order_history, null);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvTitle.setText(order.getTitle());
        holder.tvDetails.setText(order.getDetails());
        holder.tvStatus.setText(order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDetails, tvStatus;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvOrderTitle);
            tvDetails = itemView.findViewById(R.id.tvOrderDetails);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}
