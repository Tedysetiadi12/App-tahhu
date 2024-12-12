package com.tahhu.id;

import java.util.Date;

public class ShoppingItem {
    private String id; // Firebase key
    private String name;
    private int quantity;
    private double price;
    private String category;
    private String notes;
    private String date;
    private Date completionDate;

    public ShoppingItem() {
        // Default constructor required for calls to DataSnapshot.getValue(ShoppingItem.class)

    }

    public ShoppingItem(String name, int quantity, double price, String category, String notes, String date) {
        this.id = "";
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.notes = notes;
        this.date = date;
        this.completionDate = null; // Default null
    }

    // Add getters and setters for the new 'date' field
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }
}
