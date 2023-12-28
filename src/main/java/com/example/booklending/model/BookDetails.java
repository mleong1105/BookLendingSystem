package com.example.booklending.model;

public class BookDetails {
    private String title;
    private String source;
    private String author;
    private String publish_year;
    private boolean loaned;
    private boolean isAvailable;
    private String loaned_to;

    public BookDetails(String bookName, String author, String publish_year, String source) {
        this.title = bookName;
        this.author = author;
        this.publish_year = publish_year;
        this.source = source;
        this.loaned = false;
        this.isAvailable = false;
        this.loaned_to = "";
    }

    public String getBookName() {
        return title;
    }

    public void setBookName(String bookName) {
        this.title = bookName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
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

     public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLoaned_to() {
        return loaned_to;
    }

    public void setLoaned_to(String loaned_to) {
        this.loaned_to = loaned_to;
    }

    public String getPublish_year() {
        return publish_year;
    }

    public void setPublish_year(String publish_year) {
        this.publish_year = publish_year;
    }
}
