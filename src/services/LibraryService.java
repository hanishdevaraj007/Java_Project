package services;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import models.*;

public class LibraryService {
    private Map<String, Book> books;
    private Map<String, Member> members;
    private List<Transaction> transactions;
    private int transactionCounter;

    // Fine calculation constants
    private static final double FINE_PER_DAY = 1.0; // $1 per day overdue
    private static final double MAX_FINE_PER_BOOK = 50.0; // Maximum fine per book

    public LibraryService() {
        this.books = new HashMap<>();
        this.members = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.transactionCounter = 1;
    }

    // Book Management Methods
    public boolean addBook(Book book) {
        if (books.containsKey(book.getBookId())) {
            return false; // Book already exists
        }
        books.put(book.getBookId(), book);
        return true;
    }

    public boolean removeBook(String bookId) {
        Book book = books.get(bookId);
        if (book == null) {
            return false; // Book not found
        }
        if (!book.isAvailable()) {
            return false; // Cannot remove borrowed book
        }
        books.remove(bookId);
        return true;
    }

    public Book getBook(String bookId) {
        return books.get(bookId);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public List<Book> getAvailableBooks() {
        return books.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    // Member Management Methods
    public boolean addMember(Member member) {
        if (members.containsKey(member.getMemberId())) {
            return false; // Member already exists
        }
        members.put(member.getMemberId(), member);
        return true;
    }

    public boolean removeMember(String memberId) {
        Member member = members.get(memberId);
        if (member == null) {
            return false; // Member not found
        }
        if (member.getBorrowedBooksCount() > 0) {
            return false; // Cannot remove member with borrowed books
        }
        members.remove(memberId);
        return true;
    }

    public Member getMember(String memberId) {
        return members.get(memberId);
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    // Book Search Methods
    public List<Book> searchBooksByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByAuthor(String author) {
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByCategory(String category) {
        return books.values().stream()
                .filter(book -> book.getCategory().toLowerCase().contains(category.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooks(String query) {
        String lowerQuery = query.toLowerCase();
        return books.values().stream()
                .filter(book -> 
                    book.getTitle().toLowerCase().contains(lowerQuery) ||
                    book.getAuthor().toLowerCase().contains(lowerQuery) ||
                    book.getCategory().toLowerCase().contains(lowerQuery) ||
                    book.getIsbn().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    // Borrowing and Returning Methods
    public String borrowBook(String memberId, String bookId) {
        Member member = members.get(memberId);
        Book book = books.get(bookId);

        // Validation checks
        if (member == null) {
            return "Member not found!";
        }
        if (book == null) {
            return "Book not found!";
        }
        if (!member.isActive()) {
            return "Member account is inactive!";
        }
        if (!book.isAvailable()) {
            return "Book is not available!";
        }
        if (!member.canBorrowBooks()) {
            return "Member cannot borrow books (fine exceeds limit or account issues)!";
        }
        if (!member.canBorrowMoreBooks()) {
            return "Member has reached maximum book limit!";
        }

        // Process borrowing
        int borrowDuration = member.getMemberType().getBorrowDurationDays();
        book.borrowBook(memberId, borrowDuration);
        member.addBorrowedBook(bookId);

        // Create transaction record
        String transactionId = generateTransactionId();
        LocalDateTime dueDate = LocalDateTime.now().plusDays(borrowDuration);
        Transaction transaction = new Transaction(transactionId, memberId, bookId, 
                                                Transaction.TransactionType.BORROW, dueDate);
        transactions.add(transaction);

        return "Book borrowed successfully! Due date: " + book.getDueDate();
    }

    public String returnBook(String memberId, String bookId) {
        Member member = members.get(memberId);
        Book book = books.get(bookId);

        // Validation checks
        if (member == null) {
            return "Member not found!";
        }
        if (book == null) {
            return "Book not found!";
        }
        if (book.isAvailable()) {
            return "Book is not currently borrowed!";
        }
        if (!book.getBorrowedBy().equals(memberId)) {
            return "This book was not borrowed by this member!";
        }

        // Calculate fine if overdue
        double fineAmount = 0.0;
        String message = "Book returned successfully!";
        
        if (book.isOverdue()) {
            long daysOverdue = book.getDaysOverdue();
            fineAmount = Math.min(daysOverdue * FINE_PER_DAY, MAX_FINE_PER_BOOK);
            member.addFine(fineAmount);
            message += String.format(" Fine of $%.2f applied for %d days overdue.", 
                                   fineAmount, daysOverdue);
        }

        // Process return
        book.returnBook();
        member.removeBorrowedBook(bookId);

        // Create transaction record
        String transactionId = generateTransactionId();
        String notes = fineAmount > 0 ? "Returned late" : "Returned on time";
        Transaction transaction = new Transaction(transactionId, memberId, bookId, 
                                                Transaction.TransactionType.RETURN, 
                                                fineAmount, notes);
        transactions.add(transaction);

        return message;
    }

    // Fine Management
    public String payFine(String memberId, double amount) {
        Member member = members.get(memberId);
        if (member == null) {
            return "Member not found!";
        }
        if (amount <= 0) {
            return "Invalid payment amount!";
        }
        if (amount > member.getFineAmount()) {
            return "Payment amount exceeds fine amount!";
        }

        member.payFine(amount);
        
        // Create transaction record
        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(transactionId, memberId, null, 
                                                Transaction.TransactionType.FINE_PAID);
        transaction.setFineAmount(amount);
        transaction.setNotes("Fine payment");
        transactions.add(transaction);

        return String.format("Fine payment of $%.2f successful! Remaining fine: $%.2f", 
                           amount, member.getFineAmount());
    }

    // Reporting Methods
    public List<Book> getOverdueBooks() {
        return books.values().stream()
                .filter(Book::isOverdue)
                .collect(Collectors.toList());
    }

    public List<Member> getMembersWithFines() {
        return members.values().stream()
                .filter(Member::hasPendingFines)
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> getMemberTransactions(String memberId) {
        return transactions.stream()
                .filter(t -> t.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    // Utility Methods
    private String generateTransactionId() {
        return "TXN" + String.format("%06d", transactionCounter++);
    }

    public void generateSampleData() {
        // Add sample books
        addBook(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", "978-0-7432-7356-5", "Fiction"));
        addBook(new Book("B002", "To Kill a Mockingbird", "Harper Lee", "978-0-06-112008-4", "Fiction"));
        addBook(new Book("B003", "1984", "George Orwell", "978-0-452-28423-4", "Dystopian"));
        addBook(new Book("B004", "Pride and Prejudice", "Jane Austen", "978-0-14-143951-8", "Romance"));
        addBook(new Book("B005", "The Catcher in the Rye", "J.D. Salinger", "978-0-316-76948-0", "Fiction"));

        // Add sample members
        addMember(new Member("M001", "John Doe", "john@email.com", "123-456-7890", "123 Main St", Member.MemberType.STUDENT));
        addMember(new Member("M002", "Jane Smith", "jane@email.com", "098-765-4321", "456 Oak Ave", Member.MemberType.FACULTY));
        addMember(new Member("M003", "Bob Johnson", "bob@email.com", "555-123-4567", "789 Pine Rd", Member.MemberType.STAFF));
    }

    // Statistics Methods
    public int getTotalBooks() {
        return books.size();
    }

    public int getAvailableBooksCount() {
        return (int) books.values().stream().filter(Book::isAvailable).count();
    }

    public int getBorrowedBooksCount() {
        return (int) books.values().stream().filter(book -> !book.isAvailable()).count();
    }

    public int getTotalMembers() {
        return members.size();
    }

    public int getActiveMembers() {
        return (int) members.values().stream().filter(Member::isActive).count();
    }
}