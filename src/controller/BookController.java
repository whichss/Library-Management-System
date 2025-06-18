package controller;

import model.Book;
import model.Library;
import model.Member;
import model.Admin;
import view.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookController {
    private Library library;
    private MainController mainController;
    private BookSearch bookSearchView;
    private BookManage bookManageView;
    private BookDetail bookDetailView;
    
    public BookController(Library library, MainController mainController) {
        this.library = library;
        this.mainController = mainController;
    }
    
    public void showBookSearch() {
        bookSearchView = new BookSearch();
        
        // 이벤트 리스너 등록
        bookSearchView.addSearchListener(this::performSearch);
        bookSearchView.addViewDetailListener(this::showDetailFromSearch);
        bookSearchView.addBackListener(e -> {
            bookSearchView.dispose();
            mainController.backToMainMenu();
        });
        
        // 전체 도서 목록 표시
        List<Book> allBooks = library.getAllBooks();
        bookSearchView.displaySearchResults(allBooks);
        bookSearchView.setVisible(true);
    }
    
    public void showBookManage() {
        bookManageView = new BookManage();
        
        // 도서 목록 표시
        List<Book> allBooks = library.getAllBooks();
        bookManageView.displayBooks(allBooks);
        
        // 이벤트 리스너 등록
        bookManageView.addAddBookListener(this::addBook);
        bookManageView.addSearchListener(this::performManageSearch);
        bookManageView.addEditListener(this::editBook);
        bookManageView.addDeleteListener(this::deleteBook);
        bookManageView.addBackListener(e -> {
            bookManageView.dispose();
            mainController.backToMainMenu();
        });
        
        bookManageView.setVisible(true);
    }
    
    public void showBookDetail(Book book) {
        if (mainController.getCurrentUser() == null) {
            bookSearchView.showMessage("도서 대출/반납은 회원만 가능합니다.");
            return;
        }
        
        bookDetailView = new BookDetail();
        bookDetailView.setBookInfo(book, mainController.getCurrentUser().getUserId());
        
        bookDetailView.addBorrowListener(this::borrowBook);
        bookDetailView.addReturnListener(this::returnBook);
        bookDetailView.addBackListener(e -> {
            bookDetailView.dispose();
            if (bookSearchView != null && bookSearchView.isVisible()) {
                List<Book> allBooks = library.getAllBooks();
                bookSearchView.displaySearchResults(allBooks);
            }
        });
        
        bookDetailView.setVisible(true);
    }
    
    private void performSearch(ActionEvent e) {
        String keyword = bookSearchView.getSearchKeyword();
        String searchType = bookSearchView.getSearchType();
        
        List<Book> results = keyword.isEmpty() ? 
            library.getAllBooks() : 
            library.searchBooks(keyword, searchType);
        
        bookSearchView.displaySearchResults(results);
    }
    
    private void showDetailFromSearch(ActionEvent e) {
        String barcode = bookSearchView.getSelectedBarcode();
        if (barcode == null) {
            bookSearchView.showError("도서를 선택해주세요.");
            return;
        }
        
        Book book = library.findBookByBarcode(barcode);
        if (book == null) {
            bookSearchView.showError("해당 도서를 찾을 수 없습니다.");
            return;
        }
        
        showBookDetail(book);
    }
    
    private void addBook(ActionEvent e) {
        // 입력값 가져오기 및 검증 (ISBN 제거됨)
        String title = bookManageView.getBookTitle().trim();
        String author = bookManageView.getAuthor().trim();
        String publisher = bookManageView.getPublisher().trim();
        String year = bookManageView.getYear().trim();
        String category = bookManageView.getCategory().trim();
        String priceStr = bookManageView.getPrice().trim();
        int quantity = bookManageView.getQuantity();
        
        if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || 
            year.isEmpty() || category.isEmpty() || priceStr.isEmpty()) {
            bookManageView.showError("모든 항목을 입력해주세요.");
            return;
        }
        
        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException ex) {
            bookManageView.showError("가격은 숫자로 입력해주세요.");
            return;
        }
        
        // 새 도서 생성 및 추가 (청구기호 자동 생성, ISBN 제거됨)
        Book newBook = new Book(title, author, publisher, year, category, price);
        List<String> addedBarcodes = library.addBooks(newBook, quantity);
        
        bookManageView.showMessage("도서 " + quantity + "권이 추가되었습니다.\n" +
                                 "바코드: " + String.join(", ", addedBarcodes));
        bookManageView.clearFields();
        
        // 도서 목록 새로고침
        refreshBookList();
    }
    
    private void performManageSearch(ActionEvent e) {
        String keyword = bookManageView.getSearchKeyword().trim();
        String searchType = bookManageView.getSearchType();
        
        List<Book> results = keyword.isEmpty() ? 
            library.getAllBooks() : 
            library.searchBooks(keyword, searchType);
        
        bookManageView.displayBooks(results);
    }
    
    private void editBook(ActionEvent e) {
        String barcode = bookManageView.getSelectedBarcode();
        if (barcode == null) {
            bookManageView.showError("수정할 도서를 선택해주세요.");
            return;
        }
        
        Book book = library.findBookByBarcode(barcode);
        if (book == null) {
            bookManageView.showError("해당 도서를 찾을 수 없습니다.");
            return;
        }
        
        BookEdit editDialog = new BookEdit(bookManageView, book);
        editDialog.addSaveListener(saveEvent -> {
            if (!editDialog.isValidInput()) {
                editDialog.showError("모든 필드를 올바르게 입력해주세요.");
                return;
            }
            
            Book updatedBook = editDialog.getUpdatedBook();
            if (updatedBook != null && library.updateBook(barcode, updatedBook)) {
                bookManageView.showMessage("도서 정보가 수정되었습니다.");
                editDialog.dispose();
                refreshBookList();
            } else {
                editDialog.showError("도서 수정에 실패했습니다.");
            }
        });
        
        editDialog.addCancelListener(cancelEvent -> editDialog.dispose());
        editDialog.setVisible(true);
    }
    
    private void deleteBook(ActionEvent e) {
        String barcode = bookManageView.getSelectedBarcode();
        if (barcode == null) {
            bookManageView.showError("삭제할 도서를 선택해주세요.");
            return;
        }
        
        Book book = library.findBookByBarcode(barcode);
        if (book == null) {
            bookManageView.showError("해당 도서를 찾을 수 없습니다.");
            return;
        }
        
        int result = javax.swing.JOptionPane.showConfirmDialog(
            bookManageView,
            "정말로 이 도서를 삭제하시겠습니까?\n제목: " + book.getTitle(),
            "도서 삭제 확인",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (result == javax.swing.JOptionPane.YES_OPTION) {
            if (library.deleteBook(barcode)) {
                bookManageView.showMessage("도서가 삭제되었습니다.");
                refreshBookList();
            } else {
                bookManageView.showError("도서를 삭제할 수 없습니다.\n대출중이거나 정상 상태가 아닙니다.");
            }
        }
    }
    
    private void borrowBook(ActionEvent e) {
        Book book = bookDetailView.getCurrentBook();
        String userId = bookDetailView.getUserId();
        String barcode = bookDetailView.getBarcode();
        
        // 사용자 대출 가능 여부 확인
        if (mainController.getCurrentUser() instanceof Member) {
            Member member = (Member) mainController.getCurrentUser();
            if (!member.canBorrow()) {
                String message = member.isSuspended() ? "대출이 정지된 상태입니다." :
                               "대출 한도를 초과했습니다.\n현재 " + member.getBorrowedCount() + "권 대출중";
                bookDetailView.showError(message);
                return;
            }
        } else if (mainController.getCurrentUser() instanceof Admin) {
            Admin admin = (Admin) mainController.getCurrentUser();
            if (!admin.canBorrow()) {
                bookDetailView.showError("대출 한도를 초과했습니다.\n현재 " + admin.getBorrowedCount() + "권 대출중");
                return;
            }
        }
        
        if (library.borrowBook(userId, barcode)) {
            // 사용자 대출 권수 증가
            if (mainController.getCurrentUser() instanceof Member) {
                ((Member) mainController.getCurrentUser()).borrowBook();
            } else if (mainController.getCurrentUser() instanceof Admin) {
                ((Admin) mainController.getCurrentUser()).borrowBook();
            }
            
            bookDetailView.showMessage("도서가 대출되었습니다.");
            bookDetailView.updateBookStatus(false);
            mainController.updateMainMenuBorrowInfo();
        } else {
            bookDetailView.showError("도서 대출에 실패했습니다.");
        }
    }
    
    private void returnBook(ActionEvent e) {
        String userId = bookDetailView.getUserId();
        String barcode = bookDetailView.getBarcode();
        
        if (library.returnBook(userId, barcode)) {
            // 사용자 대출 권수 감소
            if (mainController.getCurrentUser() instanceof Member) {
                ((Member) mainController.getCurrentUser()).returnBook();
            } else if (mainController.getCurrentUser() instanceof Admin) {
                ((Admin) mainController.getCurrentUser()).returnBook();
            }
            
            bookDetailView.showMessage("도서가 반납되었습니다.");
            bookDetailView.updateBookStatus(true);
            mainController.updateMainMenuBorrowInfo();
        } else {
            bookDetailView.showError("도서 반납에 실패했습니다.");
        }
    }
    
    private void refreshBookList() {
        List<Book> allBooks = library.getAllBooks();
        bookManageView.displayBooks(allBooks);
    }
}
