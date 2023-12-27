package com.example.booklending.Lending;

import com.example.booklending.model.firebaseBorrowHistory;
import com.example.booklending.model.firebaseBorrowRequest;
import com.example.booklending.model.firebaseBorrowedBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/lending")
public class LendingController {

    @Autowired
    private LendingService lendingService;

    // Get borrowing history for a student
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<firebaseBorrowHistory>> getBorrowingHistory(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(lendingService.getBorrowingHistory(userId));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build(); // Consider a more detailed error response
        }
    }

    // Get current borrowed books for a student
    @GetMapping("/currentBorrowed/{userId}")
    public ResponseEntity<List<firebaseBorrowedBook>> getCurrentBorrowedBooks(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(lendingService.getCurrentBorrowedBooks(userId));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build(); // Consider a more detailed error response
        }
    }

    @GetMapping("/availableBooks")
    public ResponseEntity<List<firebaseBorrowedBook>> getAvailableBooks() {
        try {
            return ResponseEntity.ok(lendingService.getAvailableBooks());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build(); // Consider a more detailed error response
        }
    }
    // Create a borrow request
    @PostMapping("/borrow")
    public ResponseEntity<Void> makeBorrowRequest(@RequestBody firebaseBorrowRequest request) {
        try {
            lendingService.makeBorrowRequest(request);
            return ResponseEntity.ok().build();
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build(); // Consider a more detailed error response
        }
    }

    // Process a borrow request by staff
    @PutMapping("/processRequest")
    public ResponseEntity<Void> processBorrowRequest(@RequestParam String requestId, @RequestParam String status) {
        try {
            lendingService.processBorrowRequest(requestId, status);
            return ResponseEntity.ok().build();
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build(); // Consider a more detailed error response
        }
    }

    // Handle book return
    @PutMapping("/returnBook/{borrowId}")
    public ResponseEntity<Void> returnBook(@PathVariable String borrowId) {
        try {
            lendingService.returnBook(borrowId);
            return ResponseEntity.ok().build();
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build(); // Consider a more detailed error response
        }
    }
}
