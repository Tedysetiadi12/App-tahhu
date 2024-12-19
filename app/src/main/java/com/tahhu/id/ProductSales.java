package com.tahhu.id;

public class ProductSales {
    private String productName;
    private int quantity;

    public ProductSales(String productName) {
        this.productName = productName;
        this.quantity = 0;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
}
