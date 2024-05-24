package com.example.catch_up.Models;

public class Post {
    private String postUrl;
    private String caption;
    private String uid;
    private String time;

    // Default constructor
    public Post() {
    }

    // Parameterized constructor
    public Post(String postUrl, String caption, String name, String time) {
        this.postUrl = postUrl;
        this.caption = caption;
        this.uid = name;
        this.time = time;
    }

    // Getter for post URL
    public String getPostUrl() {
        return postUrl;
    }

    // Getter for caption
    public String getCaption() {
        return caption;
    }

    // Getter for user ID
    public String getUid() {
        return uid;
    }

    // Getter for post time
    public String getTime() {
        return time;
    }
}
