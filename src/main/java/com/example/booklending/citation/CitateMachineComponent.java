package com.example.booklending.citation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CitateMachineComponent {

    @Autowired
    private DatabaseReference databaseReference;

    public Boolean addBookToCitationList(Map<String, Object> bookData, String userId) {
        try {
            DatabaseReference userRef = databaseReference.child("users").child(userId).child("citationList");
            Boolean duplicateStatus = addBookForExistingList(bookData, userRef);

            return duplicateStatus;

        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
            throw new RuntimeException("Failed to add book to citationList", e);
        }
    }

    private boolean duplicate = false;

    private boolean addBookForExistingList(Map<String, Object> bookData, DatabaseReference userRef) {
        try {
            // Assuming your bookData contains necessary fields like title, author, etc.
            String bookTitle = (String) bookData.get("title");
            String bookAuthor = (String) bookData.get("author");
            String bookuid = (String) bookData.get("uid");

            DatabaseReference bookchildRef = userRef.child(bookuid);
            bookchildRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        duplicate = true;
                        System.out.println("Item already exists in Citation List");
                    } else {
                        Map<String, Object> bookObject = Map.of(
                            "title", bookTitle,
                            "author", bookAuthor
                        );

                        // Set the book data under the bookUid
                        bookchildRef.setValueAsync(bookObject);
                        System.out.println("Book added successfully");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle onCancelled event
                    throw new RuntimeException("Failed to check book existence in citationList", error.toException());
                }
            });

            Boolean returnDuplicate = duplicate;
            duplicate = false;

            return returnDuplicate;

        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
            throw new RuntimeException("Failed to add book for existing user in citationList", e);
        }
    }
}
