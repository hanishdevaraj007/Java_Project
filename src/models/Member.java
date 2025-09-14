package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member {
    private String memberId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private MemberType memberType;
    private LocalDate registrationDate;
    private List<String> borrowedBooks; // List of book IDs
    private double fineAmount;
    private boolean isActive;

    public enum MemberType {
        STUDENT(14, 3),      // 14 days borrow period, max 3 books
        FACULTY(21, 5),      // 21 days borrow period, max 5 books
        STAFF(14, 3);        // 14 days borrow period, max 3 books

        private final int borrowDurationDays;
        private final int maxBooksAllowed;

        MemberType(int borrowDurationDays, int maxBooksAllowed) {
            this.borrowDurationDays = borrowDurationDays;
            this.maxBooksAllowed = maxBooksAllowed;
        }

        public int getBorrowDurationDays() {
            return borrowDurationDays;
        }

        public int getMaxBooksAllowed() {
            return maxBooksAllowed;
        }
    }

    // Constructor
    public Member(String memberId, String name, String email, String phoneNumber, 
                  String address, MemberType memberType) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memberType = memberType;
        this.registrationDate = LocalDate.now();
        this.borrowedBooks = new ArrayList<>();
        this.fineAmount = 0.0;
        this.isActive = true;
    }

    // Getters
    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public List<String> getBorrowedBooks() {
        return new ArrayList<>(borrowedBooks); // Return copy for safety
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    // Business methods
    public boolean canBorrowMoreBooks() {
        return borrowedBooks.size() < memberType.getMaxBooksAllowed();
    }

    public void addBorrowedBook(String bookId) {
        if (canBorrowMoreBooks()) {
            borrowedBooks.add(bookId);
        }
    }

    public void removeBorrowedBook(String bookId) {
        borrowedBooks.remove(bookId);
    }

    public int getBorrowedBooksCount() {
        return borrowedBooks.size();
    }

    public void addFine(double amount) {
        this.fineAmount += amount;
    }

    public void payFine(double amount) {
        this.fineAmount = Math.max(0, this.fineAmount - amount);
    }

    public boolean hasPendingFines() {
        return fineAmount > 0;
    }

    public boolean canBorrowBooks() {
        return isActive && fineAmount <= 50.0; // Can't borrow if fine exceeds $50
    }

    @Override
    public String toString() {
        return String.format("Member{ID='%s', Name='%s', Type='%s', Books Borrowed=%d, Fine=$%.2f, Active=%s}",
                memberId, name, memberType, borrowedBooks.size(), fineAmount, isActive);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return memberId.equals(member.memberId);
    }

    @Override
    public int hashCode() {
        return memberId.hashCode();
    }
}