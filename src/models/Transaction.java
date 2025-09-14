package models;

import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private String memberId;
    private String bookId;
    private TransactionType type;
    private LocalDateTime transactionDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private double fineAmount;
    private String notes;

    public enum TransactionType {
        BORROW,
        RETURN,
        RENEW,
        FINE_PAID
    }

    // Constructor for borrowing
    public Transaction(String transactionId, String memberId, String bookId, 
                      TransactionType type, LocalDateTime dueDate) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.type = type;
        this.transactionDate = LocalDateTime.now();
        this.dueDate = dueDate;
        this.returnDate = null;
        this.fineAmount = 0.0;
        this.notes = "";
    }

    // Constructor for returning
    public Transaction(String transactionId, String memberId, String bookId, 
                      TransactionType type, double fineAmount, String notes) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.type = type;
        this.transactionDate = LocalDateTime.now();
        this.returnDate = LocalDateTime.now();
        this.fineAmount = fineAmount;
        this.notes = notes;
    }

    // Simple constructor
    public Transaction(String transactionId, String memberId, String bookId, TransactionType type) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.type = type;
        this.transactionDate = LocalDateTime.now();
        this.fineAmount = 0.0;
        this.notes = "";
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getBookId() {
        return bookId;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public String getNotes() {
        return notes;
    }

    // Setters
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Business methods
    public boolean isOverdue() {
        if (dueDate != null && returnDate == null) {
            return LocalDateTime.now().isAfter(dueDate);
        }
        return false;
    }

    public long getDaysOverdue() {
        if (isOverdue()) {
            return java.time.Duration.between(dueDate, LocalDateTime.now()).toDays();
        }
        if (returnDate != null && dueDate != null && returnDate.isAfter(dueDate)) {
            return java.time.Duration.between(dueDate, returnDate).toDays();
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Transaction{ID='%s', Member='%s', Book='%s', Type='%s', Date='%s', Fine=$%.2f}",
                transactionId, memberId, bookId, type, 
                transactionDate.toLocalDate(), fineAmount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction that = (Transaction) obj;
        return transactionId.equals(that.transactionId);
    }

    @Override
    public int hashCode() {
        return transactionId.hashCode();
    }
}