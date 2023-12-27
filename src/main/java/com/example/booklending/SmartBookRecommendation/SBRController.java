package com.example.booklending.SmartBookRecommendation;

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
@RequestMapping("/api/SmartBookRecommendation")
public class SBRController {
    
    @Autowired
    private SBRComponent sbrComponent;

    // Endpoint to get recommended books by user prefered genres and the request body is the user id
    @PostMapping("/getRecommendedBooks")
    public ResponseEntity<Map<String, Object>> getRecommendedBooks(@RequestBody String requestBody) {
        try {

            String userId = JSONParser.getStringValue(requestBody, "userId");


            CompletableFuture<List<FirebaseBookDetails>> recommendedBooks = sbrComponent.getRecommendedBooks(userId);

            // Wait for the completion of the CompletableFuture
            List<FirebaseBookDetails> books = recommendedBooks.get();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Recommended books retrieved successfully");
            response.put("recommendedBooks", books);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to get recommended books: " + e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/getFavoriteGenres")
    public ResponseEntity<Map<String, Object>> getFavoriteGenres(@RequestBody String requestBody) {
    try {

        String userId = JSONParser.getStringValue(requestBody, "userId");

        CompletableFuture<List<String>> favoriteGenres = sbrComponent.getFavoriteGenres(userId);

        // Wait for the completion of the CompletableFuture
        List<String> genres = favoriteGenres.get();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Favorite genres retrieved successfully");
        response.put("favoriteGenres", genres);

        return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Failed to get favorite genres: " + e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

}
