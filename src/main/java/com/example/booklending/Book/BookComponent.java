package com.example.booklending.Book;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.booklending.model.BookDetails;

@Component
public class BookComponent {
    @Autowired
    BookService bookService;

    public List<BookDetails> getListOfBooks() {
        return bookService.viewListOfBooks();
    }

    public CompletableFuture<BookDetails> getBookDetails(String bookID) {
        CompletableFuture<BookDetails> searchResult = bookService.viewBookDetails(bookID);

        searchResult.thenAccept(result -> {
            if (result != null) {
                System.out.println("Book: " + result);
            } else {
                System.out.println("Error");
            }
        }).exceptionally(exception -> {
            // Handle exceptions
            System.out.println("Exception during fetching: " + exception.getMessage());
            return null;
        });
        return searchResult;
    }
}
