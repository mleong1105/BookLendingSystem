package com.example.booklending.forum;

import com.example.booklending.model.FirebaseForumDetails;
import com.example.booklending.model.ForumCommentDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/forums")
public class ForumController {

    private final ForumModule forumModule;

    @Autowired
    public ForumController(ForumModule forumModule) {
        this.forumModule = forumModule;
    }

    @PostMapping("/{forumPostId}/comments")
    public CompletableFuture<ResponseEntity<Void>> addCommentToForumPost(
            @PathVariable String forumPostId,
            @RequestBody ForumCommentDetails commentDetails) {
        return forumModule.addCommentToForum(forumPostId, commentDetails)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @GetMapping("/{forumId}/comments")
    public CompletableFuture<ResponseEntity<List<ForumCommentDetails>>> viewCommentsForForum(
            @PathVariable String forumId,
            @RequestParam(required = false) String authorId) {
        if (authorId != null) {
            // If authorId is provided, filter comments by authorId
            return forumModule.findCommentsByAuthorId(forumId, authorId)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex -> ResponseEntity.status(500).build());
        } else {
            // If authorId is not provided, retrieve all comments for the forum
            CompletableFuture<List<ForumCommentDetails>> future = forumModule.viewCommentsForForum(forumId);
            try {
                List<ForumCommentDetails> comments = future.get();
                return CompletableFuture.completedFuture(new ResponseEntity<>(comments, HttpStatus.OK));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace(); // Handle the exception appropriately
                return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            }
        }
    }

    @GetMapping("/viewAll")
    public CompletableFuture<ResponseEntity<List<FirebaseForumDetails>>> viewAllForums() {
        return forumModule.viewAllForums()
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<List<FirebaseForumDetails>>body(null));
    }

    @GetMapping("/view/{category}")
    public CompletableFuture<ResponseEntity<List<FirebaseForumDetails>>> viewForums(
            @PathVariable String category,
            @RequestParam(required = false, defaultValue = "timestamp") String orderBy,
            @RequestParam(required = false, defaultValue = "true") boolean ascending) {
        return forumModule.viewForums(category, orderBy, ascending)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<List<FirebaseForumDetails>>body(null));
    }

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<Void>> createForumPost(@RequestBody FirebaseForumDetails forumPost) {
        return forumModule.createForumPost(forumPost)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @PutMapping("/edit/{forumPostId}")
    public CompletableFuture<ResponseEntity<Void>> editForumPost(
            @PathVariable String forumPostId,
            @RequestBody FirebaseForumDetails updatedForumPost) {
        updatedForumPost.setForumId(forumPostId); // Make sure to set the forum ID from the path

        return forumModule.editForumPost(updatedForumPost)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @DeleteMapping("/delete/{forumPostId}")
    public CompletableFuture<ResponseEntity<Void>> deleteForumPost(@PathVariable String forumPostId) {
        return forumModule.deleteForumPost(forumPostId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @GetMapping("/author/{authorId}")
    public CompletableFuture<ResponseEntity<List<FirebaseForumDetails>>> viewForumsByAuthor(
            @PathVariable String authorId) {
        return forumModule.findForumsByAuthorId(authorId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).build());
    }
}
