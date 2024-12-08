package com.tahhu.coba;

import java.io.Serializable;

public class CartProduct implements Serializable {
    private Product product;
    private int quantity;
    private String productImageUrl;
    public CartProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
        }
    }

    // Helper methods for easier access to product details
    public String getProductName() {
        return product.getName();
    }

    public String getProductPrice() {
        return product.getPrice();
    }

    // Tambahkan getter dan setter untuk productImageUrl
    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
}
