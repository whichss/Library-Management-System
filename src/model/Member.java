package model;

public class Member extends User {
    private int borrowedCount;
    private static final int MAX_BORROW_LIMIT = 5;
    private int overdueCount;
    private boolean isSuspended;
    
    public Member(String userId, String name, String password) {
        super(userId, name, password, "MEMBER");
        this.borrowedCount = 0;
        this.overdueCount = 0;
        this.isSuspended = false;
    }
    
    // Getters
    public int getBorrowedCount() { return borrowedCount; }
    public int getOverdueCount() { return overdueCount; }
    public boolean isSuspended() { return isSuspended; }
    public int getRemainingBorrowLimit() { return MAX_BORROW_LIMIT - borrowedCount; }
    public int getMaxBorrowLimit() { return MAX_BORROW_LIMIT; }
    
    // Setters
    public void setBorrowedCount(int count) { this.borrowedCount = count; }
    public void setOverdueCount(int count) { this.overdueCount = count; }
    public void setSuspended(boolean suspended) { this.isSuspended = suspended; }
    
    // 대출 관련 메서드
    public boolean canBorrow() {
        return !isSuspended && borrowedCount < MAX_BORROW_LIMIT;
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
    
    public void increaseOverdueCount() {
        this.overdueCount++;
        if (overdueCount >= 3) {
            isSuspended = true;
        }
    }
    
    public void releaseSuspension() {
        isSuspended = false;
    }
}
