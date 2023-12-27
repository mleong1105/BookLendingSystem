package com.example.booklending.model;

public class firebaseBookDetails {
    private String bookId;
    private String bookName;
    private String author;
    private String genre;
    private String publish_year;
    private boolean available;
    private boolean loaned;

    public firebaseBookDetails(String bookId, String bookName, String author, String genre, String publish_year, boolean available, boolean loaned) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.genre = genre;
        this.publish_year = publish_year;
        this.available = available;
        this.loaned = loaned;
    }


    public String getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getPublish_year() {
        return publish_year;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isLoaned() {
        return loaned;
    }



}