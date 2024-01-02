package com.example.booklending.LendingManagement;

import com.example.booklending.model.FirebaseBookDetails;
import com.example.booklending.util.JSONParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/LendingManagemnt")
public class LendingManagementController {
    @Autowired
    private lendingManagementComponent lendingManagementComponent;


    // Endpoint to get history of books borrowed by user and the request body is the user id
    @PostMapping("/getBorrowedBooks")
    public ResponseEntity<Map<String, Object>> getBorrowedBooks(@RequestBody String requestBody) {
        try {

            String userId = JSONParser.getStringValue(requestBody, "userId");

            CompletableFuture<List<FirebaseBookDetails>> borrowedBooks = lendingManagementComponent.getBorrowedBooks(userId);

            // Wait for the completion of the CompletableFuture
            List<FirebaseBookDetails> books = borrowedBooks.get();

            Map<String, Object> response = new HashMap<>();

            response.put("message", "Borrowed books retrieved successfully");
            response.put("borrowedBooks", books);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to get borrowed books: " + e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }

    // Endpoint to make a borrow request and the request body is the user id and the book id
    @PostMapping("/makeBorrowRequest")

    public ResponseEntity<Map<String, Object>> makeBorrowRequest(@RequestBody String requestBody) {
        try {

            String userId = JSONParser.getStringValue(requestBody, "userId");
            String bookId = JSONParser.getStringValue(requestBody, "bookId");

            CompletableFuture<String> borrowRequest = lendingManagementComponent.makeBorrowRequest(userId, bookId);

            // Wait for the completion of the CompletableFuture
            String message = borrowRequest.get();

            Map<String, Object> response = new HashMap<>();
            response.put("message", message);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to make borrow request: " + e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }


    //Endpoint to get currentlly borrowed books and the request body is the user id
    @PostMapping("/getCurrentBorrowedBooks")

    public ResponseEntity<Map<String, Object>> getCurrentBorrowedBooks(@RequestBody String requestBody) {
        try {

            String userId = JSONParser.getStringValue(requestBody, "userId");

            CompletableFuture<List<FirebaseBookDetails>> currentBorrowedBooks = lendingManagementComponent.getCurrentBorrowedBooks(userId);

            // Wait for the completion of the CompletableFuture
            List<FirebaseBookDetails> books = currentBorrowedBooks.get();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Current borrowed books retrieved successfully");
            response.put("currentBorrowedBooks", books);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to get current borrowed books: " + e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    //returnBook
    @PostMapping("/returnBook")

    public ResponseEntity<Map<String, Object>> returnBook(@RequestBody String requestBody) {
        try {

            String userId = JSONParser.getStringValue(requestBody, "userId");
            String bookId = JSONParser.getStringValue(requestBody, "bookId");

            CompletableFuture<String> returnBook = lendingManagementComponent.returnBook(userId, bookId);

            // Wait for the completion of the CompletableFuture
            String message = returnBook.get();

            Map<String, Object> response = new HashMap<>();
            response.put("message", message);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to return book: " + e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
