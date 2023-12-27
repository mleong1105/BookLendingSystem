package com.example.booklending.forum;

import com.example.booklending.model.FirebaseForumDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booklending.forum.ForumService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/forums")
public class ForumController {

    private final ForumModule forumModule;

    @Autowired
    public ForumController(ForumModule forumModule) {
        this.forumModule = forumModule;
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

    @GetMapping("/viewAll")
    public CompletableFuture<ResponseEntity<List<FirebaseForumDetails>>> viewAllForums() {
        return forumModule.viewAllForums()
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<List<FirebaseForumDetails>>body(null));
    }

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<String>> createForumPost(@RequestBody ForumPostRequest forumPostRequest) {
        return forumModule.createForumPost(forumPostRequest)
                .thenApply(response -> ResponseEntity.ok("Forum post created successfully"))
                .exceptionally(ex -> ResponseEntity.status(500).body("Failed to create forum post"));
    }

    @PostMapping("/{forumId}/comments")
    public CompletableFuture<ResponseEntity<String>> createComment(
            @PathVariable String forumId,
            @RequestBody CommentRequest commentRequest) {
        return forumModule.createComment(forumId, commentRequest)
                .thenApply(response -> ResponseEntity.ok("Comment created successfully"))
                .exceptionally(ex -> ResponseEntity.status(500).body("Failed to create comment"));
    }

    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<List<FirebaseForumDetails>>> searchForums(
            @RequestParam String query) {
        return forumModule.searchForums(query)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<List<FirebaseForumDetails>>body(null));
    }

    @PutMapping("/edit/{forumId}")
    public CompletableFuture<ResponseEntity<String>> editForumPost(
            @PathVariable String forumId,
            @RequestBody ForumPostRequest updatedForumPostRequest) {
        return forumModule.editForumPost(forumId, updatedForumPostRequest)
                .thenApply(response -> ResponseEntity.ok("Forum post updated successfully"))
                .exceptionally(ex -> ResponseEntity.status(500).body("Failed to update forum post"));
    }

    @DeleteMapping("/delete/{forumId}")
    public CompletableFuture<ResponseEntity<String>> deleteForumPost(@PathVariable String forumId) {
        return forumModule.deleteForumPost(forumId)
                .thenApply(response -> ResponseEntity.ok("Forum post deleted successfully"))
                .exceptionally(ex -> ResponseEntity.status(500).body("Failed to delete forum post"));
    }
}
