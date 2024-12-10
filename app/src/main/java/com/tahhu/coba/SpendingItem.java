package com.tahhu.coba;


public class SpendingItem {
    private String name; // Nama libur
    private String dayOfWeek; // Hari dalam seminggu
    private String date; // Tanggal libur

    public SpendingItem(String name, String dayOfWeek, String date) {
        this.name = name;
        this.dayOfWeek = dayOfWeek;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getDate() {
        return date;
    }
}
