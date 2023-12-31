package com.example.booklending.citation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    //hardcode uid for testing
    private String uid = "bNrmYjgcJsgYHm2yQDPwV4r0lHk2";

    // Endpoint to add a book to the citation list
    @PostMapping("/api/addCitationBook")
    // Suppose to pass Authentication obj which store the session token that contains the uid
    public ResponseEntity<Map<String, Object>> addCitationBook(@RequestBody Map<String, Object> bookData) {
        try {
            // Boolean duplicateStatus = citateMachineComponent.addBookToCitationList(bookData, authentication.getName());
            Boolean duplicateStatus = citateMachineComponent.addBookToCitationList(bookData, uid);
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

    @PostMapping("/api/getCitationBookList")
    // Suppose to pass Authentication obj which store the session token that contains the uid
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getCitationBookList() {
        // return citateMachineComponent.getCitationBookList(authentication.getName())
        return citateMachineComponent.getCitationBookList(uid)
        .thenApply(result -> {
            Map<String, Object> response = new HashMap<>();
            response.put("message", (Boolean) result.get("status") ? "Citation Book List get successfully" : "Citation Book List does not exist");
            response.put("existStatus", result.get("status"));

            if((Boolean) result.get("status")) {
                response.put("citationList", result.get("citationList"));
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        })
        .exceptionally(exception -> {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to get the citation list: " + exception.getMessage());
            errorResponse.put("duplicateStatus", false);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        });
    }

    @PostMapping("/api/generateReferenceList")
    // Suppose to pass Authentication obj which store the session token that contains the uid
    public ResponseEntity<Map<String, Object>> generateReferenceList(@RequestBody String citateFormat) {
        try {
            // CompletableFuture<Map<String, Object>> citationBookListFuture = citateMachineComponent.getCitationBookList(authentication.getName());
            CompletableFuture<Map<String, Object>> citationBookListFuture = citateMachineComponent.getCitationBookList(uid);

            Map<String, Object> citationBookListResult = citationBookListFuture.get();
            Boolean citationListExist = (Boolean) citationBookListResult.get("status");

            if (citationListExist) {
                Map<String, Object> bookListRequest = new HashMap<>();
                List<String> citationList = (List<String>) citationBookListResult.get("citationList");
                bookListRequest.put("formatName", citateFormat);
                bookListRequest.put("citationList", citationList);

                CompletableFuture<List<String>> referenceListFuture = citateMachineComponent.generateReferenceList(bookListRequest);

                List<String> references = referenceListFuture.get();

                Map<String, Object> response = new HashMap<>();
                response.put("status", citationListExist);
                response.put("message", "Reference List generated successfully");
                response.put("referenceList", references);

                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", citationListExist);
                errorResponse.put("message", "Citation Book List does not exist");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (InterruptedException | ExecutionException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to generate references: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/removeCitationBook")
    // Suppose to pass Authentication obj which store the session token that contains the uid
    public ResponseEntity<Map<String, Object>> removeCitationBook(@RequestBody String bookuid) {
        try {
            // Boolean duplicateStatus = citateMachineComponent.addBookToCitationList(bookData, authentication.getName());
            CompletableFuture<Boolean> existStatus = citateMachineComponent.removeBookfromCitationList(bookuid, uid);

            Map<String, Object> response = new HashMap<>();
            response.put("message", existStatus.get() ? "Book removed successfully" : "Item does not existed in CitationList");
            response.put("duplicateStatus", existStatus);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to add the book: " + e.getMessage());
            errorResponse.put("duplicateStatus", false);

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
