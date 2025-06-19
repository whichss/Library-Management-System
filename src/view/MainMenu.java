package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.Admin;
import model.Member;
import model.User;

public class MainMenu extends JFrame {
    private JLabel welcomeLabel;
    private JLabel borrowInfoLabel;
    private JButton bookSearchButton;
    private JButton myLoansButton;
    private JButton bookManageButton;
    private JButton allLoansButton;
    private JButton userManageButton;
    private JButton myPageButton;
    private JButton logoutButton;
    private JButton signupButton; // 비회원용 회원가입 버튼
    
    private User currentUser;
    private boolean isGuest;
    
    private static final Color BLUE = new Color(59, 130, 246);
    private static final Color DARK_BLUE = new Color(37, 99, 235);
    private static final Color SUCCESS = new Color(34, 197, 94);
    private static final Color GRAY_900 = new Color(17, 24, 39);
    private static final Color GRAY_700 = new Color(55, 65, 81);
    private static final Color GRAY_600 = new Color(75, 85, 99);
    private static final Color GRAY_500 = new Color(107, 114, 128);
    // 추가 색상 정의
    private static final Color PURPLE = new Color(139, 92, 246);
    private static final Color ORANGE = new Color(251, 146, 60);
    private static final Color RED = new Color(239, 68, 68);
    
    public MainMenu(User user) {
        this.currentUser = user;
        this.isGuest = false;
        initFrame();
        initComponents();
    }
    
    public MainMenu() {
        this.currentUser = null;
        this.isGuest = true;
        initFrame();
        initComponents();
    }
    
    private void initFrame() {
        setTitle("📚 도서관");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void initComponents() {
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
                    0, getHeight(), Color.WHITE
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainContainer.setLayout(new BorderLayout());
        
        // 상단 헤더
        JPanel headerPanel = createHeader();
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        
        // 메인 컨텐츠
        JPanel contentPanel = createContent();
        mainContainer.add(contentPanel, BorderLayout.CENTER);
        
        add(mainContainer);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 헤더 배경 (흰색)
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // 하단 보더
                g2d.setColor(new Color(229, 231, 235));
                g2d.fillRect(0, getHeight()-1, getWidth(), 1);
            }
        };
        header.setOpaque(false);
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 88));
        header.setBorder(BorderFactory.createEmptyBorder(24, 48, 24, 48));
        
        // 왼쪽 사용자 정보
        JPanel userInfo = new JPanel();
        userInfo.setOpaque(false);
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        
        // 인사말
        String welcomeText = isGuest ? "둘러보기 중이에요" : currentUser.getName() + "님, 반가워요!";
        welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        welcomeLabel.setForeground(GRAY_900);
        welcomeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        userInfo.add(welcomeLabel);
        
        if (!isGuest) {
            borrowInfoLabel = new JLabel();
            borrowInfoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            borrowInfoLabel.setForeground(GRAY_600);
            
            if (currentUser instanceof Member) {
                Member member = (Member) currentUser;
                borrowInfoLabel.setText("📚 " + member.getRemainingBorrowLimit() + "권 더 빌릴 수 있어요 (총 " + member.getMaxBorrowLimit() + "권)");
            } else if (currentUser instanceof Admin) {
                Admin admin = (Admin) currentUser;
                borrowInfoLabel.setText("👑 관리자 · 📚 " + admin.getRemainingBorrowLimit() + "권 더 빌릴 수 있어요 (총 " + admin.getMaxBorrowLimit() + "권)");
            }
            
            userInfo.add(Box.createRigidArea(new Dimension(0, 6)));
            userInfo.add(borrowInfoLabel);
        } else {
            JLabel guestInfo = new JLabel("🎯 회원가입하면 도서 대출도 가능해요!");
            guestInfo.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            guestInfo.setForeground(GRAY_600);
            userInfo.add(Box.createRigidArea(new Dimension(0, 6)));
            userInfo.add(guestInfo);
        }
        
        header.add(userInfo, BorderLayout.WEST);
        
        // 오른쪽 버튼들
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightButtons.setOpaque(false);
        
        if (isGuest) {
            // 비회원용: 회원가입 + 로그인 버튼
            signupButton = createHeaderButton("회원가입", SUCCESS);
            logoutButton = createHeaderButton("로그인", BLUE);
            
            rightButtons.add(signupButton);
            rightButtons.add(logoutButton);
        } else {
            // 회원용: 로그아웃 버튼만
            logoutButton = createHeaderButton("로그아웃", GRAY_500);
            rightButtons.add(logoutButton);
        }
        
        header.add(rightButtons, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new GridBagLayout());
        content.setBorder(BorderFactory.createEmptyBorder(40, 48, 40, 48));
        
        JPanel menuContainer = new JPanel();
        menuContainer.setOpaque(false);
        
        if (isGuest) {
            // 비회원 메뉴 - 검색만 가능
            menuContainer.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            // 메인 카드만 표시
            gbc.gridx = 0; gbc.gridy = 0;
            bookSearchButton = createMenuCard("🔍", "도서 검색", "원하는 책을 쉽게 찾아보세요", BLUE);
            bookSearchButton.setPreferredSize(new Dimension(400, 200));
            menuContainer.add(bookSearchButton, gbc);
            
        } else if (currentUser.getUserType().equals("ADMIN")) {
            // 관리자 메뉴 - 3x2 그리드
            menuContainer.setLayout(new GridLayout(2, 3, 24, 24));
            
            bookSearchButton = createMenuCard("", "도서 검색", "책을 검색하고 상세 정보를 확인해요", BLUE);
            myLoansButton = createMenuCard("", "나의 대출", "내가 빌린 책들을 관리해요", PURPLE);
            bookManageButton = createMenuCard("", "도서 관리", "새 책을 추가하고 정보를 수정해요", ORANGE);
            allLoansButton = createMenuCard("", "전체 대출", "모든 회원의 대출 현황을 관리해요", RED);
            userManageButton = createMenuCard("", "회원 관리", "회원 정보를 조회하고 관리해요", SUCCESS);
            myPageButton = createMenuCard("", "마이페이지", "내 계정 정보를 확인하고 수정해요", GRAY_700);
            
            menuContainer.add(bookSearchButton);
            menuContainer.add(myLoansButton);
            menuContainer.add(bookManageButton);
            menuContainer.add(allLoansButton);
            menuContainer.add(userManageButton);
            menuContainer.add(myPageButton);
        } else {
            // 일반 회원 메뉴 - 2x2 그리드
            menuContainer.setLayout(new GridLayout(2, 2, 24, 24));
            
            bookSearchButton = createMenuCard("🔍", "도서 검색", "책을 검색하고 상세 정보를 확인해요", BLUE);
            myLoansButton = createMenuCard("📋", "나의 대출", "내가 빌린 책들을 관리해요", PURPLE);
            myPageButton = createMenuCard("⚙️", "마이페이지", "내 계정 정보를 확인하고 수정해요", SUCCESS);
            
            JPanel emptyCard = new JPanel();
            emptyCard.setOpaque(false);
            
            menuContainer.add(bookSearchButton);
            menuContainer.add(myLoansButton);
            menuContainer.add(myPageButton);
            menuContainer.add(emptyCard);
        }
        
        content.add(menuContainer);
        return content;
    }

    
    private JButton createMenuCard(String icon, String title, String description, Color accentColor) {
        JButton card = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // 호버 효과를 위한 색상 계산
                Color bgColor = Color.WHITE;
                if (getModel().isPressed()) {
                    bgColor = new Color(248, 250, 252);
                } else if (getModel().isRollover()) {
                    bgColor = new Color(249, 250, 251);
                }
                
                // 그림자
                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(3, 3, getWidth()-3, getHeight()-3, 20, 20);
                g2d.setColor(new Color(0, 0, 0, 4));
                g2d.fillRoundRect(1, 1, getWidth()-1, getHeight()-1, 20, 20);
                
                // 배경
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // 보더
                g2d.setColor(new Color(229, 231, 235));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                
                super.paintComponent(g);
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        card.setFocusPainted(false);
        card.setContentAreaFilled(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(300, 180));
        
        // 카드 내용
        JPanel cardContent = new JPanel();
        cardContent.setOpaque(false);
        cardContent.setLayout(new BoxLayout(cardContent, BoxLayout.Y_AXIS));
        
        // 아이콘
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardContent.add(iconLabel);
        
        cardContent.add(Box.createRigidArea(new Dimension(0, 16)));
        
        // 제목
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 19));
        titleLabel.setForeground(GRAY_900);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardContent.add(titleLabel);
        
        cardContent.add(Box.createRigidArea(new Dimension(0, 8)));
        
        // 설명
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        descLabel.setForeground(GRAY_600);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardContent.add(descLabel);
        
        // 추가 여백으로 컬러바와 설명 간격 늘리기
        cardContent.add(Box.createRigidArea(new Dimension(0, 60))); // 50에서 60으로 더 증가
        
        card.add(cardContent, BorderLayout.CENTER);
        
        // 하단 컬러 바
        JPanel colorBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(accentColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
            }
        };
        colorBar.setOpaque(false);
        colorBar.setPreferredSize(new Dimension(0, 5)); // 컬러바 두께 증가
        card.add(colorBar, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JButton createHeaderButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // 배경
                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(100, 44));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        
        return button;
    }
    
    // 대출 정보 업데이트
    public void updateBorrowInfo() {
        if (borrowInfoLabel != null && currentUser != null) {
            if (currentUser instanceof Member) {
                Member member = (Member) currentUser;
                borrowInfoLabel.setText("📚 " + member.getRemainingBorrowLimit() + "권 더 빌릴 수 있어요 (총 " + member.getMaxBorrowLimit() + "권)");
            } else if (currentUser instanceof Admin) {
                Admin admin = (Admin) currentUser;
                borrowInfoLabel.setText("👑 관리자 · 📚 " + admin.getRemainingBorrowLimit() + "권 더 빌릴 수 있어요 (총 " + admin.getMaxBorrowLimit() + "권)");
            }
        }
    }
    
    // 이벤트 리스너
    public void addBookSearchListener(ActionListener listener) {
        if (bookSearchButton != null) bookSearchButton.addActionListener(listener);
    }
    public void addMyLoansListener(ActionListener listener) {
        if (myLoansButton != null) myLoansButton.addActionListener(listener);
    }
    public void addBookManageListener(ActionListener listener) {
        if (bookManageButton != null) bookManageButton.addActionListener(listener);
    }
    public void addAllLoansListener(ActionListener listener) {
        if (allLoansButton != null) allLoansButton.addActionListener(listener);
    }
    public void addUserManageListener(ActionListener listener) {
        if (userManageButton != null) userManageButton.addActionListener(listener);
    }
    public void addMyPageListener(ActionListener listener) {
        if (myPageButton != null) myPageButton.addActionListener(listener);
    }
    public void addLogoutListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
    public void addSignupListener(ActionListener listener) {
        if (signupButton != null) signupButton.addActionListener(listener);
    }
    
    public User getCurrentUser() { return currentUser; }
    public boolean isGuest() { return isGuest; }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "알림", JOptionPane.INFORMATION_MESSAGE);
    }
}