package com.tahhu.coba;

public class Product {
    private String name;
    private String price;
    private int imageResource;
    private String rating;
    private String soldCount;
    private String description;  // Tambahkan variabel untuk deskripsi produk

    // Constructor yang menerima deskripsi
    public Product(String name, String price, int imageResource, String rating, String soldCount, String description) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.rating = rating;
        this.soldCount = soldCount;
        this.description = description;  // Setel deskripsi
    }

    // Getters
    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getImageResource() { return imageResource; }
    public String getRating() { return rating; }
    public String getSoldCount() { return soldCount; }
    public String getDescription() { return description; }  // Getter untuk deskripsi


}
