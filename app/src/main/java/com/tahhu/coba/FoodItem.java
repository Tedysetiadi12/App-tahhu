package com.tahhu.coba;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodItem implements Parcelable {
    private String name;
    private int price;
    private int quantity;
    private int imageResId;  // Gambar makanan
    private int cookingTime; // Estimasi waktu masak

    public FoodItem(String name, int price, int imageResId, int cookingTime) {
        this.name = name;
        this.price = price;
        this.quantity = 1;
        this.imageResId = imageResId;
        this.cookingTime = cookingTime;
    }

    protected FoodItem(Parcel in) {
        name = in.readString();
        price = in.readInt();
        quantity = in.readInt();
        imageResId = in.readInt();
        cookingTime = in.readInt();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

    // Getter dan setter untuk atribut baru
    public int getImageResId() {
        return imageResId;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeInt(quantity);
        dest.writeInt(imageResId);
        dest.writeInt(cookingTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
