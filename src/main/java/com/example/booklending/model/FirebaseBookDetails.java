package com.example.booklending.model;

public class FirebaseBookDetails {
    private String bookId;
    private String bookName;
    private String author;
    private String genre;
    private String publish_year;
    private boolean available;
    private boolean loaned;
    private String loaned_To;

    public FirebaseBookDetails(String bookId, String bookName, String author, String genre, String publish_year, boolean available, boolean loaned, String loaned_To) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.genre = genre;
        this.publish_year = publish_year;
        this.available = available;
        this.loaned = loaned;
        this.loaned_To = loaned_To;

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

    public String getLoaned_To() {
        return loaned_To;
    }

}
