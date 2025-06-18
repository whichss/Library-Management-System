package model;

public class Admin extends User {
    private int borrowedCount;
    private static final int MAX_BORROW_LIMIT = 7;
    
    public Admin(String userId, String name, String password) {
        super(userId, name, password, "ADMIN");
        this.borrowedCount = 0;
    }
    
    // Getters
    public int getBorrowedCount() { return borrowedCount; }
    public int getRemainingBorrowLimit() { return MAX_BORROW_LIMIT - borrowedCount; }
    public int getMaxBorrowLimit() { return MAX_BORROW_LIMIT; }
    
    // Setters
    public void setBorrowedCount(int count) { this.borrowedCount = count; }
    
    // 대출 관련 메서드
    public boolean canBorrow() {
        return borrowedCount < MAX_BORROW_LIMIT;
    }
    
    public void borrowBook() {
        if (canBorrow()) {
            borrowedCount++;
        }
    }
    
    public void returnBook() {
        if (borrowedCount > 0) {
            borrowedCount--;
        }
    }
    
    // 관리자 권한 메서드
    public boolean isAdmin() { return true; }
    public boolean canAddBook() { return true; }
    public boolean canDeleteBook() { return true; }
    public boolean canViewAllLoans() { return true; }
    public boolean canManageUsers() { return true; }
}
