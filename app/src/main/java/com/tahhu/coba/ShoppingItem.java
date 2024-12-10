package com.tahhu.coba;

class ShoppingItem {
    private String name;
    private int quantity;
    private double price;
    private String category;
    private String notes;
    private String date;
    public ShoppingItem(String name, int quantity, double price, String category, String notes, String date) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.category = category;
            this.notes = notes;
            this.date = date;
    }

        // Getters and setters

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

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
