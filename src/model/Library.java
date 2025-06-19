package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private List<Book> bookList;
    private List<Loan> loanList;
    private CsvInOut csvInOut;
    private int nextBarcodeNumber;
    private int nextLoanId;
    
    public Library() {
        csvInOut = new CsvInOut();
        bookList = new ArrayList<>();
        loanList = new ArrayList<>();
        loadData();
        calculateNextIds();
    }
    
    private void loadData() {
        bookList = csvInOut.readBooks();
        loanList = csvInOut.readLoans();
    }
    
    private void calculateNextIds() {
        nextBarcodeNumber = 1000001;
        for (Book book : bookList) {
            try {
                int barcodeNum = Integer.parseInt(book.getBarcode());
                if (barcodeNum >= nextBarcodeNumber) {
                    nextBarcodeNumber = barcodeNum + 1;
                }
            } catch (Exception e) {
                // 바코드가 숫자가 아닌 경우 무시
            }
        }
        
        nextLoanId = 1;
        for (Loan loan : loanList) {
            try {
                int loanNum = Integer.parseInt(loan.getLoanId().substring(1));
                if (loanNum >= nextLoanId) {
                    nextLoanId = loanNum + 1;
                }
            } catch (Exception e) {
                // ID 형식이 다른 경우 무시
            }
        }
    }
    
    // 새 도서 추가
    public List<String> addBooks(Book book, int quantity) {
        List<String> addedBarcodes = new ArrayList<>();
        
        for (int i = 0; i < quantity; i++) {
            String barcode = String.valueOf(nextBarcodeNumber++);
            
            Book newBook = new Book(
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublicationYear(),
                book.getCategory(),
                book.getPrice()
            );
            newBook.setBarcode(barcode);
            
            bookList.add(newBook);
            addedBarcodes.add(barcode);
        }
        
        csvInOut.saveBooks(bookList);
        return addedBarcodes;
    }
    
    // 도서 수정
    public boolean updateBook(String barcode, Book updatedBook) {
        Book book = findBookByBarcode(barcode);
        if (book != null) {
            book.setCallNumber(updatedBook.getCallNumber());
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPublisher(updatedBook.getPublisher());
            book.setPublicationYear(updatedBook.getPublicationYear());
            book.setCategory(updatedBook.getCategory());
            book.setPrice(updatedBook.getPrice());
            book.setStatus(updatedBook.getStatus());
            book.setNote(updatedBook.getNote());
            
            csvInOut.saveBooks(bookList);
            return true;
        }
        return false;
    }
    
    // 도서 삭제
    public boolean deleteBook(String barcode) {
        Book book = findBookByBarcode(barcode);
        if (book != null && book.isAvailable() && book.getStatus().equals("정상")) {
            bookList.remove(book);
            csvInOut.saveBooks(bookList);
            return true;
        }
        return false;
    }
    
    // 바코드로 도서 찾기
    public Book findBookByBarcode(String barcode) {
        for (Book book : bookList) {
            if (book.getBarcode().equals(barcode)) {
                return book;
            }
        }
        return null;
    }
    
    // 모든 도서 목록 반환
    public List<Book> getAllBooks() {
        return new ArrayList<>(bookList);
    }
    
    // 도서 검색
    public List<Book> searchBooks(String keyword, String searchType) {
        List<Book> result = new ArrayList<>();
        keyword = keyword.toLowerCase();
        
        for (Book book : bookList) {
            boolean matches = false;
            
            switch (searchType) {
                case "바코드":
                    matches = book.getBarcode().contains(keyword);
                    break;
                case "청구기호":
                    matches = book.getCallNumber().toLowerCase().contains(keyword);
                    break;
                case "제목":
                    matches = book.getTitle().toLowerCase().contains(keyword);
                    break;
                case "저자":
                    matches = book.getAuthor().toLowerCase().contains(keyword);
                    break;
                case "카테고리":
                    matches = book.getCategory().toLowerCase().contains(keyword);
                    break;
                case "전체":
                    matches = book.getBarcode().contains(keyword) ||
                             book.getCallNumber().toLowerCase().contains(keyword) ||
                             book.getTitle().toLowerCase().contains(keyword) ||
                             book.getAuthor().toLowerCase().contains(keyword) ||
                             book.getCategory().toLowerCase().contains(keyword);
                    break;
            }
            
            if (matches) {
                result.add(book);
            }
        }
        return result;
    }
    
    // 대출 가능한 도서만 반환
    public List<Book> getAvailableBooks() {
        List<Book> result = new ArrayList<>();
        for (Book book : bookList) {
            if (book.canBorrow()) {
                result.add(book);
            }
        }
        return result;
    }
    
    // 도서 대출
    public boolean borrowBook(String userId, String barcode) {
        Book book = findBookByBarcode(barcode);
        if (book == null || !book.canBorrow()) {
            return false;
        }
        
        String loanId = "L" + String.format("%04d", nextLoanId++);
        Loan loan = new Loan(loanId, userId, barcode);
        loanList.add(loan);
        
        book.setAvailable(false);
        
        csvInOut.saveBooks(bookList);
        csvInOut.saveLoans(loanList);
        
        return true;
    }
    
    // 도서 반납
    public boolean returnBook(String userId, String barcode) {
        Loan loan = findActiveLoan(userId, barcode);
        if (loan == null) {
            return false;
        }
        
        loan.returnBook();
        
        Book book = findBookByBarcode(barcode);
        if (book != null) {
            book.setAvailable(true);
        }
        
        csvInOut.saveBooks(bookList);
        csvInOut.saveLoans(loanList);
        
        return true;
    }
    
    // 관리자가 도서 반납 처리
    public boolean returnBookByAdmin(String barcode) {
        Loan loan = null;
        for (Loan l : loanList) {
            if (l.getBookId().equals(barcode) && !l.isReturned()) {
                loan = l;
                break;
            }
        }
        
        if (loan == null) {
            return false;
        }
        
        loan.returnBook();
        
        Book book = findBookByBarcode(barcode);
        if (book != null) {
            book.setAvailable(true);
        }
        
        csvInOut.saveBooks(bookList);
        csvInOut.saveLoans(loanList);
        
        return true;
    }
    
    // 활성 대출 찾기
    private Loan findActiveLoan(String userId, String barcode) {
        for (Loan loan : loanList) {
            if (loan.getUserId().equals(userId) && 
                loan.getBookId().equals(barcode) && 
                !loan.isReturned()) {
                return loan;
            }
        }
        return null;
    }
    
    // 사용자의 현재 대출 목록
    public List<Loan> getUserActiveLoans(String userId) {
        List<Loan> result = new ArrayList<>();
        for (Loan loan : loanList) {
            if (loan.getUserId().equals(userId) && !loan.isReturned()) {
                result.add(loan);
            }
        }
        return result;
    }
    
    // 모든 활성 대출 목록
    public List<Loan> getAllActiveLoans() {
        List<Loan> result = new ArrayList<>();
        for (Loan loan : loanList) {
            if (!loan.isReturned()) {
                result.add(loan);
            }
        }
        return result;
    }
    
    // 연체된 대출 목록
    public List<Loan> getOverdueLoans() {
        List<Loan> result = new ArrayList<>();
        for (Loan loan : loanList) {
            if (!loan.isReturned() && loan.isOverdue()) {
                result.add(loan);
            }
        }
        return result;
    }
}
