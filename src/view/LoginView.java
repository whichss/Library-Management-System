package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class LoginView extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JButton guestButton;
    private JButton findIdButton;
    private JButton resetPasswordButton;
    
    private static final Color BLUE = new Color(59, 130, 246);
    private static final Color DARK_BLUE = new Color(37, 99, 235);
    private static final Color GRAY_900 = new Color(17, 24, 39);
    private static final Color GRAY_600 = new Color(75, 85, 99);
    private static final Color GRAY_500 = new Color(107, 114, 128);
    private static final Color GRAY_400 = new Color(156, 163, 175);
    private static final Color SUCCESS = new Color(34, 197, 94);
    
    public LoginView() {
        setTitle("📚 도서관");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initComponents();
    }
    
    private void initComponents() {
        // 메인 컨테이너
        JPanel mainContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(249, 250, 251),
                    0, getHeight(), new Color(243, 244, 246)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainContainer.setLayout(new GridBagLayout());
        
        // 로그인 카드
        JPanel loginCard = createLoginCard();
        
        // 중앙 정렬을 위한 GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        mainContainer.add(loginCard, gbc);
        
        add(mainContainer);
    }
    
    private JPanel createLoginCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(4, 4, getWidth()-4, getHeight()-4, 24, 24);
                g2d.setColor(new Color(0, 0, 0, 4));
                g2d.fillRoundRect(2, 2, getWidth()-2, getHeight()-2, 24, 24);
                
                // 카드 배경 (흰색)
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
            }
        };
        
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(420, 640));
        card.setBorder(BorderFactory.createEmptyBorder(48, 48, 48, 48));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        // 로고 섹션
        JPanel logoSection = createLogoSection();
        logoSection.setMaximumSize(new Dimension(324, 200));
        card.add(logoSection, gbc);
        
        // 간격
        gbc.gridy++;
        gbc.insets = new Insets(40, 0, 0, 0);
        JPanel spacer1 = new JPanel();
        spacer1.setOpaque(false);
        spacer1.setPreferredSize(new Dimension(324, 1));
        card.add(spacer1, gbc);
        
        // 입력 필드들
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel inputSection = createInputFields();
        inputSection.setMaximumSize(new Dimension(324, 150));
        card.add(inputSection, gbc);
        
        // 간격
        gbc.gridy++;
        gbc.insets = new Insets(24, 0, 0, 0);
        JPanel spacer2 = new JPanel();
        spacer2.setOpaque(false);
        spacer2.setPreferredSize(new Dimension(324, 1));
        card.add(spacer2, gbc);
        
        // 계정 관련 링크들
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel linksSection = createAccountLinks();
        linksSection.setMaximumSize(new Dimension(324, 40));
        card.add(linksSection, gbc);
        
        // 간격
        gbc.gridy++;
        gbc.insets = new Insets(32, 0, 0, 0);
        JPanel spacer3 = new JPanel();
        spacer3.setOpaque(false);
        spacer3.setPreferredSize(new Dimension(324, 1));
        card.add(spacer3, gbc);
        
        // 버튼들
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel buttonSection = createButtons();
        buttonSection.setMaximumSize(new Dimension(324, 150));
        card.add(buttonSection, gbc);
        
        return card;
    }
    
    private JPanel createLogoSection() {
        JPanel logoSection = new JPanel();
        logoSection.setOpaque(false);
        logoSection.setLayout(new BoxLayout(logoSection, BoxLayout.Y_AXIS));
        
        JLabel logoLabel = new JLabel("📚");
        logoLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 52));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoSection.add(logoLabel);
        
        logoSection.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // 메인 제목
        JLabel titleLabel = new JLabel("도서관에 오신 것을");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        titleLabel.setForeground(GRAY_900);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoSection.add(titleLabel);
        
        logoSection.add(Box.createRigidArea(new Dimension(0, 4)));
        
        JLabel titleLabel2 = new JLabel("환영합니다");
        titleLabel2.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        titleLabel2.setForeground(GRAY_900);
        titleLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoSection.add(titleLabel2);
        
        logoSection.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // 서브 텍스트
        JLabel subtitleLabel = new JLabel("아이디와 비밀번호로 로그인해주세요");
        subtitleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        subtitleLabel.setForeground(GRAY_600);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoSection.add(subtitleLabel);
        
        return logoSection;
    }
    
    private JPanel createInputFields() {
        JPanel inputSection = new JPanel();
        inputSection.setOpaque(false);
        inputSection.setLayout(new BoxLayout(inputSection, BoxLayout.Y_AXIS));
        
        // 아이디 입력
        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        idLabel.setForeground(GRAY_900);
        idLabel.setHorizontalAlignment(SwingConstants.LEFT);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputSection.add(idLabel);
        
        inputSection.add(Box.createRigidArea(new Dimension(0, 10)));
        
        userIdField = createTextField("아이디를 입력해주세요");
        userIdField.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputSection.add(userIdField);
        
        inputSection.add(Box.createRigidArea(new Dimension(0, 24)));
        
        // 비밀번호 입력
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        pwLabel.setForeground(GRAY_900);
        pwLabel.setHorizontalAlignment(SwingConstants.LEFT);
        pwLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputSection.add(pwLabel);
        
        inputSection.add(Box.createRigidArea(new Dimension(0, 10)));
        
        passwordField = createPasswordField("비밀번호를 입력해주세요");
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 비밀번호 필드에서 엔터키 처리
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
        
        inputSection.add(passwordField);
        
        return inputSection;
    }
    
    private JPanel createAccountLinks() {
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        linkPanel.setOpaque(false);
        linkPanel.setPreferredSize(new Dimension(324, 32));
        
        findIdButton = createTextButton("아이디 찾기");
        JLabel separator = new JLabel("•");
        separator.setForeground(GRAY_400);
        separator.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        resetPasswordButton = createTextButton("비밀번호 재설정");
        
        linkPanel.add(findIdButton);
        linkPanel.add(separator);
        linkPanel.add(resetPasswordButton);
        
        return linkPanel;
    }
    
    private JPanel createButtons() {
        JPanel buttonSection = new JPanel();
        buttonSection.setOpaque(false);
        buttonSection.setLayout(new BoxLayout(buttonSection, BoxLayout.Y_AXIS));
        
        // 로그인 버튼 (메인)
        loginButton = createPrimaryButton("로그인", BLUE);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonSection.add(loginButton);
        
        buttonSection.add(Box.createRigidArea(new Dimension(0, 16)));
        
        // 서브 버튼들 - 크기 조정
        JPanel buttonGroup = new JPanel(new GridLayout(1, 2, 16, 0));
        buttonGroup.setOpaque(false);
        buttonGroup.setPreferredSize(new Dimension(324, 52));
        buttonGroup.setMaximumSize(new Dimension(324, 52));
        
        signupButton = createSecondaryButton("회원가입", SUCCESS);
        guestButton = createSecondaryButton("둘러보기", GRAY_500);
        
        buttonGroup.add(signupButton);
        buttonGroup.add(guestButton);
        
        buttonSection.add(buttonGroup);
        
        buttonSection.add(Box.createRigidArea(new Dimension(0, 20)));
        
        return buttonSection;
    }
    
    // 로그인 수행 메서드 (엔터키와 버튼 클릭에서 공통 사용)
    private void performLogin() {
        String userId = getUserId();
        String password = getPassword();
        
        if (userId.isEmpty()) {
            showError("아이디를 입력해주세요.");
            userIdField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showError("비밀번호를 입력해주세요.");
            passwordField.requestFocus();
            return;
        }
        
        // 로그인 버튼의 액션을 실행
        loginButton.doClick();
    }
    
    private JButton createTextButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        button.setForeground(GRAY_500);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 호버 효과
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setForeground(BLUE);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setForeground(GRAY_500);
            }
        });
        
        return button;
    }
    
    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // 배경
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // 보더
                g2d.setColor(hasFocus() ? BLUE : new Color(229, 231, 235));
                g2d.setStroke(new BasicStroke(hasFocus() ? 2 : 1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                
                super.paintComponent(g);
                
                // 플레이스홀더 텍스트
                if (getText().isEmpty() && !hasFocus()) {
                    g2d.setColor(GRAY_400);
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = getInsets().left;
                    int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                    g2d.drawString(placeholder, x, y);
                }
            }
        };
        
        field.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        field.setForeground(GRAY_900);
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(324, 52));
        field.setMaximumSize(new Dimension(324, 52));
        field.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        field.setOpaque(false);
        
        return field;
    }
    
    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // 배경
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // 보더
                g2d.setColor(hasFocus() ? BLUE : new Color(229, 231, 235));
                g2d.setStroke(new BasicStroke(hasFocus() ? 2 : 1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                
                super.paintComponent(g);
                
                // 플레이스홀더 텍스트
                if (getPassword().length == 0 && !hasFocus()) {
                    g2d.setColor(GRAY_400);
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = getInsets().left;
                    int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                    g2d.drawString(placeholder, x, y);
                }
            }
        };
        
        field.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        field.setForeground(GRAY_900);
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(324, 52));
        field.setMaximumSize(new Dimension(324, 52));
        field.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        field.setOpaque(false);
        
        return field;
    }
    
    private JButton createPrimaryButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // 배경
                if (getModel().isPressed()) {
                    g2d.setColor(DARK_BLUE);
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(96, 165, 250));
                } else {
                    g2d.setColor(bgColor);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("맑은 고딕", Font.BOLD, 17));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(324, 56));
        button.setMaximumSize(new Dimension(324, 56));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        
        return button;
    }
    
    private JButton createSecondaryButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // 배경 (투명한 색상)
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 60));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 30));
                } else {
                    g2d.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 15));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        button.setForeground(bgColor);
        button.setPreferredSize(new Dimension(154, 52));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        
        return button;
    }
    
    // Getters
    public String getUserId() { return userIdField.getText(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    
    public void clearFields() {
        userIdField.setText("");
        passwordField.setText("");
    }
    
    // 이벤트 리스너
    public void addLoginListener(ActionListener listener) { loginButton.addActionListener(listener); }
    public void addSignupListener(ActionListener listener) { signupButton.addActionListener(listener); }
    public void addGuestListener(ActionListener listener) { guestButton.addActionListener(listener); }
    public void addFindIdListener(ActionListener listener) { findIdButton.addActionListener(listener); }
    public void addResetPasswordListener(ActionListener listener) { resetPasswordButton.addActionListener(listener); }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "알림", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
}