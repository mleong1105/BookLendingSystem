package com.example.booklending.search;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.booklending.model.BookDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.springframework.http.ResponseEntity;

@Service
public class SearchService {

    private final DatabaseReference databaseReference;

    public SearchService() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("books");
    }

    

    public CompletableFuture<List<BookDetails>> searchBook(String title, String language, String genre, String yearOfPublish,
    Boolean isAvalibale, Boolean isLoaned, String author) {
        CompletableFuture<List<BookDetails>> future = new CompletableFuture<>();
    
        Query query = databaseReference;
    
      
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BookDetails> matchingBooks = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    System.out.println(dataSnapshot.getValue());
                    BookDetails book = snapshot.getValue(BookDetails.class);

                    if (title == null || book.getBookName().contains(title)) {
                        if (genre == null || book.getGenre().equalsIgnoreCase(genre)) {

                            if (yearOfPublish == null || book.getPublish_year().equalsIgnoreCase(yearOfPublish)) {

                                if (isAvalibale == null || book.isAvailable() == isAvalibale) {

                                    if (isLoaned == null || book.isLoaned() == isLoaned) {

                                        if (author == null || book.getAuthor().equalsIgnoreCase(author)){
                                            
                                            matchingBooks.add(book);
                                        }
                                    }
                                }
                            }
                        }     
                    }

                    
                    
                }
                future.complete(matchingBooks);
            }
    
            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
   
      
        return future;
    }
}
