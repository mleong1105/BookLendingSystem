package com.example.booklending.model;

public class BookDetails {
    private String bookName;
    private String bookId;
    private String author;
    private String publish_year;
    private boolean loaned;
    private boolean isAvailable;

    public BookDetails(String bookName, String bookId, String author, String publish_year) {
        this.bookName = bookName;
        this.bookId = bookId;
        this.author = author;
        this.publish_year = publish_year;
        this.loaned = false;
        this.isAvailable = false;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isLoaned() {
        return loaned;
    }

    public void setLoaned(boolean loaned) {
        this.loaned = loaned;
    }

    public String getPublish_year() {
        return publish_year;
    }

    public void setPublish_year(String publish_year) {
        this.publish_year = publish_year;
    }
}
