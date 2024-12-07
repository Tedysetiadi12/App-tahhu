package com.tahhu.coba;

public class SpendingItem {

    private String name;
    private int amount;

    public SpendingItem(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
