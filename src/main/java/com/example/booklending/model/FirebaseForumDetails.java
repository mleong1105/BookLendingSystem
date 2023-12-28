package com.example.booklending.model;

import java.util.ArrayList;
import java.util.List;

public class FirebaseForumDetails {

    private String forumId;
    private String category;
    private String authorId;
    private String timestamp;
    private String title;
    private String content;
    private List<String> upvoters;
    private List<String> reporters;
    private List<FirebaseComment> comments;

    // Default constructor with no arguments (required by Firebase)
    public FirebaseForumDetails() {
        // Initialize lists to avoid null references
        this.upvoters = new ArrayList<>();
        this.reporters = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    // Constructor with arguments
    public FirebaseForumDetails(String category, String authorId, String timestamp, String title, String content) {
        this.category = category;
        this.authorId = authorId;
        this.timestamp = timestamp;
        this.title = title;
        this.content = content;
        this.upvoters = new ArrayList<>();
        this.reporters = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    // Getter and setter for forumId
    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    // Getter and setter for category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Getter and setter for authorId
    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    // Getter and setter for timestamp
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // Getter and setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and setter for content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getter and setter for upvoters
    public List<String> getUpvoters() {
        return upvoters;
    }

    public void setUpvoters(List<String> upvoters) {
        this.upvoters = upvoters;
    }

    // Getter and setter for reporters
    public List<String> getReporters() {
        return reporters;
    }

    public void setReporters(List<String> reporters) {
        this.reporters = reporters;
    }

    // Getter and setter for comments
    public List<FirebaseComment> getComments() {
        return comments;
    }

    public void setComments(List<FirebaseComment> comments) {
        this.comments = comments;
    }

    // Additional method to add a comment
    public void addComment(FirebaseComment comment) {
        this.comments.add(comment);
    }
}
