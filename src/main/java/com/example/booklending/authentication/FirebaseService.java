package com.example.booklending.authentication;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.booklending.model.FirebaseUserDetails;
import com.example.booklending.util.JSONParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@Service
public class FirebaseService {

    @Value("${firebase.api.key}")
    private String firebaseApiKey;

    public String authenticateFirebase(String email, String password) {
        try {
            // Authenticate the user with email and password using Firebase Authentication REST API
            String firebaseAuthApiUrl = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + firebaseApiKey;
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("email", email);
            requestBody.put("password", password);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                    firebaseAuthApiUrl, HttpMethod.POST, requestEntity, String.class);

            // Process the authentication response
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String userId = JSONParser.getStringValue(responseEntity.getBody(), "localId");

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + userId);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> dataMap = (Map<String, Object>) dataSnapshot.getValue();

                        if (dataMap != null) {
                            String email = "", role = "";

                            // Check if the key exists in the dataSnapshot before extracting the value
                            if (dataMap.containsKey("email")) {
                                email = (String) dataMap.get("email");
                            }

                            if (dataMap.containsKey("role")) {
                                role = (String) dataMap.get("role");
                            }

                            FirebaseUserDetails userDetails = new FirebaseUserDetails(userId, email, role);

                            Authentication authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, null);

                            System.out.println("Authentication token: " + authentication);
                            SecurityContextHolder.getContext().setAuthentication(authentication);                        
                        } else {
                            System.out.println("No data found in above uid");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'onCancelled'");
                    }
                });

                return userId;
            } else {
                // Authentication failed
                if (responseEntity.getStatusCode().value() == 400) {
                    // Handle invalid login credentials or non-existing user account
                    System.out.println("Invalid login credentials or non-existing user account");
                } else {
                    // Handle other HTTP status codes
                    System.out.println("Authentication failed with status code: " + responseEntity.getStatusCode().value());
                }
                return null;
            }
        } catch (HttpClientErrorException.BadRequest e) {
            // Log only if it's not a "INVALID_LOGIN_CREDENTIALS" error
            if (!e.getResponseBodyAsString().contains("INVALID_LOGIN_CREDENTIALS")) {
                e.printStackTrace();
            }
            return null;
        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
            return null;
        }
    }
}