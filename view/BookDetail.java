package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.Book;

public class BookDetail extends JFrame {
    private JLabel barcodeLabel;
    private JLabel callNumberLabel;
    private JLabel titleLabel;
    private JLabel authorLabel;
    private JLabel publisherLabel;
    private JLabel yearLabel;
    private JLabel categoryLabel;
    private JLabel statusLabel;
    private JLabel availableLabel;
    private JLabel priceLabel;
    private JLabel noteLabel;
    
    private JButton borrowButton;
    private JButton returnButton;
    private JButton backButton;
    
    private Book currentBook;
    private String userId;
    
    public BookDetail() {
        setTitle("도서관 관리 시스템 - 도서 상세정보");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // 제목 패널
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(44, 62, 80));
        titlePanel.setPreferredSize(new Dimension(0, 60));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel title = new JLabel("도서 상세정보");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.CENTER);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // 중앙 정보 패널
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JPanel infoPanel = new JPanel(new GridLayout(10, 2, 15, 15));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setPreferredSize(new Dimension(600, 450));
        
        // 정보 라벨들 추가 (ISBN 제거됨)
        addInfoField(infoPanel, "바코드:", barcodeLabel = new JLabel());
        addInfoField(infoPanel, "청구기호:", callNumberLabel = new JLabel());
        addInfoField(infoPanel, "제목:", titleLabel = new JLabel());
        addInfoField(infoPanel, "저자:", authorLabel = new JLabel());
        addInfoField(infoPanel, "출판사:", publisherLabel = new JLabel());
        addInfoField(infoPanel, "출판년도:", yearLabel = new JLabel());
        addInfoField(infoPanel, "카테고리:", categoryLabel = new JLabel());
        addInfoField(infoPanel, "상태:", statusLabel = new JLabel());
        addInfoField(infoPanel, "대출 가능:", availableLabel = new JLabel());
        addInfoField(infoPanel, "가격:", priceLabel = new JLabel());
        
        centerPanel.add(infoPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // 하단 버튼 패널
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        borrowButton = createModernButton("대출하기", new Color(52, 152, 219), new Color(41, 128, 185));
        borrowButton.setPreferredSize(new Dimension(120, 45));
        
        returnButton = createModernButton("반납하기", new Color(46, 204, 113), new Color(39, 174, 96));
        returnButton.setPreferredSize(new Dimension(120, 45));
        
        backButton = createModernButton("뒤로가기", new Color(149, 165, 166), new Color(127, 140, 141));
        backButton.setPreferredSize(new Dimension(120, 45));
        
        bottomPanel.add(borrowButton);
        bottomPanel.add(returnButton);
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
        
        button.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void addInfoField(JPanel panel, String labelText, JLabel valueLabel) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        label.setForeground(Color.BLACK);
        
        valueLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        valueLabel.setForeground(Color.BLACK);
        
        panel.add(label);
        panel.add(valueLabel);
    }
    
    public void setBookInfo(Book book, String userId) {
        this.currentBook = book;
        this.userId = userId;
        
        barcodeLabel.setText(book.getBarcode());
        callNumberLabel.setText(book.getCallNumber());
        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        publisherLabel.setText(book.getPublisher());
        yearLabel.setText(book.getPublicationYear());
        categoryLabel.setText(book.getCategory());
        statusLabel.setText(book.getStatus());
        availableLabel.setText(book.isAvailable() ? "예" : "아니오");
        priceLabel.setText(book.getPrice() + "원");
        
        updateButtonState();
    }
    
    private void updateButtonState() {
        if (currentBook != null) {
            boolean canBorrow = currentBook.canBorrow();
            borrowButton.setEnabled(canBorrow);
            returnButton.setEnabled(!currentBook.isAvailable());
        }
    }
    
    public void updateBookStatus(boolean isAvailable) {
        availableLabel.setText(isAvailable ? "예" : "아니오");
        currentBook.setAvailable(isAvailable);
        updateButtonState();
    }
    
    // Getters
    public Book getCurrentBook() { return currentBook; }
    public String getUserId() { return userId; }
    public String getBarcode() { return currentBook != null ? currentBook.getBarcode() : ""; }
    
    // 이벤트 리스너
    public void addBorrowListener(ActionListener listener) { borrowButton.addActionListener(listener); }
    public void addReturnListener(ActionListener listener) { returnButton.addActionListener(listener); }
    public void addBackListener(ActionListener listener) { backButton.addActionListener(listener); }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
}
