package com.example.booklending.model;

public class FirebaseUserDetails {

    private String userId;
    private String email;
    private String role;

    public FirebaseUserDetails(String userId, String username, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
