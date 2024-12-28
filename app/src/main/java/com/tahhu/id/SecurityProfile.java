package com.tahhu.id;

public class SecurityProfile {
    private String name;
    private String experience;
    private String skills;
    private int imageResourceId;

    public SecurityProfile(String name, String experience, String skills, int imageResourceId) {
        this.name = name;
        this.experience = experience;
        this.skills = skills;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public String getExperience() {
        return experience;
    }

    public String getSkills() {
        return skills;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}

