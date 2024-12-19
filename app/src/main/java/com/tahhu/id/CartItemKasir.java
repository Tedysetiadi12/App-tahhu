package com.tahhu.id;

public class CartItemKasir {
    private ProductKasir product;
    private int quantity;

    public CartItemKasir(ProductKasir product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductKasir getProduct() { return product; }
    public void setProduct(ProductKasir product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSubtotal() {
        return product != null ? product.getPrice() * quantity : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemKasir that = (CartItemKasir) o;
        return product != null && that.product != null &&
                product.getId().equals(that.product.getId());
    }

    @Override
    public int hashCode() {
        return product != null ? product.getId().hashCode() : 0;
    }
}


