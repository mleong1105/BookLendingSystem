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
            @RequestParam(name = "source") String source,
            @RequestParam(name = "author") String author, @RequestParam(name = "yearPublished") String yearPublished,
            @RequestParam(name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            BookDetails bookDetails = new BookDetails(bookName, author, yearPublished, source);
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
            @RequestParam(name = "isAdmin") boolean isAdmin,
            @RequestParam(name = "source", required = false) String source,
            @RequestParam(name = "loaned_to", required = false) String loaned_to) {
        if (isAdmin) {
            String newBookName = "", newAuthor = "", newYearPublished = "", newSource = "", newLoanedTo = "";
            CompletableFuture<BookDetails> result = bookComponent.getBookDetails(bookID);
            try {
                // Wait for the asynchronous removal to complete
                BookDetails currentBook = result.get();

                if (currentBook != null) {
                    if (bookName == null)
                        newBookName = currentBook.getBookName();
                    else
                        newBookName = bookName;
                    if (author == null)
                        newAuthor = currentBook.getAuthor();
                    else
                        newAuthor = author;
                    if (yearPublished == null)
                        newYearPublished = currentBook.getPublish_year();
                    if (source == null)
                        newSource = currentBook.getSource();
                    else
                        newSource = source;
                    if (loaned_to == null)
                        newLoanedTo = currentBook.getLoaned_to();
                    else
                        newLoanedTo = loaned_to;

                    BookDetails newBookDetails = new BookDetails(newBookName, newAuthor, newYearPublished, newSource);
                    if (loaned)
                        newBookDetails.setLoaned(true);
                    else
                        newBookDetails.setLoaned(false);

                    if (isAvailable)
                        newBookDetails.setAvailable(true);
                    else
                        newBookDetails.setAvailable(false);

                    return bookRepository.updateBook(newBookDetails, bookID);
                } else {
                    System.out.println("No matching book");
                    return null;
                }
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Exception during book removal: " + e.getMessage());
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
        } else {
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
        CompletableFuture<BookDetails> result = bookComponent.getBookDetails(id);
        try {
            // Wait for the asynchronous removal to complete
            BookDetails resultBook = result.get();

            if (resultBook != null) {
                System.out.println("Book: " + resultBook.getBookName());
                return resultBook;
            } else {
                System.out.println("No matching book");
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Exception during fetching: " + e.getMessage());
            return null;
        }
    }

}
