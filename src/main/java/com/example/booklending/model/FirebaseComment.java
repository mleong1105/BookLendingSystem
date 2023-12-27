package com.example.booklending.model;

public class FirebaseComment {

    private String commentId;
    private String authorId;
    private String content;

    public FirebaseComment() {
        // Default constructor
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

    // Getter and setter for the timestamp if needed

    // Additional methods if needed
}
