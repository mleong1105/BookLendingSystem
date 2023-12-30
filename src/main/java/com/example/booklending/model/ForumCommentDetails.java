package com.example.booklending.model;

public class ForumCommentDetails {

    private String commentId;
    private String authorId;
    private String content;

    // Constructors, getters, and setters...

    public ForumCommentDetails() {
        // Default constructor for Firebase deserialization
    }

    public ForumCommentDetails(String authorId, String content) {
        this.authorId = authorId;
        this.content = content;
        // Initialize any other necessary fields...
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Add getters and setters for any other necessary fields...
}
