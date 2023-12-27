package com.example.booklending.forum;

public class ForumPostRequest {

    private String authorId;
    private String category;
    private String title;
    private String content;
    private String forumId;

   

    public ForumPostRequest() {
    }

    public ForumPostRequest(String authorId, String category, String title, String content) {
        this.authorId = authorId;
        this.category = category;
        this.title = title;
        this.content = content;
    }

  

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    
    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
