package controller;

import model.User;
import model.Users;
import view.LoginView;
import view.SignView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class LoginController {
    private LoginView loginView;
    private Users users;
    private MainController mainController;
    
    public LoginController(LoginView loginView, Users users) {
        this.loginView = loginView;
        this.users = users;
        
        this.loginView.addLoginListener(this::login);
        this.loginView.addSignupListener(this::showSignup);
        this.loginView.addGuestListener(this::guestAccess);
        this.loginView.addFindIdListener(this::findUserId);
        this.loginView.addResetPasswordListener(this::resetPassword);
    }
    
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    
    private void login(ActionEvent e) {
        String userId = loginView.getUserId();
        String password = loginView.getPassword();
        
        if (userId.isEmpty() || password.isEmpty()) {
            loginView.showError("아이디와 비밀번호를 입력해주세요.");
            return;
        }
        
        User user = users.login(userId, password);
        
        if (user != null) {
            loginView.showMessage("로그인 성공! " + user.getName() + "님 환영합니다.");
            loginView.clearFields();
            loginView.setVisible(false);
            
            if (mainController != null) {
                mainController.showMainMenu(user);
            }
        } else {
            loginView.showError("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }
    
    private void showSignup(ActionEvent e) {
        SignView signView = new SignView();
        SignupController signupController = new SignupController(signView, users);
        signView.setVisible(true);
    }
    
    private void guestAccess(ActionEvent e) {
        loginView.setVisible(false);
        if (mainController != null) {
            mainController.showGuestMenu();
        }
    }
    
    private void findUserId(ActionEvent e) {
        String name = JOptionPane.showInputDialog(
            loginView,
            "이름을 입력해주세요:",
            "아이디 찾기",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (name != null && !name.trim().isEmpty()) {
            User foundUser = null;
            for (User user : users.getAllUsers()) {
                if (user.getName().equals(name.trim())) {
                    foundUser = user;
                    break;
                }
            }
            
            if (foundUser != null) {
                String maskedId = maskUserId(foundUser.getUserId());
                JOptionPane.showMessageDialog(
                    loginView,
                    "찾은 아이디: " + maskedId + "\n\n" +
                    "가입일: " + foundUser.getSignupDate(),
                    "아이디 찾기 결과",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    loginView,
                    "입력하신 이름으로 가입된 계정을 찾을 수 없습니다.",
                    "아이디 찾기 실패",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }
    
    private void resetPassword(ActionEvent e) {
        String userId = JOptionPane.showInputDialog(
            loginView,
            "아이디를 입력해주세요:",
            "비밀번호 재설정",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (userId != null && !userId.trim().isEmpty()) {
            User user = users.findUserById(userId.trim());
            
            if (user != null) {
                String name = JOptionPane.showInputDialog(
                    loginView,
                    "본인 확인을 위해 이름을 입력해주세요:",
                    "본인 확인",
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (name != null && name.equals(user.getName())) {
                    String newPassword = JOptionPane.showInputDialog(
                        loginView,
                        "새로운 비밀번호를 입력해주세요:",
                        "비밀번호 재설정",
                        JOptionPane.QUESTION_MESSAGE
                    );
                    
                    if (newPassword != null && !newPassword.trim().isEmpty()) {
                        String confirmPassword = JOptionPane.showInputDialog(
                            loginView,
                            "비밀번호를 다시 한 번 입력해주세요:",
                            "비밀번호 확인",
                            JOptionPane.QUESTION_MESSAGE
                        );
                        
                        if (confirmPassword != null && confirmPassword.equals(newPassword)) {
                            user.setPassword(newPassword);
                            users.saveUsers();
                            
                            JOptionPane.showMessageDialog(
                                loginView,
                                "비밀번호가 성공적으로 변경되었습니다.\n새 비밀번호로 로그인해주세요.",
                                "비밀번호 재설정 완료",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                loginView,
                                "비밀번호가 일치하지 않습니다.",
                                "비밀번호 재설정 실패",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        loginView,
                        "이름이 일치하지 않습니다.",
                        "본인 확인 실패",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                    loginView,
                    "입력하신 아이디로 가입된 계정을 찾을 수 없습니다.",
                    "비밀번호 재설정 실패",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }
    
    private String maskUserId(String userId) {
        if (userId.length() <= 2) {
            return userId;
        }
        
        StringBuilder masked = new StringBuilder();
        masked.append(userId.substring(0, 2));
        for (int i = 2; i < userId.length(); i++) {
            masked.append("*");
        }
        return masked.toString();
    }
    
    public void showLoginView() {
        loginView.setVisible(true);
    }
    
    public void showSignup() {
        SignView signView = new SignView();
        SignupController signupController = new SignupController(signView, users);
        signView.setVisible(true);
    }
}