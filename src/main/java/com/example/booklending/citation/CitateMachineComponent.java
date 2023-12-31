package com.example.booklending.citation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class CitateMachineComponent {

    @Autowired
    private DatabaseReference databaseReference;

    //Function 1:
    //Adding book to citation list function

    public Boolean addBookToCitationList(Map<String, Object> bookData, String userId) {
        try {
            DatabaseReference userRef = databaseReference.child("users").child(userId).child("citationList");
            CompletableFuture<Boolean> duplicateStatus = addBookForExistingList(bookData, userRef);

            return duplicateStatus.get();

        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
            throw new RuntimeException("Failed to add book to citationList", e);
        }
    }

    private CompletableFuture<Boolean> addBookForExistingList(Map<String, Object> bookData, DatabaseReference userRef) {
        try {
            // Assuming your bookData contains necessary fields like title, author, etc.
            String bookAuthor = (String) bookData.get("Author");
            String bookYear = (String) bookData.get("Year");
            String bookTitle = (String) bookData.get("Title");
            String bookPublisher = (String) bookData.get("Publisher");
            String bookSource = (String) bookData.get("Source");
            String bookuid = (String) bookData.get("uid");

            DatabaseReference bookchildRef = userRef.child(bookuid);

            CompletableFuture<Boolean> duplicateFuture = new CompletableFuture<>();
            bookchildRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean duplicate = dataSnapshot.exists();
                    duplicateFuture.complete(duplicate);

                    if (!duplicate) {
                        Map<String, Object> bookObject = Map.of(
                            "Author", bookAuthor,
                            "Year", bookYear,
                            "Title", bookTitle,
                            "Publisher", bookPublisher,
                            "Source", bookSource
                        );

                        // Set the book data under the bookUid
                        bookchildRef.setValue(bookObject, (databaseError, databaseReference) -> {
                            if (databaseError != null) {
                                duplicateFuture.completeExceptionally(databaseError.toException());
                            }
                        });
                        System.out.println("Book added successfully");
                    } else {
                        System.out.println("Item has existed in CitationList");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle onCancelled event
                    throw new RuntimeException("Failed to check book existence in citationList", error.toException());
                }
            });

            return duplicateFuture;

        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
            throw new RuntimeException("Failed to add book for existing user in citationList", e);
        }
    }

    //Function 2:
    //Get citation book list function

    public CompletableFuture<Map<String, Object>> getCitationBookList(String userId) {
        CompletableFuture<Map<String, Object>> futureResult = new CompletableFuture<>();

        try {
            DatabaseReference userRef = databaseReference.child("users").child(userId).child("citationList");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> result = new HashMap<>();

                    if (dataSnapshot.exists()) {
                        result.put("status", true);
                        System.out.println("Citation Book List Exist");

                        List<Map<String, Object>> citationList = new ArrayList<>();
                        for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                            String bookName = bookSnapshot.getKey();
                            Map<String, Object> bookData = new HashMap<>();
                            for (DataSnapshot propertySnapshot : bookSnapshot.getChildren()) {
                                String propertyName = propertySnapshot.getKey();
                                Object propertyValue = propertySnapshot.getValue();
                                bookData.put(propertyName, propertyValue);
                            }
                            bookData.put("uid", bookName);
                            citationList.add(bookData);
                        }

                        result.put("citationList", citationList);
                        System.out.println("BookList get successfully");
                    } else {
                        result.put("status", false);
                        System.out.println("Citation Book List Not Exist");
                    }

                    futureResult.complete(result);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle onCancelled event
                    futureResult.completeExceptionally(new RuntimeException("Failed to check citationList", error.toException()));
                }
            });

        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
            throw new RuntimeException("Failed to add book to citationList", e);
        }

        return futureResult;
    }

    //Function 3:
    //Citate the book list and get the reference according to APA format

    public CompletableFuture<List<String>> generateReferenceList(Map<String, Object> bookListRequest) {
        String formatName = (String) bookListRequest.get("formatName");
        List<Map<String, Object>> citateList = (List<Map<String, Object>>) bookListRequest.get("citationList");
    
        DatabaseReference formatRef = databaseReference.child("citationFormat").child(formatName);
    
        CompletableFuture<List<String>> referenceListFuture = new CompletableFuture<>();
    
        formatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String formatString = snapshot.child("formatString").getValue(String.class);
                    List<Map<String, Object>> formatFields = (List<Map<String, Object>>) snapshot.child("field").getValue();
                    formatFields.sort(Comparator.comparingInt(field -> {
                        Object sequenceObject = field.get("sequence");
                        if (sequenceObject instanceof Number) {
                            return ((Number) sequenceObject).intValue();
                        } else {
                            throw new IllegalArgumentException("Sequence value is not a number");
                        }
                    }));
    
                    List<CompletableFuture<String>> referenceFuture = citateList.stream()
                            .map(book -> {
                                Object[] fieldValues = formatFields.stream()
                                        .map(field -> {
                                            String fieldName = (String) field.get("name");
                                            return book.containsKey(fieldName) ? book.get(fieldName).toString() : "";
                                        })
                                        .toArray();
    
                                return CompletableFuture.completedFuture(String.format(formatString, fieldValues));
                            })
                            .collect(Collectors.toList());

                    CompletableFuture<Void> allOf = CompletableFuture.allOf(
                            referenceFuture.toArray(new CompletableFuture[0])
                    );
    
                    allOf.thenAccept(ignoredVoid -> {
                        List<String> references = referenceFuture.stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList());
                        referenceListFuture.complete(references);
                    });
                } else {
                    referenceListFuture.completeExceptionally(new RuntimeException("Citation format not found: " + formatName));
                }
            }
    
            @Override
            public void onCancelled(DatabaseError error) {
                referenceListFuture.completeExceptionally(new RuntimeException("Failed to fetch citation format: " + error.getMessage(), error.toException()));
            }
        });
    
        return referenceListFuture;
    }    

    //Function 4
    //Remove book from Citation Book List

    public CompletableFuture<Boolean> removeBookfromCitationList(String bookuid, String userId) {
        try {
            DatabaseReference userRef = databaseReference.child("users").child(userId).child("citationList").child(bookuid);
            CompletableFuture<Boolean> bookExistsFuture = new CompletableFuture<>();
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean bookExists = dataSnapshot.exists();
                    bookExistsFuture.complete(bookExists);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    bookExistsFuture.completeExceptionally(error.toException());
                }
            });

            Boolean bookExists = bookExistsFuture.get();
            if (bookExists) {
                CompletableFuture<Void> removeFuture = new CompletableFuture<>();
                userRef.removeValue((databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        removeFuture.complete(null);
                    } else {
                        removeFuture.completeExceptionally(databaseError.toException());
                    }
                });
    
                removeFuture.get();
            }
            return CompletableFuture.completedFuture(bookExists);

        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
            throw new RuntimeException("Failed to remove book from citationList", e);
        }
    }
}
