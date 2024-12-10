package com.tahhu.coba;

<<<<<<< HEAD
import java.util.Date;

public class ShoppingItem {
=======
class ShoppingItem {
>>>>>>> 4be5d6164f6112e3ebdf2633fb916f6d8034769b
    private String name;
    private int quantity;
    private double price;
    private String category;
    private String notes;
<<<<<<< HEAD
    private Date completionDate;

    public ShoppingItem(String name, int quantity, double price, String category, String notes) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.notes = notes;
        this.completionDate = null; // Default null
=======
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
>>>>>>> 4be5d6164f6112e3ebdf2633fb916f6d8034769b
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
