package com.example.booklending.comment;

import com.example.booklending.model.FirebaseCommentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/view/{bookId}")
    public CompletableFuture<ResponseEntity<List<FirebaseCommentDetails>>> viewComments(
            @PathVariable String bookId,
            @RequestParam(required = false, defaultValue = "timestamp") String orderBy,
            @RequestParam(required = false, defaultValue = "true") boolean ascending) {
        return commentService.viewComments(bookId, orderBy, ascending)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<List<FirebaseCommentDetails>>body(null));
    }

    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<Void>> addComment(@RequestBody FirebaseCommentDetails comment) {
        return commentService.addComment(comment)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @PutMapping("/edit")
    public CompletableFuture<ResponseEntity<Void>> editComment(@RequestBody FirebaseCommentDetails updatedComment) {
        return commentService.editComment(updatedComment)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @DeleteMapping("/delete/{commentId}")
    public CompletableFuture<ResponseEntity<Void>> deleteComment(@PathVariable String commentId) {
        return commentService.deleteComment(commentId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @PostMapping("/upvote/{commentId}/{userId}")
    public CompletableFuture<ResponseEntity<Void>> upvoteComment(
            @PathVariable String commentId,
            @PathVariable String userId) {
        return commentService.upvoteComment(commentId, userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @PostMapping("/undoUpvote/{commentId}/{userId}")
    public CompletableFuture<ResponseEntity<Void>> undoUpvoteComment(
            @PathVariable String commentId,
            @PathVariable String userId) {
        return commentService.undoUpvoteComment(commentId, userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @PostMapping("/report/{commentId}/{userId}")
    public CompletableFuture<ResponseEntity<Void>> reportComment(
            @PathVariable String commentId,
            @PathVariable String userId) {
        return commentService.reportComment(commentId, userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @PostMapping("/undoReport/{commentId}/{userId}")
    public CompletableFuture<ResponseEntity<Void>> undoReportComment(
            @PathVariable String commentId,
            @PathVariable String userId) {
        return commentService.undoReportComment(commentId, userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @GetMapping("/upvoteCount/{commentId}")
    public CompletableFuture<ResponseEntity<Integer>> getUpvoteCount(@PathVariable String commentId) {
        return commentService.getUpvoteCount(commentId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Integer>body(null));
    }

    @GetMapping("/reportCount/{commentId}")
    public CompletableFuture<ResponseEntity<Integer>> getReportCount(@PathVariable String commentId) {
        return commentService.getReportCount(commentId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Integer>body(null));
    }
}
