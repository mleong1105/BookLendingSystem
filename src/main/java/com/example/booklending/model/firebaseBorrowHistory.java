package com.example.booklending.model;

import java.util.Date;

public class firebaseBorrowHistory {
    private String historyId;
    private String userId;
    private String bookId;
    private Date borrowedDate;
    private Date returnedDate;
    private String status; // e.g., "returned", "damaged"

    public firebaseBorrowHistory(String historyId, String userId, String bookId, Date borrowedDate, Date returnedDate, String status) {
        this.historyId = historyId;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowedDate = borrowedDate;
        this.returnedDate = returnedDate;
        this.status = status;
    }

    // Getters
    public String getHistoryId() { return historyId; }
    public String getUserId() { return userId; }
    public String getBookId() { return bookId; }
    public Date getBorrowedDate() { return borrowedDate; }
    public Date getReturnedDate() { return returnedDate; }
    public String getStatus() { return status; }
}
