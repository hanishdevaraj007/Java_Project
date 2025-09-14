<<<<<<< HEAD
# Library Management System

A comprehensive Library Management System built with Java, featuring book management, member management, borrowing/returning operations, and detailed reporting capabilities.

## Features

### Core Functionality
- **Book Management**: Add, remove, search, and view books
- **Member Management**: Register members, manage profiles, track borrowing history
- **Borrowing System**: Automated borrowing and returning with due date tracking
- **Fine Management**: Automatic fine calculation for overdue books
- **Search System**: Search books by title, author, category, or general query

### Advanced Features
- **Member Types**: Different privileges for Students, Faculty, and Staff
- **Transaction History**: Complete audit trail of all library operations
- **Overdue Tracking**: Automatic identification and management of overdue books
- **Reporting**: Comprehensive reports and statistics
- **Data Persistence**: File-based data storage with backup capabilities

## Project Structure

```
Library-Management-System/
├── models/
│   ├── Book.java              # Book entity class
│   ├── Member.java            # Member entity class  
│   └── Transaction.java       # Transaction record class
├── services/
│   └── LibraryService.java    # Core business logic
├── ui/
│   └── LibraryConsoleUI.java  # Console user interface
├── utils/
│   └── FileUtils.java         # File operations utility
├── data/                      # Data storage directory
└── LibraryManagementApp.java  # Main application class
```

## Getting Started

### Prerequisites
- Java 8 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code) or command line

### Setup Instructions

1. **Create Project Directory**
   ```
   mkdir LibraryManagementSystem
   cd LibraryManagementSystem
   ```

2. **Create Package Structure**
   ```
   mkdir -p src/models
   mkdir -p src/services
   mkdir -p src/ui
   mkdir -p src/utils
   mkdir -p data
   ```

3. **Add Source Files**
   - Copy all the provided Java files to their respective directories
   - Ensure proper package declarations in each file

4. **Compile the Project**
   ```bash
   javac -d . LibraryManagementApp.java models/Book.java models/Member.java models/Transaction.java services/LibraryService.java ui/LibraryConsoleUI.java utils/FileUtils.java
   ```

5. **Run the Application**
   ```bash
   java LibraryManagementApp
   ```

### Alternative: IDE Setup
1. Create a new Java project in your IDE
2. Create the package structure (models, services, ui, utils)
3. Add the provided classes to their respective packages
4. Run `LibraryManagementApp.java`

## Usage Guide

### Main Menu Options

1. **Book Management**
   - Add new books to the library
   - Remove books from inventory
   - View all books or available books only

2. **Member Management**
   - Register new members (Student/Faculty/Staff)
   - Remove members (only if no borrowed books)
   - View member details and borrowing history
   - Process fine payments

3. **Borrow/Return Operations**
   - Borrow books (with automatic due date calculation)
   - Return books (with fine calculation if overdue)
   - View borrowed books by member

4. **Search Functionality**
   - Search by book title
   - Search by author name
   - Search by category
   - General search (searches all fields)

5. **Reports and Analytics**
   - Library statistics overview
   - Overdue books report
   - Members with pending fines
   - Complete transaction history

### Sample Data
The system comes with pre-loaded sample data including:
- 5 sample books across different categories
- 3 sample members (Student, Faculty, Staff)

## Technical Details

### Design Patterns Used
- **Repository Pattern**: Clean separation of data access logic
- **Enum Pattern**: Type-safe member types and transaction types
- **Builder Pattern**: Clean object construction
- **MVC Pattern**: Separation of concerns between models, services, and UI

### Key Classes

#### Book.java
- Manages book information and borrowing state
- Tracks due dates and overdue calculations
- Handles book availability status

#### Member.java
- Stores member information and borrowing limits
- Different member types with varying privileges
- Fine tracking and payment management

#### Transaction.java
- Records all library operations
- Provides audit trail for borrowing/returning
- Tracks fines and payment history

#### LibraryService.java
- Core business logic implementation
- Validates all operations
- Manages relationships between books and members

### Member Types and Privileges

| Member Type | Borrow Duration | Max Books | Description |
|-------------|----------------|-----------|-------------|
| STUDENT     | 14 days        | 3 books   | Standard student access |
| FACULTY     | 21 days        | 5 books   | Extended privileges for faculty |
| STAFF       | 14 days        | 3 books   | Standard staff access |

### Fine System
- **Fine Rate**: $1.00 per day overdue
- **Maximum Fine**: $50.00 per book
- **Borrowing Restriction**: Members with fines over $50 cannot borrow books
- **Automatic Calculation**: Fines calculated automatically upon return

## Future Enhancements

### Planned Features
- [ ] GUI interface using JavaFX or Swing
- [ ] Database integration (MySQL/PostgreSQL)
- [ ] Book reservation system
- [ ] Email notifications for due dates
- [ ] Barcode/QR code integration
- [ ] Multi-branch support
- [ ] Digital book management
- [ ] Web-based interface

### Possible Improvements
- [ ] Advanced search with filters
- [ ] Book recommendation system
- [ ] Integration with external book APIs
- [ ] Mobile app companion
- [ ] Statistical dashboards
- [ ] Export functionality (PDF, Excel)

## Code Quality Features

### Error Handling
- Comprehensive input validation
- Graceful error recovery
- User-friendly error messages

### Data Integrity
- Consistent data validation
- Transaction atomicity
- Backup and recovery options

### Code Organization
- Clean package structure
- Separation of concerns
- Comprehensive documentation
- Consistent naming conventions

## Testing

### Manual Testing Scenarios
1. **Book Operations**: Add/remove books, search functionality
2. **Member Operations**: Register/remove members, view details
3. **Borrowing Flow**: Complete borrow-return cycle
4. **Fine Calculation**: Test overdue scenarios
5. **Edge Cases**: Invalid inputs, non-existent records

### Sample Test Data
The system includes built-in sample data for immediate testing:
- Books from various genres and authors
- Members of different types
- Various transaction scenarios

## Troubleshooting

### Common Issues

**"Member not found" Error**
- Ensure member ID exists in the system
- Check for typos in member ID

**"Book not available" Error**
- Verify book exists and is not currently borrowed
- Use search function to find available books

**File Permission Errors**
- Ensure write permissions in the data directory
- Check if data directory exists

**Compilation Errors**
- Verify Java version compatibility
- Check package declarations match directory structure

## Contributing

This project is designed as a learning exercise. Feel free to:
- Add new features
- Improve existing functionality
- Enhance the user interface
- Add database integration
- Implement additional design patterns

## License

This project is created for educational purposes. Feel free to use, modify, and distribute as needed for learning and academic projects.

## Contact

For questions or suggestions about this Library Management System implementation, please refer to the code comments or create issues in your repository.

---

**Note**: This is a console-based application designed for learning Java programming concepts including OOP, collections, file I/O, and basic software architecture patterns.
=======
# Library_Management
>>>>>>> 472adfca3ffc16c6fffe5583b796aa1932ca8e18
