package com.tahhu.coba;
public class Car {
    private String title;

    private int imageResource;

    private double price;

    public Car(String title, double price, int imageResource) {
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

    public int getimageResource() {
        return imageResource;
    }

    public void setimageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
