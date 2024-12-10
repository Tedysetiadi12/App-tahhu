package com.tahhu.id;

public class Video {
    private String url;
    private int likes;
    private int comments;

    public Video(String url, int likes, int comments) {
        this.url = url;
        this.likes = likes;
        this.comments = comments;
    }

    public String getUrl() {
        return url;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }
}
