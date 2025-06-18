package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.User;

public class MyPage extends JFrame {
    private JLabel userIdLabel;
    private JLabel nameLabel;
    private JLabel userTypeLabel;
    private JPasswordField currentPwField;
    private JPasswordField newPwField;
    private JPasswordField confirmPwField;
    private JButton changePwButton;
    private JButton withdrawButton;
    private JButton backButton;
    
    private User currentUser;
    
    public MyPage(User user) {
        this.currentUser = user;
        
        setTitle("도서관 관리 시스템 - 마이페이지");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // 상단 제목 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(149, 117, 205));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel titleLabel = new JLabel("마이페이지");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // 중앙 컨텐츠 패널
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 15, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(600, 400));
        
        // 사용자 정보
        addField(formPanel, "아이디:", userIdLabel = new JLabel(currentUser.getUserId()));
        addField(formPanel, "이름:", nameLabel = new JLabel(currentUser.getName()));
        
        String userType = currentUser.getUserType().equals("ADMIN") ? "관리자" : "일반회원";
        addField(formPanel, "회원유형:", userTypeLabel = new JLabel(userType));
        
        // 비밀번호 변경
        addField(formPanel, "현재 비밀번호:", currentPwField = new JPasswordField());
        addField(formPanel, "새 비밀번호:", newPwField = new JPasswordField());
        addField(formPanel, "비밀번호 확인:", confirmPwField = new JPasswordField());
        
        changePwButton = createModernButton("비밀번호 변경", new Color(92, 184, 92), new Color(76, 175, 80));
        formPanel.add(new JLabel(""));
        formPanel.add(changePwButton);
        
        withdrawButton = createModernButton("회원 탈퇴", new Color(217, 83, 79), new Color(192, 57, 43));
        
        if (currentUser.getUserId().equals("admin")) {
            withdrawButton.setEnabled(false);
        }
        
        formPanel.add(new JLabel(""));
        formPanel.add(withdrawButton);
        
        contentPanel.add(formPanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // 하단 버튼
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        backButton = createModernButton("뒤로가기", new Color(108, 117, 125), new Color(90, 98, 104));
        backButton.setPreferredSize(new Dimension(120, 40));
        bottomPanel.add(backButton);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }
    
    private JButton createModernButton(String text, Color primaryColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // 비활성화 상태 처리
                if (!isEnabled()) {
                    g2d.setColor(new Color(200, 200, 200));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    super.paintComponent(g);
                    return;
                }
                
                // 그림자 효과
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(2, 4, getWidth()-2, getHeight()-2, 15, 15);
                
                // 그라데이션 배경
                Color startColor, endColor;
                if (getModel().isPressed()) {
                    startColor = hoverColor.darker();
                    endColor = primaryColor.darker();
                } else if (getModel().isRollover()) {
                    startColor = hoverColor;
                    endColor = primaryColor;
                } else {
                    startColor = primaryColor;
                    endColor = primaryColor.darker();
                }
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, startColor,
                    0, getHeight(), endColor
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // 테두리
                g2d.setColor(primaryColor.darker().darker());
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void addField(JPanel panel, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        
        if (component instanceof JLabel) {
            ((JLabel) component).setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            ((JLabel) component).setForeground(Color.BLACK);
        } else if (component instanceof JPasswordField) {
            component.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            component.setForeground(Color.BLACK);
        }
        
        panel.add(label);
        panel.add(component);
    }
    
    // Getters
    public String getCurrentPassword() { return new String(currentPwField.getPassword()); }
    public String getNewPassword() { return new String(newPwField.getPassword()); }
    public String getConfirmPassword() { return new String(confirmPwField.getPassword()); }
    
    public void clearPasswordFields() {
        currentPwField.setText("");
        newPwField.setText("");
        confirmPwField.setText("");
    }
    
    // 이벤트 리스너
    public void addChangePasswordListener(ActionListener listener) { changePwButton.addActionListener(listener); }
    public void addWithdrawListener(ActionListener listener) { withdrawButton.addActionListener(listener); }
    public void addBackListener(ActionListener listener) { backButton.addActionListener(listener); }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
}
