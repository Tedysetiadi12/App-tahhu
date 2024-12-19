package com.tahhu.id;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    // Properties dengan nilai default
    private String id;
    private long timestamp = 0L;
    private double total = 0.0;
    private double amountPaid = 0.0;
    private double change = 0.0;
    private String status = "pending";
    private List<TransactionItem> items = new ArrayList<>();
    private String userId;

    // Constructor kosong untuk Firebase
    public Transaction() {}

    // Constructor dengan parameter
    public Transaction(String id, String userId, long timestamp, double total,
                       double amountPaid, double change, List<TransactionItem> items) {
        this.id = id;
        this.userId = userId;
        this.timestamp = timestamp;
        this.total = total;
        this.amountPaid = amountPaid;
        this.change = change;
        this.items = items != null ? items : new ArrayList<>();
        this.status = "pending";
    }

    // Getter dan Setter dengan validasi
    public String getId() {
        return id != null ? id : "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getTotal() {
        if (items != null) {
            double calculatedTotal = 0.0;
            for (TransactionItem item : items) {
                calculatedTotal += item.getSubtotal();
            }
            return calculatedTotal;
        }
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String getStatus() {
        return status != null ? status : "pending";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TransactionItem> getItems() {
        return items != null ? items : new ArrayList<>();
    }

    public void setItems(List<TransactionItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Method helper untuk menambah item
    public void addItem(TransactionItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        // Recalculate total
        this.total = getTotal();
    }

    // Method helper untuk menghapus item
    public void removeItem(TransactionItem item) {
        if (items != null) {
            items.remove(item);
            // Recalculate total
            this.total = getTotal();
        }
    }

    // Method untuk mengupdate status
    public void completeTransaction() {
        this.status = "completed";
    }

    // Method untuk validasi transaksi
    public boolean isValid() {
        return id != null && !id.isEmpty() &&
                timestamp > 0 &&
                total >= 0 &&
                items != null && !items.isEmpty();
    }
}