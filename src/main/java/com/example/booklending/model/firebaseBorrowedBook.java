package com.example.booklending.model;

import java.util.Date;

public class firebaseBorrowedBook {
    private String borrowId;
    private String userId;
    private String bookId;
    private Date borrowedDate;
    private Date dueDate;
    private String status; // e.g., "borrowed", "overdue"

    public firebaseBorrowedBook(String borrowId, String userId, String bookId, Date borrowedDate, Date dueDate, String status) {
        this.borrowId = borrowId;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters
    public String getBorrowId() { return borrowId; }
    public String getUserId() { return userId; }
    public String getBookId() { return bookId; }
    public Date getBorrowedDate() { return borrowedDate; }
    public Date getDueDate() { return dueDate; }
    public String getStatus() { return status; }
}
