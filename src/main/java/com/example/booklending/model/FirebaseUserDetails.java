package com.example.booklending.model;

public class FirebaseUserDetails {

    private String userId;
    private String email;
    private String role;
    private Object preferedGenres;
    private Object borrow_history;

    public FirebaseUserDetails(String userId, String email, String role, Object preferedGenres, Object borrow_history) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.preferedGenres = preferedGenres;
        this.borrow_history = borrow_history;
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

    public Object getPreferedGenres() {
        return preferedGenres;
    }

    public Object getBorrow_history() {
        return borrow_history;
    }
}
