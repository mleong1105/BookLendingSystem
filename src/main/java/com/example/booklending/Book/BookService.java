package com.example.booklending.Book;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.example.booklending.model.BookDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

@Service
public class BookService {

    public boolean insertSingleBook(BookDetails newBookDetails) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        String bookId = mDatabaseReference.push().getKey();
        List<BookDetails> list = viewListOfBooks();
        AtomicBoolean addSuccessfully = new AtomicBoolean(false);

        boolean checking = false;
        for (BookDetails item : list) {
            if (item.getBookId().equals(newBookDetails.getBookId())) {
                checking = true;
                break;
            }
        }

        if (!checking) {
            CountDownLatch latch = new CountDownLatch(1);
            mDatabaseReference.child("books").child(bookId).setValue(newBookDetails,
                    (error, ref) -> {
                        if (error == null) {
                            System.out.println("ADD SUCCESSFULLY");
                            addSuccessfully.set(true);
                            ;
                        } else {
                            System.out.println(error);
                            addSuccessfully.set(false);
                        }
                        latch.countDown();
                    });
            try {
                latch.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

        } else {
            System.out.println("BOOK ALREADY EXIST");
            addSuccessfully.set(false);
        }

        return addSuccessfully.get();
    }

    public CompletableFuture<BookDetails> removeBook(String id) {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("books");

        CompletableFuture<BookDetails> future = new CompletableFuture<>();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    String bookId = dataSnapshot.child("bookId").getValue(String.class);
                    boolean previousLoanStatus = dataSnapshot.child("loaned").getValue(Boolean.class);
                    boolean previousAvailableStatus = dataSnapshot.child("available").getValue(Boolean.class);
                    System.out.println(bookId+" | "+id);
                    if (bookId.equals(id)) {
                        mDatabaseReference.child(key).removeValue((error, ref) -> {
                            if (error == null) {
                                // Book successfully deleted
                                BookDetails deletedBook = new BookDetails(
                                        dataSnapshot.child("bookName").getValue(String.class),
                                        bookId,
                                        dataSnapshot.child("author").getValue(String.class),
                                        dataSnapshot.child("publish_year").getValue(String.class));
                                deletedBook.setLoaned(previousLoanStatus);
                                deletedBook.setAvailable(previousAvailableStatus);
                                System.out.println("DELETE COMPLETE");
                                future.complete(deletedBook);
                            } else {
                                // Error occurred during deletion
                                System.out.println("Deletion error: " + error);
                                future.completeExceptionally(new RuntimeException("Deletion error"));
                            }
                        });
                        return; // Exit the loop once the book is found and processed
                    }
                }

                // If the loop completes without finding a matching book
                System.out.println("NO MATCHING BOOKID TO DELETE. ID: " + id);
                future.complete(null);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle onCancelled event
                System.out.println("Error: " + error.getMessage());
                future.completeExceptionally(new RuntimeException("Database error"));
            }
        });

        return future;
    }

    public BookDetails updateBookDetails(BookDetails newBookDetails) {
        AtomicReference<BookDetails> returnBookAfterUpdate = new AtomicReference<>(newBookDetails);
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("books");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    String bookId = dataSnapshot.child("bookId").getValue(String.class);
                    String title = dataSnapshot.child("bookName").getValue(String.class);
                    String author = dataSnapshot.child("author").getValue(String.class);
                    String publishYear = dataSnapshot.child("publish_year").getValue(String.class);

                    if (newBookDetails.getBookId().equals(bookId)) {
                        CountDownLatch latch = new CountDownLatch(1);
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("bookName", newBookDetails.getBookName());
                        updates.put("author", newBookDetails.getAuthor());
                        updates.put("publish_year", newBookDetails.getPublish_year());
                        updates.put("loaned", newBookDetails.isLoaned());
                        updates.put("isAvailable", newBookDetails.isAvailable());

                        mDatabaseReference.child(key).updateChildren(updates, (error, ref) -> {
                            if (error == null) {
                                System.out.println("UPDATE SUCCESSFULLY");
                                returnBookAfterUpdate.set(newBookDetails);
                            } else {
                                returnBookAfterUpdate.set(null);
                                ;
                                System.out.println(error);
                            }
                            latch.countDown();
                        });
                        try {
                            latch.await(10, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'onCancelled'");
            }
        });

        return returnBookAfterUpdate.get();
    }

    public boolean insertBookData() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        ObjectMapper ObjectMapper = new ObjectMapper();
        AtomicBoolean addSuccessfully = new AtomicBoolean(false);
        try {
            int counter = 0;
            JsonNode jsonNode = ObjectMapper.readTree(new File("./src/main/resources/data.json"));
            if (jsonNode.isArray()) {
                Iterator<JsonNode> elemIterator = jsonNode.elements();
                CountDownLatch latch = new CountDownLatch(10);
                BookDetails bookDetails;
                while (elemIterator.hasNext()) {
                    counter++;
                    JsonNode bookNode = elemIterator.next();
                    System.out.println("Book Title: " + bookNode.get("title").asText());
                    System.out.println("Author: " + bookNode.get("Author").asText());
                    System.out.println("Publish Year: " + bookNode.get("publishYear").asText());
                    System.out.println("ID: " + bookNode.get("bookID").asText());

                    System.out.println("---------------------");
                    String bookDocId = mDatabaseReference.push().getKey();
                    bookDetails = new BookDetails(bookNode.get("title").asText(), bookNode.get("bookID").asText(),
                            bookNode.get("Author").asText(), bookNode.get("publishYear").asText());
                    mDatabaseReference.child("books").child(bookDocId).setValue(bookDetails,
                            (error, ref) -> {
                                if (error == null) {
                                    System.out.println("INSERT SUCCESSFULLY");
                                    addSuccessfully.set(true);
                                    ;
                                } else {
                                    System.out.println("INSERT FAILED");
                                    addSuccessfully.set(false);
                                }
                                latch.countDown();
                            });
                }
                latch.await();
            } else {
                System.out.println("Invalid JSON format. The root should be an array.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return addSuccessfully.get();
    }

    public BookDetails viewBookDetails(String id) {
        List<BookDetails> list = viewListOfBooks();
        BookDetails result = null;

        for (BookDetails item : list) {
            if (item.getBookId().equals(id)) {
                result = new BookDetails(item.getBookName(), item.getBookId(), item.getAuthor(),
                        item.getPublish_year());
                break;
            }
        }
        return result;
    }

    public List<BookDetails> viewListOfBooks() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("books");
        List<BookDetails>[] listWrapper = new List[] { new ArrayList<>() };
        CountDownLatch latch = new CountDownLatch(1);
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listWrapper[0] = convertToBookDetailsList((Map<String, Map<String, String>>) dataSnapshot.getValue());
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                throw new UnsupportedOperationException("Unimplemented method 'onCancelled'");
            }
        });
        try {
            // Wait for the asynchronous operation to complete
            latch.await(1, TimeUnit.SECONDS); // Adjust the timeout as needed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        return listWrapper[0];
    }

    public static List<BookDetails> convertToBookDetailsList(Map<String, Map<String, String>> data) {
        List<BookDetails> bookDetailsList = new ArrayList<>();

        for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {
            String bookId = entry.getKey();

            if ("counter".equals(bookId)) {
                continue;
            }
            Map<String, String> bookData = entry.getValue();

            BookDetails bookDetails = new BookDetails(bookData.get("bookName"), bookData.get("bookId"),
                    bookData.get("author"), bookData.get("publish_year"));

            bookDetailsList.add(bookDetails);
        }

        return bookDetailsList;
    }
}
