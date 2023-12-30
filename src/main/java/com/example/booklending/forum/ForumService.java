package com.example.booklending.forum;

import com.example.booklending.model.FirebaseForumDetails;
import com.example.booklending.model.ForumCommentDetails;
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

    public CompletableFuture<Void> addCommentToForum(String forumPostId, ForumCommentDetails commentDetails) {
        DatabaseReference forumRef = forumDatabaseReference.child(forumPostId).child("comments");
        String commentId = forumRef.push().getKey();
        commentDetails.setCommentId(commentId);

        CompletableFuture<Void> future = new CompletableFuture<>();
        forumRef.child(commentId).setValue(commentDetails,
                (databaseError, databaseReference) -> handleCompletion(databaseError, future));

        return future;
    }

    public CompletableFuture<List<ForumCommentDetails>> viewCommentsForForum(String forumPostId) {
        CompletableFuture<List<ForumCommentDetails>> future = new CompletableFuture<>();
    
        DatabaseReference commentsRef = forumDatabaseReference.child(forumPostId).child("comments");
        commentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ForumCommentDetails> comments = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ForumCommentDetails comment = snapshot.getValue(ForumCommentDetails.class);
                    comments.add(comment);
                }
                future.complete(comments);
            }
    
            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleCompletion(databaseError, future);
            }
        });
    
        return future;
    }

    public CompletableFuture<List<ForumCommentDetails>> findCommentsByAuthorId(String forumId, String authorId) {
        CompletableFuture<List<ForumCommentDetails>> future = new CompletableFuture<>();
        List<ForumCommentDetails> comments = new ArrayList<>();

        DatabaseReference commentsReference = forumDatabaseReference.child(forumId).child("comments");

        commentsReference.orderByChild("authorId").equalTo(authorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ForumCommentDetails comment = snapshot.getValue(ForumCommentDetails.class);
                    comments.add(comment);
                }
                future.complete(comments);
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseForumDetails forum = snapshot.getValue(FirebaseForumDetails.class);
                    forums.add(forum);
                }

                future.complete(forums);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleCompletion(databaseError, future);
            }
        });

        return future;
    }

    public CompletableFuture<List<FirebaseForumDetails>> viewForums(String category, String orderBy,
            boolean ascending) {
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseForumDetails forum = snapshot.getValue(FirebaseForumDetails.class);
                    forums.add(forum);
                }

                sortForums(forums, orderBy, ascending);

                future.complete(forums);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleCompletion(databaseError, future);
            }
        });

        return future;
    }

    public CompletableFuture<Void> createForumPost(FirebaseForumDetails forumPost) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String forumId = forumDatabaseReference.push().getKey();
        forumPost.setForumId(forumId);

        forumDatabaseReference.child(forumId).setValue(forumPost,
                (databaseError, databaseReference) -> handleCompletion(databaseError, future));

        return future;
    }

    public CompletableFuture<Void> editForumPost(FirebaseForumDetails updatedForumPost) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    DatabaseReference forumRef = forumDatabaseReference.child(updatedForumPost.getForumId());

    // Use updateChildren to only update specific fields without affecting others
    Map<String, Object> updates = new HashMap<>();
    updates.put("title", updatedForumPost.getTitle());
    updates.put("content", updatedForumPost.getContent());
    updates.put("category", updatedForumPost.getCategory());
    updates.put("authorId", updatedForumPost.getAuthorId());

    forumRef.updateChildren(updates, (databaseError, databaseReference) -> handleCompletion(databaseError, future));

    return future;
}

    public CompletableFuture<Void> deleteForumPost(String forumPostId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        forumDatabaseReference.child(forumPostId)
                .removeValue((databaseError, databaseReference) -> handleCompletion(databaseError, future));

        return future;
    }

    public CompletableFuture<List<FirebaseForumDetails>> findForumsByAuthorId(String authorId) {
        CompletableFuture<List<FirebaseForumDetails>> future = new CompletableFuture<>();
        List<FirebaseForumDetails> forums = new ArrayList<>();
    
        forumDatabaseReference.orderByChild("authorId").equalTo(authorId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            FirebaseForumDetails forum = snapshot.getValue(FirebaseForumDetails.class);
                            forums.add(forum);
                        }
                        future.complete(forums);
                    }
    
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        handleCompletion(databaseError, future);
                    }
                });
    
        return future;
    }

    private void handleCompletion(DatabaseError databaseError, CompletableFuture<?> future) {
        if (databaseError == null) {
            future.complete(null);
        } else {
            future.completeExceptionally(databaseError.toException());
        }
    }

    // Method to sort forums based on orderBy and order
    private void sortForums(List<FirebaseForumDetails> forums, String orderBy, boolean ascending) {
        switch (orderBy) {
            case "timestamp":
                forums.sort((f1, f2) -> ascending ? f1.getTimestamp().compareTo(f2.getTimestamp())
                        : f2.getTimestamp().compareTo(f1.getTimestamp()));
                break;
            default:
                // Default sorting by timestamp if orderBy is not recognized
                forums.sort((f1, f2) -> ascending ? f1.getTimestamp().compareTo(f2.getTimestamp())
                        : f2.getTimestamp().compareTo(f1.getTimestamp()));
        }
    }
}
