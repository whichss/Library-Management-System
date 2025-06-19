package controller;

import model.*;
import view.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UserController {
    private Users users;
    private Library library;
    private MainController mainController;
    private MyPage myPageView;
    private UserManage userManageView;
    
    public UserController(Users users, Library library, MainController mainController) {
        this.users = users;
        this.library = library;
        this.mainController = mainController;
    }
    
    public void showMyPage(User user) {
        myPageView = new MyPage(user);
        
        myPageView.addChangePasswordListener(e -> changePassword(user));
        myPageView.addWithdrawListener(e -> withdrawUser(user));
        myPageView.addBackListener(e -> {
            myPageView.dispose();
            mainController.backToMainMenu();
        });
        
        myPageView.setVisible(true);
    }
    
    public void showUserManage() {
        userManageView = new UserManage();
        
        userManageView.addRefreshListener(e -> refreshUserList());
        userManageView.addAddListener(e -> addUser());
        userManageView.addDeleteListener(e -> deleteUser());
        userManageView.addBackListener(e -> {
            userManageView.dispose();
            mainController.backToMainMenu();
        });
        
        refreshUserList();
        userManageView.setVisible(true);
    }
    
    private void changePassword(User user) {
        String currentPw = myPageView.getCurrentPassword();
        String newPw = myPageView.getNewPassword();
        String confirmPw = myPageView.getConfirmPassword();
        
        if (currentPw.isEmpty() || newPw.isEmpty() || confirmPw.isEmpty()) {
            myPageView.showError("모든 필드를 입력해주세요.");
            return;
        }
        
        if (newPw.length() < 4) {
            myPageView.showError("새 비밀번호는 4자 이상이어야 합니다.");
            return;
        }
        
        if (!newPw.equals(confirmPw)) {
            myPageView.showError("새 비밀번호가 일치하지 않습니다.");
            return;
        }
        
        if (users.changePassword(user.getUserId(), currentPw, newPw)) {
            myPageView.showMessage("비밀번호가 변경되었습니다.");
            myPageView.clearPasswordFields();
        } else {
            myPageView.showError("현재 비밀번호가 올바르지 않습니다.");
        }
    }
    
    private void withdrawUser(User user) {
        int result = javax.swing.JOptionPane.showConfirmDialog(
            myPageView,
            "정말로 회원 탈퇴하시겠습니까?\n탈퇴 후에는 복구할 수 없습니다.",
            "회원 탈퇴 확인",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (result == javax.swing.JOptionPane.YES_OPTION) {
            if (users.deleteUser(user.getUserId())) {
                myPageView.showMessage("회원 탈퇴가 완료되었습니다.");
                myPageView.dispose();
                mainController.logout();
            } else {
                myPageView.showError("회원 탈퇴에 실패했습니다.");
            }
        }
    }
    
    private void refreshUserList() {
        List<User> allUsers = users.getAllUsers();
        userManageView.displayUsers(allUsers);
    }
    
    private void addUser() {
        AdminAddUserDialog dialog = new AdminAddUserDialog(userManageView);
        
        dialog.addAddListener(e -> {
            String userId = dialog.getUserId().trim();
            String name = dialog.getName().trim();
            String password = dialog.getPassword();
            String confirmPassword = dialog.getConfirmPassword();
            boolean isAdmin = dialog.isAdminSelected();
            
            // 입력 검증
            if (userId.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                dialog.showError("모든 필드를 입력해주세요.");
                return;
            }
            
            if (userId.length() < 3) {
                dialog.showError("아이디는 3자 이상이어야 합니다.");
                return;
            }
            
            if (password.length() < 4) {
                dialog.showError("비밀번호는 4자 이상이어야 합니다.");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                dialog.showError("비밀번호가 일치하지 않습니다.");
                return;
            }
            
            if (users.isUserIdExists(userId)) {
                dialog.showError("이미 존재하는 아이디입니다.");
                return;
            }
            
            // 사용자 생성
            User newUser;
            if (isAdmin) {
                newUser = new Admin(userId, name, password);
            } else {
                newUser = new Member(userId, name, password);
            }
            
            if (users.addUser(newUser)) {
                String userType = isAdmin ? "관리자" : "일반회원";
                dialog.showMessage(userType + " 회원이 성공적으로 추가되었습니다.");
                dialog.clearFields();
                dialog.dispose();
                refreshUserList();
            } else {
                dialog.showError("회원 추가에 실패했습니다.");
            }
        });
        
        dialog.addCancelListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void deleteUser() {
        String userId = userManageView.getSelectedUserId();
        if (userId == null) {
            userManageView.showError("탈퇴시킬 회원을 선택해주세요.");
            return;
        }
        
        if (userId.equals("admin")) {
            userManageView.showError("관리자 계정은 삭제할 수 없습니다.");
            return;
        }
        
        int result = javax.swing.JOptionPane.showConfirmDialog(
            userManageView,
            "정말로 이 회원을 탈퇴시키시겠습니까?\n아이디: " + userId,
            "회원 탈퇴 확인",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (result == javax.swing.JOptionPane.YES_OPTION) {
            if (users.deleteUser(userId)) {
                userManageView.showMessage("회원이 탈퇴 처리되었습니다.");
                refreshUserList();
            } else {
                userManageView.showError("회원 탈퇴에 실패했습니다.");
            }
        }
    }
}
