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
    private List<ForumCommentDetails> comments;

    // Default constructor for Firebase serialization
    public FirebaseForumDetails() {
        this.upvoters = new ArrayList<>();
        this.reporters = new ArrayList<>();
    }

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

    public List<ForumCommentDetails> getComments() {
        return comments;
    }

    public void setComments(List<ForumCommentDetails> comments) {
        this.comments = comments;
    }

    public String getForumId() {
        return this.forumId;
    }

    public String getCategory() {
        return this.category;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public List<String> getUpvoters() {
        return this.upvoters;
    }

    public List<String> getReporters() {
        return this.reporters;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUpvoters(List<String> upvoters) {
        this.upvoters = upvoters;
    }

    public void setReporters(List<String> reporters) {
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
