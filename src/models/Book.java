package models;

import java.time.LocalDate;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private boolean isAvailable;
    private LocalDate dateAdded;
    private String borrowedBy; // Member ID who borrowed this book
    private LocalDate borrowDate;
    private LocalDate dueDate;

    // Constructor
    public Book(String bookId, String title, String author, String isbn, String category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.isAvailable = true;
        this.dateAdded = LocalDate.now();
        this.borrowedBy = null;
        this.borrowDate = null;
        this.dueDate = null;
    }

    // Getters
    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public void setBorrowedBy(String borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Business methods
    public void borrowBook(String memberId, int borrowDurationDays) {
        if (this.isAvailable) {
            this.isAvailable = false;
            this.borrowedBy = memberId;
            this.borrowDate = LocalDate.now();
            this.dueDate = LocalDate.now().plusDays(borrowDurationDays);
        }
    }

    public void returnBook() {
        this.isAvailable = true;
        this.borrowedBy = null;
        this.borrowDate = null;
        this.dueDate = null;
    }

    public boolean isOverdue() {
        if (dueDate != null && !isAvailable) {
            return LocalDate.now().isAfter(dueDate);
        }
        return false;
    }

    public long getDaysOverdue() {
        if (isOverdue()) {
            return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Book{ID='%s', Title='%s', Author='%s', Category='%s', Available=%s}",
                bookId, title, author, category, isAvailable);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return bookId.equals(book.bookId);
    }

    @Override
    public int hashCode() {
        return bookId.hashCode();
    }
}