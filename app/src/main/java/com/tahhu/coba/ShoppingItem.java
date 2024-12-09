package com.tahhu.coba;

import java.util.ArrayList;
import java.util.List;

class ShoppingItem {
    private String name;
    private int quantity;
    private double price;
    private String category;
    private String notes;
    public ShoppingItem(String name, int quantity, double price, String category, String notes) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.category = category;
            this.notes = notes;
        }

        // Getters and setters

        public double getPrice() {
            return price;
        }
        public int getQuantity() {
            return quantity;
        }
        public String getName() {
            return name;
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
    }
