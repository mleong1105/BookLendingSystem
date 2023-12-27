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
      
        this.forumDatabaseReference = FirebaseDatabase.getInstance().getReference("forums");
    }

    public CompletableFuture<List<FirebaseForumDetails>> viewForums(String category, String orderBy, boolean ascending) {
        CompletableFuture<List<FirebaseForumDetails>> future = new CompletableFuture<>();
        List<FirebaseForumDetails> forums = new ArrayList<>();

        Query query;
        if ("timestamp".equals(orderBy)) {
            query = forumDatabaseReference.orderByChild("timestamp");
        } else {
      
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

  
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                ex.printStackTrace(); 
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

 
    public CompletableFuture<Void> createComment(String forumId, CommentRequest commentRequest) {
        CompletableFuture<Void> future = new CompletableFuture<>();
    
 
        String commentId = forumDatabaseReference.child(forumId).child("comments").push().getKey();
        

        FirebaseComment comment = new FirebaseComment();
        comment.setCommentId(commentId);
        comment.setAuthorId(commentRequest.getAuthorId());
        comment.setContent(commentRequest.getContent());
    
     
        forumDatabaseReference.child(forumId).child("comments").child(commentId).setValue(comment, (databaseError, databaseReference) ->
                handleCompletion(databaseError, future));
    
        return future;
    }

    
    public CompletableFuture<List<FirebaseForumDetails>> searchForums(String query) {
        CompletableFuture<List<FirebaseForumDetails>> future = new CompletableFuture<>();
        List<FirebaseForumDetails> matchingForums = new ArrayList<>();
    
        
        System.out.println("Received search query: " + query);
    
        forumDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FirebaseForumDetails forum = snapshot.getValue(FirebaseForumDetails.class);
    
             
                        System.out.println("Forum ID: " + forum.getForumId());
                        System.out.println("Forum Title: " + forum.getTitle());
                        System.out.println("Forum Content: " + forum.getContent());
                        System.out.println("Forum Author: " + forum.getAuthorId());
                        System.out.println("Forum Category: " + forum.getCategory());
                        System.out.println("Forum Timestamp: " + forum.getTimestamp());
    
                   
                        if (containsKeyword(forum, query)) {
                            matchingForums.add(forum);
    
                            System.out.println("Matching Forum ID: " + forum.getForumId());
                        }
                    }
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
       
        return forum.getTitle().contains(query) ||
                forum.getContent().contains(query) ||
                forum.getAuthorId().contains(query) ||
                forum.getCategory().contains(query) ||
                forum.getTimestamp().contains(query);
    }
  
    public CompletableFuture<Void> editForumPost(FirebaseForumDetails updatedForumPost) {
        CompletableFuture<Void> future = new CompletableFuture<>();
    
      
        if (updatedForumPost.getForumId() == null) {
            future.completeExceptionally(new IllegalArgumentException("ForumId cannot be null."));
            return future;
        }
    
        DatabaseReference forumRef = forumDatabaseReference.child(updatedForumPost.getForumId());
    
    
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("title", updatedForumPost.getTitle());
        updatedFields.put("content", updatedForumPost.getContent());
        updatedFields.put("timestamp", updatedForumPost.getTimestamp());
        updatedFields.put("authorId", updatedForumPost.getAuthorId());
        updatedFields.put("category", updatedForumPost.getCategory());
    
    
        forumRef.updateChildren(updatedFields, (databaseError, databaseReference) ->
                handleCompletion(databaseError, future));
    
        return future;
    }

  
    public CompletableFuture<Void> deleteForumPost(String forumPostId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        forumDatabaseReference.child(forumPostId).removeValue((databaseError, databaseReference) ->
                handleCompletion(databaseError, future));

        return future;
    }



    private void handleException(Throwable ex, CompletableFuture<?> future) {
        ex.printStackTrace(); 
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
        
    }
}
