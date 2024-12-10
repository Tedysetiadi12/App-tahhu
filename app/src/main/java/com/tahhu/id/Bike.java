package com.tahhu.id;
public class Bike {
    private String title, detail;
    private int imageResource;
    private double price;

    public Bike(String title, double price, String detail , int imageResource) {
        this.title = title;
        this.price = price;
        this.detail = detail;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
