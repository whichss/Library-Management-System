package controller;

import model.*;
import view.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class LoanController {
    private Library library;
    private Users users;
    private MainController mainController;
    private LoanView loanView;
    
    public LoanController(Library library, Users users, MainController mainController) {
        this.library = library;
        this.users = users;
        this.mainController = mainController;
    }
    
    public void showMyLoans(String userId) {
        loanView = new LoanView(false); // 일반회원용
        
        loanView.addRefreshListener(e -> refreshMyLoans(userId));
        loanView.addReturnListener(e -> returnBook(userId));
        loanView.addBackListener(e -> {
            loanView.dispose();
            mainController.backToMainMenu();
        });
        
        refreshMyLoans(userId);
        loanView.setVisible(true);
    }
    
    public void showAllLoans() {
        loanView = new LoanView(true); // 관리자용
        
        loanView.addRefreshListener(e -> refreshAllLoans());
        loanView.addReturnListener(e -> returnBookByAdmin());
        loanView.addBackListener(e -> {
            loanView.dispose();
            mainController.backToMainMenu();
        });
        
        refreshAllLoans();
        loanView.setVisible(true);
    }
    
    private void refreshMyLoans(String userId) {
        List<Loan> loans = library.getUserActiveLoans(userId);
        List<Book> books = library.getAllBooks();
        loanView.displayLoans(loans, books);
    }
    
    private void refreshAllLoans() {
        List<Loan> loans = library.getAllActiveLoans();
        List<Book> books = library.getAllBooks();
        loanView.displayLoans(loans, books);
    }
    
    private void returnBook(String userId) {
        String barcode = loanView.getSelectedBookId();
        if (barcode == null) {
            loanView.showError("반납할 도서를 선택해주세요.");
            return;
        }
        
        if (library.returnBook(userId, barcode)) {
            // 사용자 대출 권수 감소
            User user = mainController.getCurrentUser();
            if (user instanceof Member) {
                ((Member) user).returnBook();
            } else if (user instanceof Admin) {
                ((Admin) user).returnBook();
            }
            
            loanView.showMessage("도서가 반납되었습니다.");
            refreshMyLoans(userId);
            mainController.updateMainMenuBorrowInfo();
        } else {
            loanView.showError("도서 반납에 실패했습니다.");
        }
    }
    
    private void returnBookByAdmin() {
        String barcode = loanView.getSelectedBookId();
        if (barcode == null) {
            loanView.showError("반납할 도서를 선택해주세요.");
            return;
        }
        
        if (library.returnBookByAdmin(barcode)) {
            loanView.showMessage("도서가 반납되었습니다.");
            refreshAllLoans();
        } else {
            loanView.showError("도서 반납에 실패했습니다.");
        }
    }
}
