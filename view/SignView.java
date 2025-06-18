package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SignView extends JFrame {
    private JTextField userIdField;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton signupButton;
    private JButton cancelButton;
    
    // 컬러 시스템
    private static final Color BLUE = new Color(59, 130, 246);
    private static final Color DARK_BLUE = new Color(37, 99, 235);
    private static final Color SUCCESS = new Color(34, 197, 94);
    private static final Color GRAY_900 = new Color(17, 24, 39);
    private static final Color GRAY_600 = new Color(75, 85, 99);
    private static final Color GRAY_500 = new Color(107, 114, 128);
    private static final Color GRAY_400 = new Color(156, 163, 175);
    
    public SignView() {
        setTitle("📚 회원가입");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
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
                
                // 배경
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(249, 250, 251),
                    0, getHeight(), new Color(243, 244, 246)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainContainer.setLayout(new BorderLayout());
        
        // 스크롤 패널로 감싸기
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // 중앙 정렬을 위한 패널
        JPanel centerPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Container parent = getParent();
                if (parent != null) {
                    return new Dimension(d.width, Math.max(d.height, parent.getHeight()));
                }
                return d;
            }
        };
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        // 중앙 정렬을 위한 GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 0, 30, 0);
        
        // 회원가입 카드
        JPanel signupCard = createSignupCard();
        centerPanel.add(signupCard, gbc);
        
        scrollPane.setViewportView(centerPanel);
        mainContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(mainContainer);
    }
    
    private JPanel createSignupCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 미묘한 그림자
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
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(500, 850));
        card.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // 헤더
        addHeader(card);
        
        card.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // 입력 필드들
        addInputSection(card);
        
        card.add(Box.createRigidArea(new Dimension(0, 32)));
        
        // 버튼들
        addButtonSection(card);
        
        // 추가 여백 공간
        card.add(Box.createRigidArea(new Dimension(0, 40)));
        
        return card;
    }
    
    private void addHeader(JPanel parent) {
        
        // 메인 제목
        JLabel titleLabel = new JLabel("회원가입하고");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        titleLabel.setForeground(GRAY_900);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(titleLabel);
        
        parent.add(Box.createRigidArea(new Dimension(0, 4)));
        
        JLabel titleLabel2 = new JLabel("도서관을 이용해보세요");
        titleLabel2.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        titleLabel2.setForeground(GRAY_900);
        titleLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(titleLabel2);
        
        parent.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // 서브 텍스트
        JLabel subtitleLabel = new JLabel("도서 대출, 검색 등 모든 기능을 이용할 수 있어요");
        subtitleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        subtitleLabel.setForeground(GRAY_600);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(subtitleLabel);
    }
    
    private void addInputSection(JPanel parent) {
        // 아이디
        addFormField(parent, "아이디", userIdField = createTextField("사용할 아이디를 입력해주세요"));
        
        // 이름
        addFormField(parent, "이름", nameField = createTextField("실명을 입력해주세요"));
        
        // 비밀번호
        addFormField(parent, "비밀번호", passwordField = createPasswordField("안전한 비밀번호를 설정해주세요"));
        
        // 비밀번호 확인
        addFormField(parent, "비밀번호 확인", confirmPasswordField = createPasswordField("비밀번호를 다시 입력해주세요"));
        
        // 혜택 안내
        parent.add(Box.createRigidArea(new Dimension(0, 24)));
        
        JPanel benefitPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 배경
                g2d.setColor(new Color(239, 246, 255));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // 보더
                g2d.setColor(new Color(147, 197, 253));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
            }
        };
        
        benefitPanel.setOpaque(false);
        benefitPanel.setLayout(new BoxLayout(benefitPanel, BoxLayout.Y_AXIS));
        benefitPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        benefitPanel.setMaximumSize(new Dimension(400, 90));
        benefitPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        JLabel benefitTitle = new JLabel("일반회원으로 가입됩니다");
        benefitTitle.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        benefitTitle.setForeground(GRAY_900);
        benefitTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        benefitPanel.add(benefitTitle);
        
        benefitPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        
        JLabel benefitDesc = new JLabel("최대 5권까지 대출 가능 • 모든 도서 검색 가능");
        benefitDesc.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        benefitDesc.setForeground(GRAY_600);
        benefitDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        benefitPanel.add(benefitDesc);
        
        parent.add(benefitPanel);
    }
    
    private void addFormField(JPanel parent, String labelText, JComponent field) {
        parent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        label.setForeground(GRAY_900);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(label);
        
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
        
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(field);
    }
    
    private void addButtonSection(JPanel parent) {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(400, 56));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        signupButton = createPrimaryButton("가입 완료", BLUE);
        cancelButton = createSecondaryButton("취소", GRAY_500);
        
        buttonPanel.add(signupButton);
        buttonPanel.add(cancelButton);
        
        parent.add(buttonPanel);
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
        field.setPreferredSize(new Dimension(400, 52));
        field.setMaximumSize(new Dimension(400, 52));
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
        field.setPreferredSize(new Dimension(400, 52));
        field.setMaximumSize(new Dimension(400, 52));
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
        button.setPreferredSize(new Dimension(192, 56));
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
        button.setPreferredSize(new Dimension(192, 56));
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
    public String getName() { return nameField.getText(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    public String getConfirmPassword() { return new String(confirmPasswordField.getPassword()); }
    public boolean isAdminSelected() { return false; } // 항상 일반회원
    
    public void clearFields() {
        userIdField.setText("");
        nameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
    
    // 이벤트 리스너
    public void addSignupListener(ActionListener listener) { signupButton.addActionListener(listener); }
    public void addCancelListener(ActionListener listener) { cancelButton.addActionListener(listener); }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "알림", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
}
