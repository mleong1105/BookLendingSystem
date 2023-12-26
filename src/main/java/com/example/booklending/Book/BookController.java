package com.example.booklending.Book;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.booklending.model.BookDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class BookController {

    @Autowired
    BookComponent bookComponent;

    @Autowired
    BookRepository bookRepository;

    @PostMapping("/addBook")
    public boolean addBook(@RequestParam(name = "bookName") String bookName,
            @RequestParam(name = "bookID") String bookID,
            @RequestParam(name = "author") String author, @RequestParam(name = "yearPublished") String yearPublished,
            @RequestParam(name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            BookDetails bookDetails = new BookDetails(bookName, bookID, author, yearPublished);
            return bookRepository.addBook(bookDetails);

        } else {
            return false;
        }
    }

    @PostMapping("/updateBook")
    public BookDetails updateBook(@RequestParam(name = "bookName", required = false) String bookName,
            @RequestParam(name = "bookID") String bookID,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "yearPublished", required = false) String yearPublished,
            @RequestParam(name = "loaned", required = false) boolean loaned,
            @RequestParam(name = "isAvailable", required = false) boolean isAvailable,
            @RequestParam(name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            String newBookName = "", newAuthor = "", newYearPublished = "";
            BookDetails updateBook = bookComponent.getBookDetails(bookID);
            if (updateBook != null) {
                if (bookName == null)
                    newBookName = updateBook.getBookName();
                else
                    newBookName = bookName;
                if (author == null)
                    newAuthor = updateBook.getAuthor();
                else
                    newAuthor = author;
                if (yearPublished == null)
                    newYearPublished = updateBook.getPublish_year();

                BookDetails newBookDetails = new BookDetails(newBookName, bookID, newAuthor, newYearPublished);
                if (loaned)
                    newBookDetails.setLoaned(true);
                else
                    newBookDetails.setLoaned(false);

                if (isAvailable)
                    newBookDetails.setAvailable(true);
                else
                    newBookDetails.setAvailable(false);

                return bookRepository.updateBook(newBookDetails);
            } else {
                System.out.println("BOOK NOT FOUND, PLEASE ENTER CORRECT BOOKID");
                return null;
            }
        } else {
            System.out.println("ONLY ADMIN CAN UPDATE BOOK");
            return null;
        }
    }

    @PostMapping("/updateBooksFromAPI")
    public boolean updateBooksFromAPI() {
        boolean result = bookRepository.addBookFromAPI();
        if (result)
            System.out.println("ADD BOOK(S) SUCCESSFULLY");
        else
            System.out.println("ADD FAILED");
        return result;
    }

    @PostMapping("/removeBooks/{id}")
    public BookDetails removeBookViaID(@PathVariable String id, boolean isAdmin) {
        if (isAdmin) {
            CompletableFuture<BookDetails> deletionResult = bookRepository.removeBook(id);
            try {
                // Wait for the asynchronous removal to complete
                BookDetails deletedBook = deletionResult.get();

                if (deletedBook != null) {
                    System.out.println("Book removed: " + deletedBook.getBookName());
                    return deletedBook;
                } else {
                    System.out.println("No matching book or deletion error");
                    return null;
                }
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Exception during book removal: " + e.getMessage());
                return null;
            }
        }
        else {
            System.out.println("ONLY ADMIN CAN REMOVE BOOK");
            return null;
        }
    }

    @GetMapping("/getAllBooks")
    public List<BookDetails> getAllBooks() {
        List<BookDetails> list = bookComponent.getListOfBooks();
        return list;
    }

    @GetMapping("/books/{id}")
    public BookDetails getBookDetailsWithID(@PathVariable String id) {
        return bookComponent.getBookDetails(id);
    }

}
