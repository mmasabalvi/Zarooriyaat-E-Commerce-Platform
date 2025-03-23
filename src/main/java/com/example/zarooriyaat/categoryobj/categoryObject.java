package com.example.zarooriyaat.categoryobj;

public class categoryObject {
    private String name;
    private String imageUrl;
    private String link;

    public categoryObject(String name, String imageUrl, String link) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.link = link;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
