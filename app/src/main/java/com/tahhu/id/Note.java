package com.tahhu.id;

public class Note {
    private String id; // ID catatan
    private String title; // Judul catatan
    private String content; // Isi catatan
    private String createdAt; // Timestamp pembuatan
    private String updatedAt; // Timestamp pembaruan
    private boolean isPinned; // Status pinned

    // Constructor kosong (dibutuhkan oleh Firebase)
    public Note() {
    }

    // Constructor dengan parameter
    public Note(String id, String title, String content, String createdAt, String updatedAt, boolean isPinned) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isPinned = isPinned;
    }

    // Getter dan Setter untuk ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter dan Setter untuk Title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter dan Setter untuk Content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getter dan Setter untuk CreatedAt
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Getter dan Setter untuk UpdatedAt
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Getter dan Setter untuk IsPinned
    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }
}
