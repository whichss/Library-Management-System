package controller;

import model.Admin;
import model.Member;
import model.User;
import model.Users;
import view.SignView;
import java.awt.event.ActionEvent;

public class SignupController {
    private SignView signView;
    private Users users;
    
    public SignupController(SignView signView, Users users) {
        this.signView = signView;
        this.users = users;
        
        this.signView.addSignupListener(this::signup);
        this.signView.addCancelListener(this::cancel);
    }
    
    private void signup(ActionEvent e) {
        String userId = signView.getUserId().trim();
        String name = signView.getName().trim();
        String password = signView.getPassword();
        String confirmPassword = signView.getConfirmPassword();
        // 일반 회원가입에서는 항상 일반회원으로 가입
        boolean isAdmin = false;
        
        if (!validateInput(userId, name, password, confirmPassword)) {
            return;
        }
        
        // 일반회원만 생성
        User newUser = new Member(userId, name, password);
        
        if (users.addUser(newUser)) {
            signView.showMessage("회원가입이 완료되었습니다!");
            signView.dispose();
        } else {
            signView.showError("이미 사용중인 아이디입니다.");
        }
    }
    
    private boolean validateInput(String userId, String name, String password, String confirmPassword) {
        if (userId.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signView.showError("모든 항목을 입력해주세요.");
            return false;
        }
        
        if (userId.length() < 3) {
            signView.showError("아이디는 3자 이상이어야 합니다.");
            return false;
        }
        
        if (password.length() < 4) {
            signView.showError("비밀번호는 4자 이상이어야 합니다.");
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            signView.showError("비밀번호가 일치하지 않습니다.");
            return false;
        }
        
        return true;
    }
    
    private void cancel(ActionEvent e) {
        signView.dispose();
    }
}
