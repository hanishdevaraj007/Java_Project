package utils;

import models.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Utility class for handling file operations
 * This class provides methods to save and load library data to/from files
 */
public class FileUtils {
    
    private static final String DATA_DIRECTORY = "data/";
    private static final String BOOKS_FILE = "books.csv";
    private static final String MEMBERS_FILE = "members.csv";
    private static final String TRANSACTIONS_FILE = "transactions.csv";
    
    // Date formatters for consistent date handling
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    static {
        // Create data directory if it doesn't exist
        File dataDir = new File(DATA_DIRECTORY);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
    
    /**
     * Save books to CSV file
     */
    public static void saveBooks(Collection<Book> books) throws IOException {
        File file = new File(DATA_DIRECTORY + BOOKS_FILE);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write CSV header
            writer.println("BookID,Title,Author,ISBN,Category,IsAvailable,DateAdded,BorrowedBy,BorrowDate,DueDate");
            
            for (Book book : books) {
                writer.printf("%s,%s,%s,%s,%s,%b,%s,%s,%s,%s%n",
                    escapeCsv(book.getBookId()),
                    escapeCsv(book.getTitle()),
                    escapeCsv(book.getAuthor()),
                    escapeCsv(book.getIsbn()),
                    escapeCsv(book.getCategory()),
                    book.isAvailable(),
                    book.getDateAdded().format(DATE_FORMATTER),
                    book.getBorrowedBy() != null ? escapeCsv(book.getBorrowedBy()) : "",
                    book.getBorrowDate() != null ? book.getBorrowDate().format(DATE_FORMATTER) : "",
                    book.getDueDate() != null ? book.getDueDate().format(DATE_FORMATTER) : ""
                );
            }
        }
    }
    
    /**
     * Load books from CSV file
     */
    public static List<Book> loadBooks() throws IOException {
        List<Book> books = new ArrayList<>();
        File file = new File(DATA_DIRECTORY + BOOKS_FILE);
        
        if (!file.exists()) {
            return books; // Return empty list if file doesn't exist
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCsvLine(line);
                if (parts.length >= 6) {
                    Book book = new Book(
                        parts[0], // bookId
                        parts[1], // title
                        parts[2], // author
                        parts[3], // isbn
                        parts[4]  // category
                    );
                    
                    book.setAvailable(Boolean.parseBoolean(parts[5]));
                    
                    // Set borrowed information if available
                    if (parts.length > 7 && !parts[7].isEmpty()) {
                        book.setBorrowedBy(parts[7]);
                        if (parts.length > 8 && !parts[8].isEmpty()) {
                            book.setBorrowDate(LocalDate.parse(parts[8], DATE_FORMATTER));
                        }
                        if (parts.length > 9 && !parts[9].isEmpty()) {
                            book.setDueDate(LocalDate.parse(parts[9], DATE_FORMATTER));
                        }
                    }
                    
                    books.add(book);
                }
            }
        }
        
        return books;
    }
    
    /**
     * Save members to CSV file
     */
    public static void saveMembers(Collection<Member> members) throws IOException {
        File file = new File(DATA_DIRECTORY + MEMBERS_FILE);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write CSV header
            writer.println("MemberID,Name,Email,Phone,Address,MemberType,RegistrationDate,BorrowedBooks,FineAmount,IsActive");
            
            for (Member member : members) {
                // Convert borrowed books list to semicolon-separated string
                String borrowedBooksStr = String.join(";", member.getBorrowedBooks());
                
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%.2f,%b%n",
                    escapeCsv(member.getMemberId()),
                    escapeCsv(member.getName()),
                    escapeCsv(member.getEmail()),
                    escapeCsv(member.getPhoneNumber()),
                    escapeCsv(member.getAddress()),
                    member.getMemberType().name(),
                    member.getRegistrationDate().format(DATE_FORMATTER),
                    escapeCsv(borrowedBooksStr),
                    member.getFineAmount(),
                    member.isActive()
                );
            }
        }
    }
    
    /**
     * Load members from CSV file
     */
    public static List<Member> loadMembers() throws IOException {
        List<Member> members = new ArrayList<>();
        File file = new File(DATA_DIRECTORY + MEMBERS_FILE);
        
        if (!file.exists()) {
            return members; // Return empty list if file doesn't exist
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCsvLine(line);
                if (parts.length >= 9) {
                    Member member = new Member(
                        parts[0], // memberId
                        parts[1], // name
                        parts[2], // email
                        parts[3], // phone
                        parts[4], // address
                        Member.MemberType.valueOf(parts[5]) // memberType
                    );
                    
                    // Set borrowed books if any
                    if (!parts[7].isEmpty()) {
                        String[] bookIds = parts[7].split(";");
                        for (String bookId : bookIds) {
                            if (!bookId.trim().isEmpty()) {
                                member.addBorrowedBook(bookId.trim());
                            }
                        }
                    }
                    
                    member.setFineAmount(Double.parseDouble(parts[8]));
                    member.setActive(Boolean.parseBoolean(parts[9]));
                    
                    members.add(member);
                }
            }
        }
        
        return members;
    }
    
    /**
     * Save transactions to CSV file
     */
    public static void saveTransactions(List<Transaction> transactions) throws IOException {
        File file = new File(DATA_DIRECTORY + TRANSACTIONS_FILE);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write CSV header
            writer.println("TransactionID,MemberID,BookID,Type,TransactionDate,DueDate,ReturnDate,FineAmount,Notes");
            
            for (Transaction transaction : transactions) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%.2f,%s%n",
                    escapeCsv(transaction.getTransactionId()),
                    escapeCsv(transaction.getMemberId()),
                    transaction.getBookId() != null ? escapeCsv(transaction.getBookId()) : "",
                    transaction.getType().name(),
                    transaction.getTransactionDate().format(DATETIME_FORMATTER),
                    transaction.getDueDate() != null ? transaction.getDueDate().format(DATETIME_FORMATTER) : "",
                    transaction.getReturnDate() != null ? transaction.getReturnDate().format(DATETIME_FORMATTER) : "",
                    transaction.getFineAmount(),
                    escapeCsv(transaction.getNotes())
                );
            }
        }
    }
    
    /**
     * Load transactions from CSV file
     */
    public static List<Transaction> loadTransactions() throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(DATA_DIRECTORY + TRANSACTIONS_FILE);
        
        if (!file.exists()) {
            return transactions; // Return empty list if file doesn't exist
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCsvLine(line);
                if (parts.length >= 8) {
                    String bookId = parts[2].isEmpty() ? null : parts[2];
                    Transaction.TransactionType type = Transaction.TransactionType.valueOf(parts[3]);
                    
                    Transaction transaction = new Transaction(
                        parts[0], // transactionId
                        parts[1], // memberId
                        bookId,   // bookId
                        type      // type
                    );
                    
                    // Set dates
                    if (!parts[5].isEmpty()) {
                        transaction.setDueDate(LocalDateTime.parse(parts[5], DATETIME_FORMATTER));
                    }
                    if (!parts[6].isEmpty()) {
                        transaction.setReturnDate(LocalDateTime.parse(parts[6], DATETIME_FORMATTER));
                    }
                    
                    transaction.setFineAmount(Double.parseDouble(parts[7]));
                    
                    if (parts.length > 8) {
                        transaction.setNotes(parts[8]);
                    }
                    
                    transactions.add(transaction);
                }
            }
        }
        
        return transactions;
    }
    
    /**
     * Create a backup of all data files
     */
    public static void createBackup() throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupDir = DATA_DIRECTORY + "backup_" + timestamp + "/";
        
        File backupDirectory = new File(backupDir);
        if (!backupDirectory.exists()) {
            backupDirectory.mkdirs();
        }
        
        // Copy each data file to backup directory
        String[] dataFiles = {BOOKS_FILE, MEMBERS_FILE, TRANSACTIONS_FILE};
        
        for (String fileName : dataFiles) {
            File sourceFile = new File(DATA_DIRECTORY + fileName);
            File backupFile = new File(backupDir + fileName);
            
            if (sourceFile.exists()) {
                copyFile(sourceFile, backupFile);
            }
        }
        
        System.out.println("Backup created successfully at: " + backupDir);
    }
    
    /**
     * Copy file from source to destination
     */
    private static void copyFile(File source, File destination) throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }
    
    /**
     * Export library statistics to a text file
     */
    public static void exportStatistics(int totalBooks, int availableBooks, int borrowedBooks, 
                                      int totalMembers, int activeMembers, 
                                      List<Book> overdueBooks, List<Member> membersWithFines) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = DATA_DIRECTORY + "library_statistics_" + timestamp + ".txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("LIBRARY MANAGEMENT SYSTEM - STATISTICS REPORT");
            writer.println("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("=" .repeat(60));
            writer.println();
            
            // Basic Statistics
            writer.println("BASIC STATISTICS:");
            writer.println("-".repeat(20));
            writer.println("Total Books: " + totalBooks);
            writer.println("Available Books: " + availableBooks);
            writer.println("Borrowed Books: " + borrowedBooks);
            writer.println("Total Members: " + totalMembers);
            writer.println("Active Members: " + activeMembers);
            writer.println();
            
            // Overdue Books
            writer.println("OVERDUE BOOKS (" + overdueBooks.size() + "):");
            writer.println("-".repeat(25));
            if (overdueBooks.isEmpty()) {
                writer.println("No overdue books found.");
            } else {
                for (Book book : overdueBooks) {
                    writer.printf("Book ID: %s, Title: %s, Borrowed By: %s, Days Overdue: %d%n",
                        book.getBookId(), book.getTitle(), book.getBorrowedBy(), book.getDaysOverdue());
                }
            }
            writer.println();
            
            // Members with Fines
            writer.println("MEMBERS WITH FINES (" + membersWithFines.size() + "):");
            writer.println("-".repeat(30));
            if (membersWithFines.isEmpty()) {
                writer.println("No members with fines found.");
            } else {
                double totalFines = 0.0;
                for (Member member : membersWithFines) {
                    writer.printf("Member ID: %s, Name: %s, Fine Amount: $%.2f%n",
                        member.getMemberId(), member.getName(), member.getFineAmount());
                    totalFines += member.getFineAmount();
                }
                writer.println();
                writer.printf("Total Outstanding Fines: $%.2f%n", totalFines);
            }
        }
        
        System.out.println("Statistics exported to: " + filename);
    }
    
    /**
     * Escape CSV special characters
     */
    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        
        // If the value contains comma, double quote, or newline, wrap it in double quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            // Escape existing double quotes by doubling them
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        
        return value;
    }
    
    /**
     * Parse a CSV line handling quoted values
     */
    private static String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    currentField.append('"');
                    i++; // Skip the next quote
                } else {
                    // Toggle quote mode
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // Field separator
                result.add(currentField.toString());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        
        // Add the last field
        result.add(currentField.toString());
        
        return result.toArray(new String[0]);
    }
    
    /**
     * Check if data directory exists and is writable
     */
    public static boolean isDataDirectoryAccessible() {
        File dataDir = new File(DATA_DIRECTORY);
        return dataDir.exists() && dataDir.canRead() && dataDir.canWrite();
    }
    
    /**
     * Get the size of all data files in bytes
     */
    public static long getDataSize() {
        long totalSize = 0;
        String[] dataFiles = {BOOKS_FILE, MEMBERS_FILE, TRANSACTIONS_FILE};
        
        for (String fileName : dataFiles) {
            File file = new File(DATA_DIRECTORY + fileName);
            if (file.exists()) {
                totalSize += file.length();
            }
        }
        
        return totalSize;
    }
    
    /**
     * Format file size in human-readable format
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
}