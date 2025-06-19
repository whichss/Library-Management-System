package model;

import java.time.LocalDate;

public class User {
    private String userId;
    private String name;
    private String password;
    private String userType;
    private LocalDate signupDate; // 회원 가입일 추가
    
    public User(String userId, String name, String password, String userType) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.userType = userType;
        this.signupDate = LocalDate.now(); // 기본값: 현재 날짜
    }
    
    // 생성자 오버로드 (CSV 읽기용)
    public User(String userId, String name, String password, String userType, LocalDate signupDate) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.userType = userType;
        this.signupDate = signupDate != null ? signupDate : LocalDate.now();
    }
    
    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getUserType() { return userType; }
    public LocalDate getSignupDate() { return signupDate; }
    
    // Setters
    public void setUserId(String userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setUserType(String userType) { this.userType = userType; }
    public void setSignupDate(LocalDate signupDate) { this.signupDate = signupDate; }
    
    public String toCsvString() {
        return userId + "," + name + "," + password + "," + userType + "," + signupDate;
    }
}
