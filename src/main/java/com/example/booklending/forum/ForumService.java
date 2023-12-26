package com.example.booklending.forum;

import com.example.booklending.model.FirebaseComment;
import com.example.booklending.model.FirebaseForumDetails;
import com.google.firebase.database.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ForumService {

    private final DatabaseReference forumDatabaseReference;

    public ForumService() {
        // Initialize the Firebase Realtime Database reference for forums
        this.forumDatabaseReference = FirebaseDatabase.getInstance().getReference("forums");
    }

    public CompletableFuture<List<FirebaseForumDetails>> viewForums(String category, String orderBy, boolean ascending) {
        CompletableFuture<List<FirebaseForumDetails>> future = new CompletableFuture<>();
        List<FirebaseForumDetails> forums = new ArrayList<>();

        Query query;
        if ("timestamp".equals(orderBy)) {
            query = forumDatabaseReference.orderByChild("timestamp");
        } else {
            // Default order by timestamp if orderBy is not recognized
            query = forumDatabaseReference.orderByChild("timestamp");
        }

        query.equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FirebaseForumDetails forum = snapshot.getValue(FirebaseForumDetails.class);
                        forums.add(forum);
                    }

                    // Perform sorting based on order and orderBy
                    sortForums(forums, orderBy, ascending);

                    future.complete(forums);
                } catch (Exception e) {
                    handleException(e, future);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleCompletion(databaseError, future);
            }
        });

        return future;
    }

    public CompletableFuture<List<FirebaseForumDetails>> viewAllForums() {
        CompletableFuture<List<FirebaseForumDetails>> future = new CompletableFuture<>();
        List<FirebaseForumDetails> forums = new ArrayList<>();

        forumDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FirebaseForumDetails forum = snapshot.getValue(FirebaseForumDetails.class);
                        forums.add(forum);
                    }

                    future.complete(forums);

                    // Logging
                    System.out.println("Successfully retrieved all forums: " + forums);
                } catch (Exception e) {
                    handleException(e, future);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleCompletion(databaseError, future);
            }
        });

        // Logging
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                ex.printStackTrace(); // Log the exception
            } else {
                System.out.println("Successfully retrieved forums: " + result);
            }
        });

        return future;
    }

    public CompletableFuture<Void> createForumPost(FirebaseForumDetails forumPost) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String forumId = forumDatabaseReference.push().getKey();
        forumPost.setForumId(forumId);

        forumDatabaseReference.child(forumId).setValue(forumPost, (databaseError, databaseReference) ->
                handleCompletion(databaseError, future));

        return future;
    }

    // Create a comment on a specific forum
    public CompletableFuture<Void> createComment(String forumId, CommentRequest commentRequest) {
        CompletableFuture<Void> future = new CompletableFuture<>();
    
        // Create a unique key for the comment
        String commentId = forumDatabaseReference.child(forumId).child("comments").push().getKey();
        
        // Create a FirebaseComment object
        FirebaseComment comment = new FirebaseComment();
        comment.setCommentId(commentId);
        comment.setAuthorId(commentRequest.getAuthorId());
        comment.setContent(commentRequest.getContent());
    
        // Save the comment under the forum post
        forumDatabaseReference.child(forumId).child("comments").child(commentId).setValue(comment, (databaseError, databaseReference) ->
                handleCompletion(databaseError, future));
    
        return future;
    }

    // Search forums
    public CompletableFuture<List<FirebaseForumDetails>> searchForums(String query) {
        CompletableFuture<List<FirebaseForumDetails>> future = new CompletableFuture<>();
        List<FirebaseForumDetails> matchingForums = new ArrayList<>();
    
        // Log the received query
        System.out.println("Received search query: " + query);
    
        forumDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FirebaseForumDetails forum = snapshot.getValue(FirebaseForumDetails.class);
    
                        // Log forum details for debugging
                        System.out.println("Forum ID: " + forum.getForumId());
                        System.out.println("Forum Title: " + forum.getTitle());
                        System.out.println("Forum Content: " + forum.getContent());
                        System.out.println("Forum Author: " + forum.getAuthorId());
                        System.out.println("Forum Category: " + forum.getCategory());
                        System.out.println("Forum Timestamp: " + forum.getTimestamp());
    
                        // Check if the query matches any field (e.g., title, content, authorId)
                        if (containsKeyword(forum, query)) {
                            matchingForums.add(forum);
    
                            // Log the matching forum
                            System.out.println("Matching Forum ID: " + forum.getForumId());
                        }
                    }
    
                    // Log the results
                    System.out.println("Matching Forums: " + matchingForums);
    
                    future.complete(matchingForums);
                } catch (Exception e) {
                    handleException(e, future);
                }
            }
    
            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleCompletion(databaseError, future);
            }
        });
    
        return future;
    }
    
    private boolean containsKeyword(FirebaseForumDetails forum, String query) {
        // Check if the query matches any field in the forum
        return forum.getTitle().contains(query) ||
                forum.getContent().contains(query) ||
                forum.getAuthorId().contains(query) ||
                forum.getCategory().contains(query) ||
                forum.getTimestamp().contains(query);
    }
    // Edit forums
    public CompletableFuture<Void> editForumPost(FirebaseForumDetails updatedForumPost) {
        CompletableFuture<Void> future = new CompletableFuture<>();
    
        // Check if forumId is not null
        if (updatedForumPost.getForumId() == null) {
            future.completeExceptionally(new IllegalArgumentException("ForumId cannot be null."));
            return future;
        }
    
        DatabaseReference forumRef = forumDatabaseReference.child(updatedForumPost.getForumId());
    
        // Extract the fields you want to update
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("title", updatedForumPost.getTitle());
        updatedFields.put("content", updatedForumPost.getContent());
        updatedFields.put("timestamp", updatedForumPost.getTimestamp());
        updatedFields.put("authorId", updatedForumPost.getAuthorId());
        updatedFields.put("category", updatedForumPost.getCategory());
    
        // Add other fields as needed
    
        forumRef.updateChildren(updatedFields, (databaseError, databaseReference) ->
                handleCompletion(databaseError, future));
    
        return future;
    }

    // Delete a forum post
    public CompletableFuture<Void> deleteForumPost(String forumPostId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        forumDatabaseReference.child(forumPostId).removeValue((databaseError, databaseReference) ->
                handleCompletion(databaseError, future));

        return future;
    }

    // Rest of the methods remain unchanged

    private void handleException(Throwable ex, CompletableFuture<?> future) {
        ex.printStackTrace(); // Log the exception
        future.completeExceptionally(ex);
    }

    private void handleCompletion(DatabaseError databaseError, CompletableFuture<?> future) {
        if (databaseError == null) {
            future.complete(null);
        } else {
            handleException(databaseError.toException(), future);
        }
    }

    private void sortForums(List<FirebaseForumDetails> forums, String orderBy, boolean ascending) {
        // Sorting logic
    }
}
