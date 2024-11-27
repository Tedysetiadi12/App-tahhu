package com.tahhu.coba;
import java.io.Serializable;

public class CartProduct implements Serializable{
    private Product product;
    private int quantity;

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
}
