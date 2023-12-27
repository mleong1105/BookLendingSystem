package com.example.booklending.search;

import java.util.concurrent.CompletableFuture;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.booklending.model.BookDetails;

@Component
public class SearchModule { 
    private final SearchService searchService;

    @Autowired
    public SearchModule(SearchService searchService) {
        this.searchService = searchService;
    }

    public CompletableFuture<List<BookDetails>>
    searchBook(String title, String language, String genre, String yearOfPublish, Boolean isAvaliable, Boolean isLoaned, String author) {
        return searchService.searchBook(title, language, genre, yearOfPublish, isAvaliable, isLoaned, author);
    }
}