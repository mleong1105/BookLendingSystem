package com.example.booklending.authentication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthComponent {
    @Autowired
    private FirebaseService firebaseService;

    public boolean authenticateUserByEmailAndPassword(String email, String password) {
        try {
            // Use Firebase Authentication to verify email and password
            // Note: This is a simplified example; adjust it based on your Firebase setup
            String useruid = firebaseService.authenticateFirebase(email, password);

            // Do additional checks if needed
            if (useruid != null) {
                // Authentication successful
                return true;
            }
        } catch (Error e) {
            // Handle authentication failure
            e.printStackTrace();
        }

        // Authentication failed
        return false;
    }

    public void createUser(String email, String password, String role) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            String uid = userRecord.getUid();

            // Step 2: Update Firebase Realtime Database with user information, including role
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
            Map<String, Object> userData = new HashMap<>();
            userData.put("email", email);
            userData.put("role", role);

            // Use updateChildren instead of setValue
            databaseReference.child(uid).updateChildren(userData, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.err.println("Data could not be saved: " + databaseError.getMessage());
                    } else {
                        System.out.println("Data saved successfully.");
                    }
                }
            });

        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
    }
}
