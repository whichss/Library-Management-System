package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AdminAddUserDialog extends JDialog {
    private JTextField userIdField;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JRadioButton memberRadio;
    private JRadioButton adminRadio;
    private ButtonGroup userTypeGroup;
    private JButton addButton;
    private JButton cancelButton;
    
    // 컬러 시스템
    private static final Color BLUE = new Color(59, 130, 246);
    private static final Color DARK_BLUE = new Color(37, 99, 235);
    private static final Color SUCCESS = new Color(34, 197, 94);
    private static final Color GRAY_900 = new Color(17, 24, 39);
    private static final Color GRAY_600 = new Color(75, 85, 99);
    private static final Color GRAY_500 = new Color(107, 114, 128);
    private static final Color GRAY_400 = new Color(156, 163, 175);
    
    public AdminAddUserDialog(Frame parent) {
        super(parent, "회원 추가", true);
        setSize(700, 1000); // 다이얼로그 크기 더 크게
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        
        initComponents();
    }
    
    private void initComponents() {
        // 스크롤 가능한 메인 컨테이너
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // 메인 컨테이너
        JPanel mainContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 배경 그라데이션
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(249, 250, 251),
                    0, getHeight(), new Color(243, 244, 246)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainContainer.setLayout(new BorderLayout());
        mainContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // 폼 카드
        JPanel formCard = createFormCard();
        mainContainer.add(formCard, BorderLayout.CENTER);
        
        scrollPane.setViewportView(mainContainer);
        add(scrollPane);
    }
    
    private JPanel createFormCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 그림자
                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(3, 3, getWidth()-3, getHeight()-3, 20, 20);
                g2d.setColor(new Color(0, 0, 0, 4));
                g2d.fillRoundRect(1, 1, getWidth()-1, getHeight()-1, 20, 20);
                
                // 카드 배경
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // 헤더
        addHeader(card);
        
        card.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // 입력 필드들
        addInputSection(card);
        
        card.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // 권한 선택
        addUserTypeSection(card);
        
        card.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // 버튼들
        addButtonSection(card);
        
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        return card;
    }
    
    private void addHeader(JPanel parent) {
        // 아이콘
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(iconLabel);
        
        parent.add(Box.createRigidArea(new Dimension(0, 16)));
        
        // 제목
        JLabel titleLabel = new JLabel("새 회원 추가");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setForeground(GRAY_900);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(titleLabel);
        
        parent.add(Box.createRigidArea(new Dimension(0, 8)));
        
        // 서브 텍스트
        JLabel subtitleLabel = new JLabel("새로운 회원의 정보와 권한을 설정해주세요");
        subtitleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
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
    }
    
    private void addUserTypeSection(JPanel parent) {
        // 라벨
        JLabel typeLabel = new JLabel("회원 권한");
        typeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        typeLabel.setForeground(GRAY_900);
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(typeLabel);
        
        parent.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // 라디오 버튼 그룹
        userTypeGroup = new ButtonGroup();
        
        // 권한 선택 컨테이너 - 수직 배열로 변경
        JPanel typeContainer = new JPanel();
        typeContainer.setOpaque(false);
        typeContainer.setLayout(new BoxLayout(typeContainer, BoxLayout.Y_AXIS));
        typeContainer.setMaximumSize(new Dimension(550, 280)); // 높이 더 크게 확보
        typeContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 일반회원 옵션
        memberRadio = createUserTypeRadio("일반회원", "", SUCCESS, true);
        typeContainer.add(memberRadio);
        
        typeContainer.add(Box.createRigidArea(new Dimension(0, 30))); // 간격 더 증가
        
        // 관리자 옵션
        adminRadio = createUserTypeRadio("관리자", "", BLUE, false);
        typeContainer.add(adminRadio);
        
        userTypeGroup.add(memberRadio);
        userTypeGroup.add(adminRadio);
        
        parent.add(typeContainer);
    }
    
    private JRadioButton createUserTypeRadio(String title, String description, Color accentColor, boolean selected) {
        JRadioButton radio = new JRadioButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 배경
                if (isSelected()) {
                    g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    
                    // 보더
                    g2d.setColor(accentColor);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 12, 12);
                } else {
                    g2d.setColor(new Color(249, 250, 251));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    
                    // 보더
                    g2d.setColor(new Color(229, 231, 235));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                }
                
                super.paintComponent(g);
            }
        };
        
        radio.setOpaque(false);
        radio.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // 간격을 5로 줄여서 체크박스 바로 옵에 위치
        radio.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        radio.setSelected(selected);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        radio.setMaximumSize(new Dimension(550, 90));
        radio.setPreferredSize(new Dimension(550, 90));
        radio.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        radio.setText(title); // 라디오 버튼 자체에 제목 설정
        radio.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        radio.setForeground(GRAY_900);
        
        // 설명 라벨 (더 작은 글씨)
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14)); // 15에서 14로 감소
        descLabel.setForeground(GRAY_600);
        
        radio.add(descLabel);
        
        return radio;
    }
    
    private void addFormField(JPanel parent, String labelText, JComponent field) {
        parent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        label.setForeground(GRAY_900);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(label);
        
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
        
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(field);
    }
    
    private void addButtonSection(JPanel parent) {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(500, 56)); // 높이를 56으로 조정
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        addButton = createPrimaryButton("추가", BLUE);
        cancelButton = createSecondaryButton("취소", GRAY_500);
        
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        parent.add(buttonPanel);
    }
    
    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 배경
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // 보더
                g2d.setColor(hasFocus() ? BLUE : new Color(229, 231, 235));
                g2d.setStroke(new BasicStroke(hasFocus() ? 2 : 1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                
                super.paintComponent(g);
                
                // 플레이스홀더
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
        field.setPreferredSize(new Dimension(500, 55)); // 가로 450에서 500으로, 세로 50에서 55로 증가
        field.setMaximumSize(new Dimension(500, 55));
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
                
                // 배경
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // 보더
                g2d.setColor(hasFocus() ? BLUE : new Color(229, 231, 235));
                g2d.setStroke(new BasicStroke(hasFocus() ? 2 : 1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                
                super.paintComponent(g);
                
                // 플레이스홀더
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
        field.setPreferredSize(new Dimension(500, 55)); // 가로 450에서 500으로, 세로 50에서 55로 증가
        field.setMaximumSize(new Dimension(500, 55));
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
        button.setPreferredSize(new Dimension(240, 56));
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
        button.setPreferredSize(new Dimension(240, 56));
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
    public boolean isAdminSelected() { return adminRadio.isSelected(); }
    
    public void clearFields() {
        userIdField.setText("");
        nameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        memberRadio.setSelected(true);
    }
    
    // 이벤트 리스너
    public void addAddListener(ActionListener listener) { addButton.addActionListener(listener); }
    public void addCancelListener(ActionListener listener) { cancelButton.addActionListener(listener); }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "알림", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
}
