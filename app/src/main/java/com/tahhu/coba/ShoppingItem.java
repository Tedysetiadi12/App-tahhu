package com.tahhu.coba;

import java.util.Date;

public class ShoppingItem {
    private String name;
    private int quantity;
    private double price;
    private String category;
    private String notes;
    private Date completionDate;

    public ShoppingItem(String name, int quantity, double price, String category, String notes) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.notes = notes;
        this.completionDate = null; // Default null
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
