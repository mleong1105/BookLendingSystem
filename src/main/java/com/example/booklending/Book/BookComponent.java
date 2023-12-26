package com.example.booklending.Book;

import java.util.List;

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

    public BookDetails getBookDetails(String id){
        return bookService.viewBookDetails(id);
    }
}
