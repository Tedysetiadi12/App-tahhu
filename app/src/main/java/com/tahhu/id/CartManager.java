package com.tahhu.id;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartProduct> cartProducts;

    //Konstruktor pribadi untuk mencegah banyak kejadian
    private CartManager() {
        cartProducts = new ArrayList<>();
    }

    //Pengambil contoh tunggal
    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Metode untuk menambahkan produk ke keranjang
    public void addProductToCart(Product product) {
        for (CartProduct cartProduct : cartProducts) {
            if (cartProduct.getProduct().getName().equals(product.getName())) {
                //Jika produk sudah ada, tambah jumlahnya
                cartProduct.increaseQuantity();
                return;
            }
        }
        // Jika produk tidak ada di keranjang, tambahkan
        cartProducts.add(new CartProduct(product, 1));
    }

    // Metode untuk memasukkan semua produk ke dalam keranjang
    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    // Metode untuk mendapatkan jumlah total barang di keranjang
    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (CartProduct cartProduct : cartProducts) {
            totalQuantity += cartProduct.getQuantity();
        }
        return totalQuantity;
    }

    // Metode untuk mendapatkan total harga barang di keranjang
    public double getTotalPrice() {
        double totalPrice = 0.0;
        for (CartProduct cartProduct : cartProducts) {
            double price = Double.parseDouble(cartProduct.getProduct().getPrice().replace("Rp", "").replace(",", ""));
            totalPrice += price * cartProduct.getQuantity();
        }
        return totalPrice;
    }
    public void clearCart() {
        cartProducts.clear();
    }
}
