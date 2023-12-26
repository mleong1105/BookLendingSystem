package com.example.booklending.forum;

import com.example.booklending.model.FirebaseForumDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.booklending.model.FirebaseComment;
import com.example.booklending.forum.ForumPostRequest;
import com.example.booklending.forum.ForumService;
import com.example.booklending.forum.ForumController;


import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ForumModule {

    private final ForumService forumService;

    @Autowired
    public ForumModule(ForumService forumService) {
        this.forumService = forumService;
    }

    public CompletableFuture<Void> createComment(String forumId, CommentRequest commentRequest) {
        return forumService.createComment(forumId, commentRequest);
    }

    public CompletableFuture<List<FirebaseForumDetails>> viewForums(String category, String orderBy, boolean ascending) {
        return forumService.viewForums(category, orderBy, ascending);
    }

    public CompletableFuture<List<FirebaseForumDetails>> viewAllForums() {
        return forumService.viewAllForums();
    }

    public CompletableFuture<Void> createForumPost(ForumPostRequest forumPostRequest) {
        FirebaseForumDetails forumPost = new FirebaseForumDetails();
        forumPost.setAuthorId(forumPostRequest.getAuthorId());
        forumPost.setCategory(forumPostRequest.getCategory());
        forumPost.setTitle(forumPostRequest.getTitle());
        forumPost.setContent(forumPostRequest.getContent());

        return forumService.createForumPost(forumPost);
    }

    public CompletableFuture<Void> editForumPost(String forumId, ForumPostRequest updatedForumPostRequest) {
        FirebaseForumDetails updatedForumPost = new FirebaseForumDetails();
        updatedForumPost.setForumId(updatedForumPostRequest.getForumId());
        updatedForumPost.setAuthorId(updatedForumPostRequest.getAuthorId());
        updatedForumPost.setCategory(updatedForumPostRequest.getCategory());
        updatedForumPost.setTitle(updatedForumPostRequest.getTitle());
        updatedForumPost.setContent(updatedForumPostRequest.getContent());

        return forumService.editForumPost(updatedForumPost);
    }

    public CompletableFuture<Void> deleteForumPost(String forumPostId) {
        return forumService.deleteForumPost(forumPostId);
    }



    public CompletableFuture<List<FirebaseForumDetails>> searchForums(String query) {
        return forumService.searchForums(query);
    }
}
