package model;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 관리 클래스
 * 모든 사용자 정보를 관리하고 사용자 관련 작업을 처리합니다
 */
public class Users {
    // 사용자 목록
    private List<User> userList;
    // CSV 파일 입출력 객체
    private CsvInOut csvInOut;
    
    // 생성자
    public Users() {
        csvInOut = new CsvInOut();
        userList = new ArrayList<>();
        loadUsers(); // 파일에서 사용자 정보 읽기
    }
    
    // 파일에서 사용자 정보 읽기
    public void loadUsers() {
        userList = csvInOut.readUsers();
    }
    
    // 파일로 사용자 정보 저장
    public void saveUsers() {
        csvInOut.saveUsers(userList);
    }
    
    // 새 사용자 추가
    public boolean addUser(User user) {
        // 이미 존재하는 ID인지 확인
        if (findUserById(user.getUserId()) != null) {
            return false; // 중복된 ID
        }
        userList.add(user);
        saveUsers(); // 파일에 저장
        return true;
    }
    
    // ID로 사용자 찾기
    public User findUserById(String userId) {
        for (User user : userList) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    // 로그인 확인
    public User login(String userId, String password) {
        User user = findUserById(userId);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; // 로그인 실패
    }
    
    // 모든 사용자 목록 반환
    public List<User> getAllUsers() {
        return new ArrayList<>(userList);
    }
    
    // 회원만 반환
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        for (User user : userList) {
            if (user instanceof Member) {
                members.add((Member) user);
            }
        }
        return members;
    }
    
    // 관리자만 반환
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        for (User user : userList) {
            if (user instanceof Admin) {
                admins.add((Admin) user);
            }
        }
        return admins;
    }
    
    // 사용자 삭제 (탈퇴)
    public boolean deleteUser(String userId) {
        User user = findUserById(userId);
        if (user != null) {
            userList.remove(user);
            saveUsers();
            return true;
        }
        return false;
    }
    
    // ID 중복 확인
    public boolean isUserIdExists(String userId) {
        return findUserById(userId) != null;
    }
    
    // 사용자 비밀번호 변경
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        User user = findUserById(userId);
        if (user != null && user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            saveUsers();
            return true;
        }
        return false;
    }
}