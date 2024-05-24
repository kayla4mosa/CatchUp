package com.example.catch_up.Models;

public class User {
    public String image = null;
    public String name = null;
    public String email = null;
    public String password = null;

    // Default constructor
    public User() {
    }

    // Parameterized constructor with all fields
    public User(String image, String name, String email, String password) {
        this.image = image;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Constructor without image field
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Constructor without image and name fields
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Setter for image
    public void setImage(String image) {
        this.image = image;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for image
    public String getImage() {
        return image;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }
}
