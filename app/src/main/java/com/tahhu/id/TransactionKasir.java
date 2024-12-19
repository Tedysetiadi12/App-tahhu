package com.tahhu.id;
import java.util.List;

public class TransactionKasir {
    private String userId;
    private List<CartItemKasir> items;
    private double total;
    private long timestamp;

    public TransactionKasir() {
        // Default constructor required for calls to DataSnapshot.getValue(TransactionKasir.class)
    }

    public TransactionKasir(String userId, List<CartItemKasir> items, double total) {
        this.userId = userId;
        this.items = items;
        this.total = total;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<CartItemKasir> getItems() { return items; }
    public void setItems(List<CartItemKasir> items) { this.items = items; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}

