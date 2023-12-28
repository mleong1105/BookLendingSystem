package com.example.booklending.model;

public class FirebaseUserDetails {

    private String userId;
    private String email;
    private String role;
    private String preferedGenres;

    public FirebaseUserDetails(String userId, String username, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.preferedGenres = preferedGenres;
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

    public String getPreferedGenres() {
        return preferedGenres;
    }
}
