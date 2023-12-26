package com.example.booklending.forum;

import com.example.booklending.model.FirebaseForumDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<Void>> createForumPost(@RequestBody FirebaseForumDetails forumPost) {
        return forumModule.createForumPost(forumPost)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @PutMapping("/edit")
    public CompletableFuture<ResponseEntity<Void>> editForumPost(@RequestBody FirebaseForumDetails updatedForumPost) {
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

    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<List<FirebaseForumDetails>>> searchForums(@RequestParam String query) {
        return forumModule.searchForums(query)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<List<FirebaseForumDetails>>body(null));
    }
}
