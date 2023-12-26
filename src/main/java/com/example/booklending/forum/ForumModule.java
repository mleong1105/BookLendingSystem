package com.example.booklending.forum;

import com.example.booklending.model.FirebaseForumDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ForumModule {

    private final ForumService forumService;

    @Autowired
    public ForumModule(ForumService forumService) {
        this.forumService = forumService;
    }

    public CompletableFuture<List<FirebaseForumDetails>> viewForums(String category, String orderBy, boolean ascending) {
        return forumService.viewForums(category, orderBy, ascending);
    }

    public CompletableFuture<Void> createForumPost(FirebaseForumDetails forumPost) {
        return forumService.createForumPost(forumPost);
    }

    public CompletableFuture<Void> editForumPost(FirebaseForumDetails updatedForumPost) {
        return forumService.editForumPost(updatedForumPost);
    }

    public CompletableFuture<Void> deleteForumPost(String forumPostId) {
        return forumService.deleteForumPost(forumPostId);
    }

    public CompletableFuture<List<FirebaseForumDetails>> searchForums(String query) {
        return forumService.searchForums(query);
    }
}
