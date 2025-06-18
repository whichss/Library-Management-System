package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Loan {
    private String loanId;
    private String userId;
    private String bookId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // 새 대출 생성자
    public Loan(String loanId, String userId, String bookId) {
        this.loanId = loanId;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(14); // 14일 대출
        this.returnDate = null;
    }
    
    // CSV용 생성자
    public Loan(String loanId, String userId, String bookId, String loanDate, String dueDate, String returnDate) {
        this.loanId = loanId;
        this.userId = userId;
        this.bookId = bookId;
        
        try {
            this.loanDate = LocalDate.parse(loanDate, DATE_FORMAT);
            this.dueDate = LocalDate.parse(dueDate, DATE_FORMAT);
            this.returnDate = returnDate.equals("null") ? null : LocalDate.parse(returnDate, DATE_FORMAT);
        } catch (Exception e) {
            this.loanDate = LocalDate.now();
            this.dueDate = this.loanDate.plusDays(14);
            this.returnDate = null;
        }
    }
    
    // Getters
    public String getLoanId() { return loanId; }
    public String getUserId() { return userId; }
    public String getBookId() { return bookId; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    
    public String getLoanDateString() { return loanDate.format(DATE_FORMAT); }
    public String getDueDateString() { return dueDate.format(DATE_FORMAT); }
    public String getReturnDateString() { return returnDate == null ? "null" : returnDate.format(DATE_FORMAT); }
    
    // 반납 처리
    public void returnBook() {
        this.returnDate = LocalDate.now();
    }
    
    // 상태 확인
    public boolean isReturned() { return returnDate != null; }
    
    public boolean isOverdue() {
        if (isReturned()) {
            return returnDate.isAfter(dueDate);
        } else {
            return LocalDate.now().isAfter(dueDate);
        }
    }
    
    public String toCsvString() {
        return loanId + "," + userId + "," + bookId + "," + 
               getLoanDateString() + "," + getDueDateString() + "," + getReturnDateString();
    }
}
