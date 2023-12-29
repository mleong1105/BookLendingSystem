package com.example.booklending.comment;

import com.example.booklending.model.FirebaseCommentDetails;
import com.google.firebase.database.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CommentService {

    private final DatabaseReference mDatabaseReference;

    public CommentService() {
        // Initialize the Firebase Realtime Database reference
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference("comments");
    }

    private void handleCompletion(DatabaseError databaseError, CompletableFuture<?> future) {
        if (databaseError == null) {
            future.complete(null);
        } else {
            future.completeExceptionally(databaseError.toException());
        }
    }

    public CompletableFuture<List<FirebaseCommentDetails>> viewComments(String bookId, String orderBy, boolean ascending) {
        CompletableFuture<List<FirebaseCommentDetails>> future = new CompletableFuture<>();
        List<FirebaseCommentDetails> comments = new ArrayList<>();
    
        Query query;
        if ("upvoteCount".equals(orderBy)) {
            query = mDatabaseReference.orderByChild("upvotersCount");
        } else if ("reportCount".equals(orderBy)) {
            query = mDatabaseReference.orderByChild("reportersCount");
        } else {
            // Default order by timestamp if orderBy is not recognized
            query = mDatabaseReference.orderByChild("timestamp");
        }
    
        query.equalTo(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseCommentDetails comment = snapshot.getValue(FirebaseCommentDetails.class);
                    comments.add(comment);
                }
    
                // Perform sorting based on order and orderBy
                sortComments(comments, orderBy, ascending);
    
                future.complete(comments);
            }
    
            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleCompletion(databaseError, future);
            }
        });
    
        return future;
    }
    
    // Method to sort comments based on orderBy and order
    private void sortComments(List<FirebaseCommentDetails> comments, String orderBy, boolean ascending) {
        switch (orderBy) {
            case "timestamp":
                comments.sort((c1, c2) -> ascending ? c1.getTimestamp().compareTo(c2.getTimestamp()) : c2.getTimestamp().compareTo(c1.getTimestamp()));
                break;
            case "upvoteCount":
                comments.sort((c1, c2) -> ascending ? Integer.compare(c1.getUpvoteCount(), c2.getUpvoteCount()) : Integer.compare(c2.getUpvoteCount(), c1.getUpvoteCount()));
                break;
            case "reportCount":
                comments.sort((c1, c2) -> ascending ? Integer.compare(c1.getReportCount(), c2.getReportCount()) : Integer.compare(c2.getReportCount(), c1.getReportCount()));
                break;
            default:
                // Default sorting by timestamp if orderBy is not recognized
                comments.sort((c1, c2) -> ascending ? c1.getTimestamp().compareTo(c2.getTimestamp()) : c2.getTimestamp().compareTo(c1.getTimestamp()));
        }
    }

    public CompletableFuture<Void> addComment(FirebaseCommentDetails comment) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String commentId = mDatabaseReference.push().getKey();
        comment.setCommentId(commentId);

        mDatabaseReference.child(commentId).setValue(comment, (databaseError, databaseReference) ->
            handleCompletion(databaseError, future));

        return future;
    }

    public CompletableFuture<Void> deleteComment(String commentId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        mDatabaseReference.child(commentId).removeValue((databaseError, databaseReference) ->
            handleCompletion(databaseError, future));

        return future;
    }

    public CompletableFuture<Void> upvoteComment(String commentId, String userId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        DatabaseReference commentRef = mDatabaseReference.child(commentId);
        commentRef.child("upvoters").child(userId).setValue(true, (databaseError, databaseReference) ->
            handleCompletion(databaseError, future));

        return future;
    }

    public CompletableFuture<Void> undoUpvoteComment(String commentId, String userId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        DatabaseReference commentRef = mDatabaseReference.child(commentId);
        commentRef.child("upvoters").child(userId).removeValue((databaseError, databaseReference) ->
            handleCompletion(databaseError, future));

        return future;
    }

    public CompletableFuture<Void> reportComment(String commentId, String userId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        DatabaseReference commentRef = mDatabaseReference.child(commentId);
        commentRef.child("reporters").child(userId).setValue(true, (databaseError, databaseReference) ->
            handleCompletion(databaseError, future));

        return future;
    }

    public CompletableFuture<Void> undoReportComment(String commentId, String userId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        DatabaseReference commentRef = mDatabaseReference.child(commentId);
        commentRef.child("reporters").child(userId).removeValue((databaseError, databaseReference) ->
            handleCompletion(databaseError, future));

        return future;
    }

    public CompletableFuture<Integer> getUpvoteCount(String commentId) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        DatabaseReference upvoteRef = mDatabaseReference.child(commentId).child("upvoters");
        upvoteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int upvoteCount = (int) dataSnapshot.getChildrenCount();
                future.complete(upvoteCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleCompletion(databaseError, future);
            }
        });

        return future;
    }

    public CompletableFuture<Integer> getReportCount(String commentId) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        DatabaseReference reportRef = mDatabaseReference.child(commentId).child("reporters");
        reportRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int reportCount = (int) dataSnapshot.getChildrenCount();
                future.complete(reportCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleCompletion(databaseError, future);
            }
        });

        return future;
    }
}
