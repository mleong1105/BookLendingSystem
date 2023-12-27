package com.example.booklending.search;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.booklending.model.BookDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RestController
class SearchController {

    private final SearchModule searchModule;
   

    @Autowired
    public SearchController(SearchModule searchModule) {
        this.searchModule = searchModule;
    }

    

    @GetMapping("/api/search/name")
    public CompletableFuture<ResponseEntity<List<BookDetails>>> searchByBookName(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String yearOfPublish,
            @RequestParam(required = false) Boolean isAvaliable,
            @RequestParam(required = false) Boolean isLoaned,
            @RequestParam(required = false) String author) throws InterruptedException, ExecutionException {
               
           
            return searchModule.searchBook(title, language, genre, yearOfPublish, isAvaliable, isLoaned, author)
              .thenApply(ResponseEntity::ok)
              .exceptionally(ex -> ResponseEntity.status(500).<List<BookDetails>>body(null));  
            }
}