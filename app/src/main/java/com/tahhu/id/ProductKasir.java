package com.tahhu.id;

public class ProductKasir {
    private String id;
    private String name;
    private double price;
    private int stock;

    public ProductKasir() {
        // Default constructor required for calls to DataSnapshot.getValue(ProductKasir.class)
    }

    public ProductKasir(String id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}

