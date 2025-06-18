package controller;

import model.*;
import view.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainController {
    private Users users;
    private Library library;
    private LoginView loginView;
    private MainMenu mainMenu;
    private LoginController loginController;
    private BookController bookController;
    private LoanController loanController;
    private UserController userController;
    private User currentUser;
    
    public MainController() {
        users = new Users();
        library = new Library();
        
        loginView = new LoginView();
        loginController = new LoginController(loginView, users);
        loginController.setMainController(this);
        
        bookController = new BookController(library, this);
        loanController = new LoanController(library, users, this);
        userController = new UserController(users, library, this);
    }
    
    public void start() {
        loginController.showLoginView();
    }
    
    public void showMainMenu(User user) {
        this.currentUser = user;
        mainMenu = new MainMenu(user);
        setupMainMenuListeners();
        mainMenu.setVisible(true);
    }
    
    public void showGuestMenu() {
        this.currentUser = null;
        mainMenu = new MainMenu();
        setupGuestMenuListeners();
        mainMenu.setVisible(true);
    }
    
    private void setupMainMenuListeners() {
        mainMenu.addBookSearchListener(e -> {
            mainMenu.setVisible(false);
            bookController.showBookSearch();
        });
        
        mainMenu.addMyLoansListener(e -> {
            mainMenu.setVisible(false);
            loanController.showMyLoans(currentUser.getUserId());
        });
        
        mainMenu.addMyPageListener(e -> {
            mainMenu.setVisible(false);
            userController.showMyPage(currentUser);
        });
        
        mainMenu.addLogoutListener(e -> {
            int result = javax.swing.JOptionPane.showConfirmDialog(
                mainMenu, "로그아웃 하시겠습니까?", "로그아웃 확인",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            
            if (result == javax.swing.JOptionPane.YES_OPTION) {
                logout();
            }
        });
        
        if (currentUser.getUserType().equals("ADMIN")) {
            mainMenu.addBookManageListener(e -> {
                mainMenu.setVisible(false);
                bookController.showBookManage();
            });
            
            mainMenu.addAllLoansListener(e -> {
                mainMenu.setVisible(false);
                loanController.showAllLoans();
            });
            
            mainMenu.addUserManageListener(e -> {
                mainMenu.setVisible(false);
                userController.showUserManage();
            });
        }
    }
    
    private void setupGuestMenuListeners() {
        mainMenu.addBookSearchListener(e -> {
            mainMenu.setVisible(false);
            bookController.showBookSearch();
        });
        
        mainMenu.addSignupListener(e -> {
            // 회원가입 화면으로 이동
            loginController.showSignup();
        });
        
        mainMenu.addLogoutListener(e -> {
            // 비회원은 로그인 화면으로
            mainMenu.dispose();
            loginView.setVisible(true);
        });
    }
    
    public User getCurrentUser() { return currentUser; }
    
    public void backToMainMenu() {
        if (mainMenu != null) {
            mainMenu.setVisible(true);
        }
    }
    
    public void updateMainMenuBorrowInfo() {
        if (mainMenu != null && currentUser != null) {
            mainMenu.updateBorrowInfo();
            users.saveUsers();
        }
    }
    
    public void logout() {
        currentUser = null;
        if (mainMenu != null) {
            mainMenu.dispose();
        }
        loginView.setVisible(true);
    }
}
