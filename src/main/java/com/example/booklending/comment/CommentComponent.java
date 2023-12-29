package com.example.booklending.comment;

import com.example.booklending.model.FirebaseCommentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class CommentComponent {

    private final CommentService commentService;

    @Autowired
    public CommentComponent(CommentService commentService) {
        this.commentService = commentService;
    }

    public CompletableFuture<List<FirebaseCommentDetails>> viewComments(String bookId, String orderBy, boolean ascending) {
        return commentService.viewComments(bookId, orderBy, ascending);
    }

    public CompletableFuture<Void> addComment(FirebaseCommentDetails comment) {
        return commentService.addComment(comment);
    }

    public CompletableFuture<Void> deleteComment(String commentId) {
        return commentService.deleteComment(commentId);
    }

    public CompletableFuture<Void> upvoteComment(String commentId, String userId) {
        return commentService.upvoteComment(commentId, userId);
    }

    public CompletableFuture<Void> undoUpvoteComment(String commentId, String userId) {
        return commentService.undoUpvoteComment(commentId, userId);
    }

    public CompletableFuture<Void> reportComment(String commentId, String userId) {
        return commentService.reportComment(commentId, userId);
    }

    public CompletableFuture<Void> undoReportComment(String commentId, String userId) {
        return commentService.undoReportComment(commentId, userId);
    }

    public CompletableFuture<Integer> getUpvoteCount(String commentId) {
        return commentService.getUpvoteCount(commentId);
    }

    public CompletableFuture<Integer> getReportCount(String commentId) {
        return commentService.getReportCount(commentId);
    }
}
