package ui;

import models.*;
import services.LibraryService;
import java.util.List;
import java.util.Scanner;

public class LibraryConsoleUI {
    private LibraryService libraryService;
    private Scanner scanner;

    public LibraryConsoleUI() {
        this.libraryService = new LibraryService();
        this.scanner = new Scanner(System.in);
        
        // Load sample data
        libraryService.generateSampleData();
    }

    public void start() {
        System.out.println("=================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM    ");
        System.out.println("=================================");
        
        while (true) {
            showMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    bookManagementMenu();
                    break;
                case 2:
                    memberManagementMenu();
                    break;
                case 3:
                    borrowReturnMenu();
                    break;
                case 4:
                    searchBooksMenu();
                    break;
                case 5:
                    reportsMenu();
                    break;
                case 6:
                    System.out.println("Thank you for using Library Management System!");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Book Management");
        System.out.println("2. Member Management");
        System.out.println("3. Borrow/Return Books");
        System.out.println("4. Search Books");
        System.out.println("5. Reports");
        System.out.println("6. Exit");
    }

    private void bookManagementMenu() {
        while (true) {
            System.out.println("\n--- BOOK MANAGEMENT ---");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. View All Books");
            System.out.println("4. View Available Books");
            System.out.println("5. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    viewAllBooks();
                    break;
                case 4:
                    viewAvailableBooks();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void memberManagementMenu() {
        while (true) {
            System.out.println("\n--- MEMBER MANAGEMENT ---");
            System.out.println("1. Add Member");
            System.out.println("2. Remove Member");
            System.out.println("3. View All Members");
            System.out.println("4. View Member Details");
            System.out.println("5. Pay Fine");
            System.out.println("6. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addMember();
                    break;
                case 2:
                    removeMember();
                    break;
                case 3:
                    viewAllMembers();
                    break;
                case 4:
                    viewMemberDetails();
                    break;
                case 5:
                    payFine();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void borrowReturnMenu() {
        while (true) {
            System.out.println("\n--- BORROW/RETURN ---");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. View Borrowed Books by Member");
            System.out.println("4. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    viewBorrowedBooks();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void searchBooksMenu() {
        while (true) {
            System.out.println("\n--- SEARCH BOOKS ---");
            System.out.println("1. Search by Title");
            System.out.println("2. Search by Author");
            System.out.println("3. Search by Category");
            System.out.println("4. General Search");
            System.out.println("5. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    searchByTitle();
                    break;
                case 2:
                    searchByAuthor();
                    break;
                case 3:
                    searchByCategory();
                    break;
                case 4:
                    generalSearch();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void reportsMenu() {
        while (true) {
            System.out.println("\n--- REPORTS ---");
            System.out.println("1. Library Statistics");
            System.out.println("2. Overdue Books");
            System.out.println("3. Members with Fines");
            System.out.println("4. Transaction History");
            System.out.println("5. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    showLibraryStatistics();
                    break;
                case 2:
                    showOverdueBooks();
                    break;
                case 3:
                    showMembersWithFines();
                    break;
                case 4:
                    showTransactionHistory();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // Book Management Methods
    private void addBook() {
        System.out.println("\n--- ADD BOOK ---");
        String bookId = getStringInput("Enter Book ID: ");
        String title = getStringInput("Enter Title: ");
        String author = getStringInput("Enter Author: ");
        String isbn = getStringInput("Enter ISBN: ");
        String category = getStringInput("Enter Category: ");
        
        Book book = new Book(bookId, title, author, isbn, category);
        if (libraryService.addBook(book)) {
            System.out.println("Book added successfully!");
        } else {
            System.out.println("Book with this ID already exists!");
        }
    }

    private void removeBook() {
        System.out.println("\n--- REMOVE BOOK ---");
        String bookId = getStringInput("Enter Book ID to remove: ");
        
        if (libraryService.removeBook(bookId)) {
            System.out.println("Book removed successfully!");
        } else {
            System.out.println("Cannot remove book. It may not exist or is currently borrowed.");
        }
    }

    private void viewAllBooks() {
        System.out.println("\n--- ALL BOOKS ---");
        List<Book> books = libraryService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-20s %-15s %-10s%n", 
                         "Book ID", "Title", "Author", "Category", "Status");
        System.out.println("-".repeat(80));
        
        for (Book book : books) {
            System.out.printf("%-8s %-25s %-20s %-15s %-10s%n",
                            book.getBookId(),
                            truncate(book.getTitle(), 24),
                            truncate(book.getAuthor(), 19),
                            truncate(book.getCategory(), 14),
                            book.isAvailable() ? "Available" : "Borrowed");
        }
    }

    private void viewAvailableBooks() {
        System.out.println("\n--- AVAILABLE BOOKS ---");
        List<Book> books = libraryService.getAvailableBooks();
        if (books.isEmpty()) {
            System.out.println("No available books found.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-20s %-15s%n", 
                         "Book ID", "Title", "Author", "Category");
        System.out.println("-".repeat(70));
        
        for (Book book : books) {
            System.out.printf("%-8s %-25s %-20s %-15s%n",
                            book.getBookId(),
                            truncate(book.getTitle(), 24),
                            truncate(book.getAuthor(), 19),
                            truncate(book.getCategory(), 14));
        }
    }

    // Member Management Methods
    private void addMember() {
        System.out.println("\n--- ADD MEMBER ---");
        String memberId = getStringInput("Enter Member ID: ");
        String name = getStringInput("Enter Name: ");
        String email = getStringInput("Enter Email: ");
        String phone = getStringInput("Enter Phone Number: ");
        String address = getStringInput("Enter Address: ");
        
        System.out.println("Member Types:");
        System.out.println("1. STUDENT");
        System.out.println("2. FACULTY");
        System.out.println("3. STAFF");
        
        int typeChoice = getIntInput("Select member type (1-3): ");
        Member.MemberType memberType;
        
        switch (typeChoice) {
            case 1:
                memberType = Member.MemberType.STUDENT;
                break;
            case 2:
                memberType = Member.MemberType.FACULTY;
                break;
            case 3:
                memberType = Member.MemberType.STAFF;
                break;
            default:
                System.out.println("Invalid choice. Setting as STUDENT.");
                memberType = Member.MemberType.STUDENT;
        }
        
        Member member = new Member(memberId, name, email, phone, address, memberType);
        if (libraryService.addMember(member)) {
            System.out.println("Member added successfully!");
        } else {
            System.out.println("Member with this ID already exists!");
        }
    }

    private void removeMember() {
        System.out.println("\n--- REMOVE MEMBER ---");
        String memberId = getStringInput("Enter Member ID to remove: ");
        
        if (libraryService.removeMember(memberId)) {
            System.out.println("Member removed successfully!");
        } else {
            System.out.println("Cannot remove member. They may not exist or have borrowed books.");
        }
    }

    private void viewAllMembers() {
        System.out.println("\n--- ALL MEMBERS ---");
        List<Member> members = libraryService.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        
        System.out.printf("%-8s %-20s %-12s %-8s %-10s %-8s%n", 
                         "ID", "Name", "Type", "Books", "Fine", "Status");
        System.out.println("-".repeat(70));
        
        for (Member member : members) {
            System.out.printf("%-8s %-20s %-12s %-8d $%-9.2f %-8s%n",
                            member.getMemberId(),
                            truncate(member.getName(), 19),
                            member.getMemberType(),
                            member.getBorrowedBooksCount(),
                            member.getFineAmount(),
                            member.isActive() ? "Active" : "Inactive");
        }
    }

    private void viewMemberDetails() {
        System.out.println("\n--- MEMBER DETAILS ---");
        String memberId = getStringInput("Enter Member ID: ");
        
        Member member = libraryService.getMember(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }
        
        System.out.println("Member ID: " + member.getMemberId());
        System.out.println("Name: " + member.getName());
        System.out.println("Email: " + member.getEmail());
        System.out.println("Phone: " + member.getPhoneNumber());
        System.out.println("Address: " + member.getAddress());
        System.out.println("Type: " + member.getMemberType());
        System.out.println("Registration Date: " + member.getRegistrationDate());
        System.out.println("Books Borrowed: " + member.getBorrowedBooksCount());
        System.out.println("Fine Amount: $" + String.format("%.2f", member.getFineAmount()));
        System.out.println("Status: " + (member.isActive() ? "Active" : "Inactive"));
        
        if (!member.getBorrowedBooks().isEmpty()) {
            System.out.println("\nBorrowed Books:");
            for (String bookId : member.getBorrowedBooks()) {
                Book book = libraryService.getBook(bookId);
                if (book != null) {
                    System.out.println("  - " + book.getTitle() + " (Due: " + book.getDueDate() + ")");
                }
            }
        }
    }

    private void payFine() {
        System.out.println("\n--- PAY FINE ---");
        String memberId = getStringInput("Enter Member ID: ");
        
        Member member = libraryService.getMember(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }
        
        if (!member.hasPendingFines()) {
            System.out.println("Member has no pending fines.");
            return;
        }
        
        System.out.println("Current fine amount: $" + String.format("%.2f", member.getFineAmount()));
        double amount = getDoubleInput("Enter payment amount: $");
        
        String result = libraryService.payFine(memberId, amount);
        System.out.println(result);
    }

    // Borrow/Return Methods
    private void borrowBook() {
        System.out.println("\n--- BORROW BOOK ---");
        String memberId = getStringInput("Enter Member ID: ");
        String bookId = getStringInput("Enter Book ID: ");
        
        String result = libraryService.borrowBook(memberId, bookId);
        System.out.println(result);
    }

    private void returnBook() {
        System.out.println("\n--- RETURN BOOK ---");
        String memberId = getStringInput("Enter Member ID: ");
        String bookId = getStringInput("Enter Book ID: ");
        
        String result = libraryService.returnBook(memberId, bookId);
        System.out.println(result);
    }

    private void viewBorrowedBooks() {
        System.out.println("\n--- BORROWED BOOKS BY MEMBER ---");
        String memberId = getStringInput("Enter Member ID: ");
        
        Member member = libraryService.getMember(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }
        
        if (member.getBorrowedBooks().isEmpty()) {
            System.out.println("Member has no borrowed books.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-20s %-12s %-10s%n", 
                         "Book ID", "Title", "Author", "Due Date", "Status");
        System.out.println("-".repeat(78));
        
        for (String bookId : member.getBorrowedBooks()) {
            Book book = libraryService.getBook(bookId);
            if (book != null) {
                System.out.printf("%-8s %-25s %-20s %-12s %-10s%n",
                                book.getBookId(),
                                truncate(book.getTitle(), 24),
                                truncate(book.getAuthor(), 19),
                                book.getDueDate(),
                                book.isOverdue() ? "OVERDUE" : "On Time");
            }
        }
    }

    // Search Methods
    private void searchByTitle() {
        System.out.println("\n--- SEARCH BY TITLE ---");
        String title = getStringInput("Enter title (partial match): ");
        
        List<Book> books = libraryService.searchBooksByTitle(title);
        displaySearchResults(books);
    }

    private void searchByAuthor() {
        System.out.println("\n--- SEARCH BY AUTHOR ---");
        String author = getStringInput("Enter author (partial match): ");
        
        List<Book> books = libraryService.searchBooksByAuthor(author);
        displaySearchResults(books);
    }

    private void searchByCategory() {
        System.out.println("\n--- SEARCH BY CATEGORY ---");
        String category = getStringInput("Enter category (partial match): ");
        
        List<Book> books = libraryService.searchBooksByCategory(category);
        displaySearchResults(books);
    }

    private void generalSearch() {
        System.out.println("\n--- GENERAL SEARCH ---");
        String query = getStringInput("Enter search term (searches title, author, category, ISBN): ");
        
        List<Book> books = libraryService.searchBooks(query);
        displaySearchResults(books);
    }

    private void displaySearchResults(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found matching your search.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-20s %-15s %-10s%n", 
                         "Book ID", "Title", "Author", "Category", "Status");
        System.out.println("-".repeat(80));
        
        for (Book book : books) {
            System.out.printf("%-8s %-25s %-20s %-15s %-10s%n",
                            book.getBookId(),
                            truncate(book.getTitle(), 24),
                            truncate(book.getAuthor(), 19),
                            truncate(book.getCategory(), 14),
                            book.isAvailable() ? "Available" : "Borrowed");
        }
    }

    // Report Methods
    private void showLibraryStatistics() {
        System.out.println("\n--- LIBRARY STATISTICS ---");
        System.out.println("Total Books: " + libraryService.getTotalBooks());
        System.out.println("Available Books: " + libraryService.getAvailableBooksCount());
        System.out.println("Borrowed Books: " + libraryService.getBorrowedBooksCount());
        System.out.println("Total Members: " + libraryService.getTotalMembers());
        System.out.println("Active Members: " + libraryService.getActiveMembers());
    }

    private void showOverdueBooks() {
        System.out.println("\n--- OVERDUE BOOKS ---");
        List<Book> overdueBooks = libraryService.getOverdueBooks();
        
        if (overdueBooks.isEmpty()) {
            System.out.println("No overdue books found.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-12s %-12s %-8s%n", 
                         "Book ID", "Title", "Borrowed By", "Due Date", "Days Late");
        System.out.println("-".repeat(68));
        
        for (Book book : overdueBooks) {
            System.out.printf("%-8s %-25s %-12s %-12s %-8d%n",
                            book.getBookId(),
                            truncate(book.getTitle(), 24),
                            book.getBorrowedBy(),
                            book.getDueDate(),
                            book.getDaysOverdue());
        }
    }

    private void showMembersWithFines() {
        System.out.println("\n--- MEMBERS WITH FINES ---");
        List<Member> membersWithFines = libraryService.getMembersWithFines();
        
        if (membersWithFines.isEmpty()) {
            System.out.println("No members with fines found.");
            return;
        }
        
        System.out.printf("%-8s %-20s %-12s %-10s%n", 
                         "ID", "Name", "Type", "Fine Amount");
        System.out.println("-".repeat(52));
        
        for (Member member : membersWithFines) {
            System.out.printf("%-8s %-20s %-12s $%-9.2f%n",
                            member.getMemberId(),
                            truncate(member.getName(), 19),
                            member.getMemberType(),
                            member.getFineAmount());
        }
    }

    private void showTransactionHistory() {
        System.out.println("\n--- TRANSACTION HISTORY ---");
        List<Transaction> transactions = libraryService.getTransactionHistory();
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        
        System.out.printf("%-12s %-8s %-8s %-10s %-12s %-8s%n", 
                         "Transaction", "Member", "Book", "Type", "Date", "Fine");
        System.out.println("-".repeat(70));
        
        for (Transaction transaction : transactions) {
            System.out.printf("%-12s %-8s %-8s %-10s %-12s $%-7.2f%n",
                            transaction.getTransactionId(),
                            transaction.getMemberId(),
                            transaction.getBookId() != null ? transaction.getBookId() : "N/A",
                            transaction.getType(),
                            transaction.getTransactionDate().toLocalDate(),
                            transaction.getFineAmount());
        }
    }

    // Utility Methods
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}