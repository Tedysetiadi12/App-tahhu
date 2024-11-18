package com.tahhu.coba;
public class Bike {
    private String title;
    private int imageResource;
    private double price;

    public Bike(String title, double price, int imageResource) {
        this.title = title;
        this.price = price;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
