package com.example.booklending.SmartBookRecommendation;

import org.springframework.stereotype.Component;

import com.example.booklending.model.FirebaseBookDetails;
import com.google.firebase.database.DatabaseReference;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class SBRComponent {
    
    @Autowired
    private DatabaseReference databaseReference;

    public CompletableFuture<List<FirebaseBookDetails>> getRecommendedBooks(String userId) {
        CompletableFuture<List<FirebaseBookDetails>> future = new CompletableFuture<>();
    
        DatabaseReference userRef = databaseReference.child("users").child(userId).child("preferedGenres");
    
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> genres = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        genres.add(snapshot.getValue().toString());
                    }
    
                    List<CompletableFuture<List<FirebaseBookDetails>>> genreFutures = genres.stream()
                            .map(genre -> fetchBooksForGenre(genre))
                            .collect(Collectors.toList());
    
                    CompletableFuture.allOf(genreFutures.toArray(new CompletableFuture[0]))
                            .thenAccept(v -> {
                                List<FirebaseBookDetails> recommendedBooks = genreFutures.stream()
                                        .flatMap(f -> f.join().stream())
                                        .collect(Collectors.toList());
                                future.complete(recommendedBooks);
                            })
                            .exceptionally(ex -> {
                                future.completeExceptionally(ex);
                                return null;
                            });
                } else {
                    future.complete(Collections.emptyList());
                }
            }
    
            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
    
        return future;
    }
    
    private CompletableFuture<List<FirebaseBookDetails>> fetchBooksForGenre(String genre) {
        CompletableFuture<List<FirebaseBookDetails>> genreFuture = new CompletableFuture<>();
        DatabaseReference booksRef = databaseReference.child("books");
    
        booksRef.orderByChild("genre").equalTo(genre).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FirebaseBookDetails> books = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, Object> bookMap = (HashMap<String, Object>) snapshot.getValue();
                        String bookId = (String) bookMap.get("bookId");
                        String bookName = (String) bookMap.get("bookName");
                        String author = (String) bookMap.get("author");
                        String genre = (String) bookMap.get("genre");
                        String publish_year = (String) bookMap.get("publish_year");
                        boolean available = (boolean) bookMap.get("available");
                        boolean loaned = (boolean) bookMap.get("loaned");
                        FirebaseBookDetails book = new FirebaseBookDetails(bookId, bookName, author, genre, publish_year, available, loaned);
                        books.add(book);
                    }
                }
                genreFuture.complete(books);
            }
    
            @Override
            public void onCancelled(DatabaseError databaseError) {
                genreFuture.completeExceptionally(databaseError.toException());
            }
        });
    
        return genreFuture;
    }
    

    public CompletableFuture<List<String>> getFavoriteGenres(String userId) {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        List<String> genres = new ArrayList<>();
        DatabaseReference userRef = databaseReference.child("users").child(userId).child("preferedGenres");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                	for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                		genres.add(snapshot.getValue().toString());
                	}
                	future.complete(genres);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

}
