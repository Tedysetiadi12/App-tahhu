package com.tahhu.id;

public class CCTVData {
    private final String name;
    private final String location;
    private final String videoUrl;

    public CCTVData(String name, String location, String videoUrl) {
        this.name = name;
        this.location = location;
        this.videoUrl = videoUrl;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
