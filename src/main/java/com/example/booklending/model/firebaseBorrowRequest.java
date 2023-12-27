package com.example.booklending.model;

import java.util.Date;

public class firebaseBorrowRequest {
    private String requestId;
    private String userId;
    private String bookId;
    private Date requestDate;
    private String status;

    public firebaseBorrowRequest(String requestId, String userId, String bookId, Date requestDate, String status) {
        this.requestId = requestId;
        this.userId = userId;
        this.bookId = bookId;
        this.requestDate = requestDate;
        this.status = status;
    }

    // Getters
    public String getRequestId() { return requestId; }
    public String getUserId() { return userId; }
    public String getBookId() { return bookId; }
    public Date getRequestDate() { return requestDate; }
    public String getStatus() { return status; }
}
