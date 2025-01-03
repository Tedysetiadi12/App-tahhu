package com.tahhu.id;

// Model class untuk Pengumuman
public class Announcement {
    String title;
    String content;
    String category;
    String timestamp;

    public Announcement(String title, String content, String category, String timestamp) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.timestamp = timestamp;
    }
}
