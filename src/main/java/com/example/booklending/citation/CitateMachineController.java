package com.example.booklending.citation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CitateMachineController {

    @Autowired
    private CitateMachineComponent citateMachineComponent;

    // Endpoint to add a book to the citation list
    @PostMapping("/api/addCitationBook")
    public ResponseEntity<Map<String, Object>> addBookToCitationList(@RequestBody Map<String, Object> bookData, Authentication authentication) {
        try {
            // Assuming you have a method in CitateMachineComponent to add a book
            Boolean duplicateStatus = citateMachineComponent.addBookToCitationList(bookData, authentication.getName());

            Map<String, Object> response = new HashMap<>();
            response.put("message", !duplicateStatus ? "Book added successfully" : "Item has existed in CitationList");
            response.put("duplicateStatus", duplicateStatus);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to add the book: " + e.getMessage());
            errorResponse.put("duplicateStatus", false);

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
