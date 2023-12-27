package com.example.booklending.Lending;

import com.example.booklending.model.firebaseBorrowedBook;
import com.example.booklending.model.firebaseBorrowRequest;
import com.example.booklending.model.firebaseBorrowHistory;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class LendingService {

    @Autowired
    private Firestore firestore;

    // Fetching borrowing history for a student
    public List<firebaseBorrowHistory> getBorrowingHistory(String userId) throws ExecutionException, InterruptedException {
        List<firebaseBorrowHistory> history = new ArrayList<>();
        firestore.collection("BorrowHistory")
                .whereEqualTo("userId", userId)
                .get()
                .get()
                .forEach(doc -> history.add(doc.toObject(firebaseBorrowHistory.class)));
        return history;
    }

    // Fetching current borrowed books for a student
    public List<firebaseBorrowedBook> getCurrentBorrowedBooks(String userId) throws ExecutionException, InterruptedException {
        List<firebaseBorrowedBook> borrowedBooks = new ArrayList<>();
        firestore.collection("BorrowedBooks")
                .whereEqualTo("userId", userId)
                .get()
                .get()
                .forEach(doc -> borrowedBooks.add(doc.toObject(firebaseBorrowedBook.class)));
        return borrowedBooks;
    }
    // Fetch available books for borrowing
    public List<firebaseBorrowedBook> getAvailableBooks() throws ExecutionException, InterruptedException {
        List<firebaseBorrowedBook> availableBooks = new ArrayList<>();
        firestore.collection("Books") // Assuming your books are stored in a collection named "Books"
                .whereEqualTo("available", true)
                .get()
                .get()
                .forEach(doc -> availableBooks.add(doc.toObject(firebaseBorrowedBook.class)));
        return availableBooks;
    }

    // Handling borrow requests from students
    public void makeBorrowRequest(firebaseBorrowRequest request) throws ExecutionException, InterruptedException {
        firestore.collection("BorrowRequests")
                .document(request.getRequestId())
                .set(request)
                .get();
    }

    // Processing borrow requests by staff
    public void processBorrowRequest(String requestId, String status) throws ExecutionException, InterruptedException {
        firestore.collection("BorrowRequests")
                .document(requestId)
                .update("status", status)
                .get();
    }

    // Handling book returns
    public void returnBookk(String borrowId) throws ExecutionException, InterruptedException {
        // Update BorrowedBooks collection
        firestore.collection("BorrowedBooks")
                .document(borrowId)
                .update("status", "returned")
                .get();

       
   
        
    }
    public void returnBook(String borrowId) throws ExecutionException, InterruptedException {
        // Retrieve the BorrowedBook details
        firebaseBorrowedBook borrowedBook = firestore.collection("BorrowedBooks")
                .document(borrowId)
                .get()
                .get()
                .toObject(firebaseBorrowedBook.class);

        if (borrowedBook != null) {
            // Update BorrowedBooks collection
            firestore.collection("BorrowedBooks")
                    .document(borrowId)
                    .update("status", "returned")
                    .get();

            // Create and add a new entry to BorrowHistory
            firebaseBorrowHistory historyEntry = new firebaseBorrowHistory(
                    borrowId, // Using borrowId as historyId for simplicity; consider using a unique ID
                    borrowedBook.getUserId(),
                    borrowedBook.getBookId(),
                    borrowedBook.getBorrowedDate(),
                    new Date(), // Current date as the return date
                    "returned"
            );

            firestore.collection("BorrowHistory")
                    .add(historyEntry)
                    .get();
        } 
    
    }}
