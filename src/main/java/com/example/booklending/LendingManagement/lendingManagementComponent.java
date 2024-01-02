package com.example.booklending.LendingManagement;

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
public class lendingManagementComponent {
    @Autowired
    private DatabaseReference databaseReference;


    // Get history of books borrowed by user
    public CompletableFuture<List<FirebaseBookDetails>> getBorrowedBooks(String userId) {
        CompletableFuture<List<FirebaseBookDetails>> future = new CompletableFuture<>();
    
        DatabaseReference userRef = databaseReference.child("users").child(userId).child("BorrowHistory");
    
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> books = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        books.add(snapshot.getValue().toString());
                    }
                    List<CompletableFuture<List<FirebaseBookDetails>>> bookFutures = books.stream()
                            .map(book -> fetchBooksForBook(book))
                            .collect(Collectors.toList());
    
                    CompletableFuture.allOf(bookFutures.toArray(new CompletableFuture[0]))
                            .thenAccept(v -> {
                                List<FirebaseBookDetails> borrowedBooks = bookFutures.stream()
                                        .flatMap(f -> f.join().stream())
                                        .collect(Collectors.toList());
                                future.complete(borrowedBooks);
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

    // fetchBooksForBook method
    private CompletableFuture<List<FirebaseBookDetails>> fetchBooksForBook(String bookId) {
        CompletableFuture<List<FirebaseBookDetails>> future = new CompletableFuture<>();

        DatabaseReference bookRef = databaseReference.child("books").child(bookId);

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FirebaseBookDetails> books = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    HashMap<String, Object> bookMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    String bookId = dataSnapshot.getKey();
                    String title = "", author = "", genre = "", publish_year = "", loaned_To = "";
                    boolean available = false, loaned = false;

                    // Check if the key exists in the dataSnapshot before extracting the value
                    /*
                        private String bookId;
                        private String bookName;
                        private String author;
                        private String genre;
                        private String publish_year;
                        private boolean available;
                        private boolean loaned;
                        private String loaned_To;
                     */
                    if (bookMap.containsKey("bookName")) {
                        title = (String) bookMap.get("bookName");
                    }

                    if (bookMap.containsKey("author")) {
                        author = (String) bookMap.get("author");
                    }

                    if (bookMap.containsKey("genre")) {
                        genre = (String) bookMap.get("genre");
                    }

                    if (bookMap.containsKey("publish_year")) {
                        publish_year = (String) bookMap.get("publish_year");
                    }

                    if (bookMap.containsKey("available")) {
                        available = (boolean) bookMap.get("available");
                    }

                    if (bookMap.containsKey("loaned")) {
                        loaned = (boolean) bookMap.get("loaned");
                    }

                    if (bookMap.containsKey("loaned_To")) {
                        loaned_To = (String) bookMap.get("loaned_To");
                    }

                    books.add(new FirebaseBookDetails(bookId, title, author, genre, publish_year, available, loaned, loaned_To));
                }

                future.complete(books);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }


    // make a borrow request

    public CompletableFuture<String> makeBorrowRequest(String userId, String bookId) {
        CompletableFuture<String> future = new CompletableFuture<>();
        DatabaseReference bookRef = databaseReference.child("books").child(bookId);
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashMap<String, Object> bookMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    boolean available = (boolean) bookMap.get("available");
                    boolean loaned = (boolean) bookMap.get("loaned");
                    if (available && !loaned) {
                        bookMap.put("available", false);
                        bookMap.put("loaned", true);
                        bookMap.put("loaned_To", userId);
    
                        // Update the book details 
                        bookRef.setValue(bookMap, (databaseError, databaseReference) -> {
                            if (databaseError == null) {
   
                                
                            } else {
                                future.completeExceptionally(databaseError.toException());
                            }
                        });

                        // Add the book to the user's borrow history
                        DatabaseReference userRef = databaseReference.child("users").child(userId).child("BorrowHistory");
                        userRef.push().setValue(bookId, (databaseError2, databaseReference2) -> {
                            if (databaseError2 == null) {
                                
                            } else {
                                future.completeExceptionally(databaseError2.toException());
                            }
                        });

                        future.complete("Borrow request is successful");
                    } else {
                        future.complete("Book is not available");
                    }
                } else {
                    future.complete("Book does not exist");
                }
            }
    
            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
    
        return future;
    }

    // Get books currently borrowed by user using the loaned_To field in the book details
    public CompletableFuture<List<FirebaseBookDetails>> getCurrentBorrowedBooks(String userId) {
        CompletableFuture<List<FirebaseBookDetails>> future = new CompletableFuture<>();
    
        DatabaseReference booksRef = databaseReference.child("books");
    
        booksRef.orderByChild("loaned_To").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        String loaned_To = (String) bookMap.get("loaned_To");
                        FirebaseBookDetails book = new FirebaseBookDetails(bookId, bookName, author, genre, publish_year, available, loaned, loaned_To);
                        books.add(book);
                    }
                }
                future.complete(books);
            }
    
            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
    
        return future;
    }

    // Return a book

    public CompletableFuture<String> returnBook(String userId, String bookId) {
        CompletableFuture<String> future = new CompletableFuture<>();
        DatabaseReference bookRef = databaseReference.child("books").child(bookId);
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashMap<String, Object> bookMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    boolean available = (boolean) bookMap.get("available");
                    boolean loaned = (boolean) bookMap.get("loaned");
                    String loaned_To = (String) bookMap.get("loaned_To");
                    if (!available && loaned && loaned_To.equals(userId)) {
                        bookMap.put("available", true);
                        bookMap.put("loaned", false);
                        bookMap.put("loaned_To", "");
    
                        // Update the book details 
                        bookRef.setValue(bookMap, (databaseError, databaseReference) -> {
                            if (databaseError == null) {
                                
                            } else {
                                future.completeExceptionally(databaseError.toException());
                            }
                        });

                        future.complete("Book returned successfully");
                    } else {
                        future.complete("Book is not available");
                    }
                } else {
                    future.complete("Book does not exist");
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
