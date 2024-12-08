package com.tahhu.coba;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Transaction implements Serializable {
    private String transactionId;
    private List<CartProduct> productList;
    private double totalPrice;
    private String paymentMethod;

    // Constructor, getters, and setters
    public Transaction(String transactionId,List<CartProduct> productList, double totalPrice, String paymentMethod) {
        this.transactionId = transactionId;
        this.productList = productList;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public List<CartProduct> getProductList() {
        return productList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

}
