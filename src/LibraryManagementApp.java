import ui.LibraryConsoleUI;

/**
 * Library Management System
 * 
 * This is a comprehensive library management system built with Java.
 * It provides functionality for managing books, members, borrowing/returning,
 * searching, and generating reports.
 * 
 * Features:
 * - Book Management (Add, Remove, Search)
 * - Member Management (Add, Remove, View Details)
 * - Borrowing and Returning Books
 * - Automatic Fine Calculation
 * - Search Functionality
 * - Comprehensive Reports
 * - Transaction History
 * 
 * @author Your Name
 * @version 1.0
 */
public class LibraryManagementApp {
    
    public static void main(String[] args) {
        try {
            // Create and start the console UI
            LibraryConsoleUI ui = new LibraryConsoleUI();
            ui.start();
        } catch (Exception e) {
            System.err.println("An error occurred while running the application:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}