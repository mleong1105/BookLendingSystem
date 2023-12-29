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

    @GetMapping("/view")
    public CompletableFuture<ResponseEntity<List<FirebaseCommentDetails>>> viewComments(
            @RequestParam(name = "bookId", required = true) String bookId,
            @RequestParam(name = "orderBy", required = false, defaultValue = "timestamp") String orderBy,
            @RequestParam(name = "ascending",required = false, defaultValue = "true") boolean ascending) {
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

    @DeleteMapping("/delete")
    public CompletableFuture<ResponseEntity<Void>> deleteComment(@RequestParam(name = "commentId", required = true) String commentId) {
        return commentService.deleteComment(commentId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @PostMapping("/upvote")
    public CompletableFuture<ResponseEntity<Void>> upvoteComment(
            @RequestParam(name = "commentId", required = true) String commentId,
            @RequestParam(name = "userId", required = true) String userId) {
        return commentService.upvoteComment(commentId, userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @PostMapping("/undoUpvote")
    public CompletableFuture<ResponseEntity<Void>> undoUpvoteComment(
            @RequestParam(name = "commentId", required = true) String commentId,
            @RequestParam(name = "userId", required = true) String userId) {
        return commentService.undoUpvoteComment(commentId, userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @PostMapping("/report")
    public CompletableFuture<ResponseEntity<Void>> reportComment(
            @RequestParam(name = "commentId", required = true) String commentId,
            @RequestParam(name = "userId", required = true) String userId) {
        return commentService.reportComment(commentId, userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @PostMapping("/undoReport")
    public CompletableFuture<ResponseEntity<Void>> undoReportComment(
            @RequestParam(name = "commentId", required = true) String commentId,
            @RequestParam(name = "userId", required = true) String userId) {
        return commentService.undoReportComment(commentId, userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Void>body(null));
    }

    @GetMapping("/upvoteCount")
    public CompletableFuture<ResponseEntity<Integer>> getUpvoteCount(@RequestParam(name = "commentId", required = true) String commentId) {
        return commentService.getUpvoteCount(commentId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Integer>body(null));
    }

    @GetMapping("/reportCount")
    public CompletableFuture<ResponseEntity<Integer>> getReportCount(@RequestParam(name = "commentId", required = true) String commentId) {
        return commentService.getReportCount(commentId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(500).<Integer>body(null));
    }
}
