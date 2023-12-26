package com.example.booklending.model;

import java.util.HashSet;

public class FirebaseCommentDetails {

    private String commentId;
    private String bookId;
    private String authorId;
    private String timestamp;
    private String content;
    private HashSet<String> upvoters;
    private HashSet<String> reporters;

    public FirebaseCommentDetails(String bookId, String authorId, String timestamp, String content) {
        this.bookId = bookId;
        this.authorId = authorId;
        this.timestamp = timestamp;
        this.content = content;
        this.upvoters = new HashSet<>();
        this.reporters = new HashSet<>();
    }

    public String getCommentId() {
        return this.commentId;
    }

    public String getBookId() {
        return this.bookId;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getContent() {
        return this.content;
    }

    public HashSet<String> getUpvoters() {
        return this.upvoters;
    }

    public HashSet<String> getReporters() {
        return this.reporters;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUpvoters(HashSet<String> upvoters) {
        this.upvoters = upvoters;
    }

    public void setReporters(HashSet<String> reporters) {
        this.reporters = reporters;
    }

    public void addUpvoter(String upvoter) {
        this.upvoters.add(upvoter);
    }

    public void addReporter(String reporter) {
        this.reporters.add(reporter);
    }

    // Additional methods for getting upvote count and report count

    public int getUpvoteCount() {
        return this.upvoters.size();
    }

    public int getReportCount() {
        return this.reporters.size();
    }
}
