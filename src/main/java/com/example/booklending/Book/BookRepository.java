package com.example.booklending.Book;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.booklending.model.BookDetails;

@Repository
public class BookRepository {

    @Autowired
    BookService bookService;

    public boolean addBookFromAPI() {
        boolean success = bookService.insertBookData();
        return success;
    }

    public boolean addBook(BookDetails newBookDetails) {
        boolean success = bookService.insertSingleBook(newBookDetails);
        return success;
    }

    public BookDetails updateBook(BookDetails newBookDetails, String id) {
        return bookService.updateBookDetails(newBookDetails, id);
    }

    public CompletableFuture<BookDetails> removeBook(String bookID) {
        CompletableFuture<BookDetails> deletionResult = bookService.removeBook(bookID);

        deletionResult.thenAccept(deletedBook -> {
            if (deletedBook != null) {
                // Successfully deleted
                System.out.println("Deleted Book: " + deletedBook);
            } else {
                // No matching book or deletion error
                System.out.println("No matching book or deletion error");
            }
        }).exceptionally(exception -> {
            // Handle exceptions
            System.out.println("Exception during deletion: " + exception.getMessage());
            return null;
        });
        return deletionResult;
    }

}
