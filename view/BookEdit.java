package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.Book;

public class BookEdit extends JDialog {
    private JTextField callNumberField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField publisherField;
    private JTextField yearField;
    private JTextField categoryField;
    private JTextField priceField;
    private JComboBox<String> statusCombo;
    private JTextField noteField;
    private JButton saveButton;
    private JButton cancelButton;
    
    private Book currentBook;
    
    public BookEdit(Frame parent, Book book) {
        super(parent, "도서 정보 수정", true);
        this.currentBook = book;
        
        setSize(500, 550);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        initComponents();
        loadBookInfo();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 제목
        JLabel titleLabel = new JLabel("도서 정보 수정", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // 입력 폼 (ISBN 제거됨)
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
        addFormField(formPanel, "청구기호:", callNumberField = new JTextField());
        addFormField(formPanel, "제목:", titleField = new JTextField());
        addFormField(formPanel, "저자:", authorField = new JTextField());
        addFormField(formPanel, "출판사:", publisherField = new JTextField());
        addFormField(formPanel, "출판년도:", yearField = new JTextField());
        addFormField(formPanel, "카테고리:", categoryField = new JTextField());
        addFormField(formPanel, "가격:", priceField = new JTextField());
        
        JLabel statusLabel = new JLabel("상태:");
        statusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        statusLabel.setForeground(Color.BLACK);
        formPanel.add(statusLabel);
        
        statusCombo = new JComboBox<>(new String[]{"정상", "훼손", "분실", "폐기"});
        statusCombo.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        formPanel.add(statusCombo);
        
        addFormField(formPanel, "비고:", noteField = new JTextField());
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        saveButton = createModernButton("저장", new Color(46, 204, 113), new Color(39, 174, 96));
        saveButton.setPreferredSize(new Dimension(90, 40));
        
        cancelButton = createModernButton("취소", new Color(108, 117, 125), new Color(90, 98, 104));
        cancelButton.setPreferredSize(new Dimension(90, 40));
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
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
        
        button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void addFormField(JPanel panel, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        label.setForeground(Color.BLACK);
        field.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        field.setForeground(Color.BLACK);
        panel.add(label);
        panel.add(field);
    }
    
    private void loadBookInfo() {
        if (currentBook != null) {
            callNumberField.setText(currentBook.getCallNumber());
            titleField.setText(currentBook.getTitle());
            authorField.setText(currentBook.getAuthor());
            publisherField.setText(currentBook.getPublisher());
            yearField.setText(currentBook.getPublicationYear());
            categoryField.setText(currentBook.getCategory());
            priceField.setText(String.valueOf(currentBook.getPrice()));
            statusCombo.setSelectedItem(currentBook.getStatus());
            noteField.setText(currentBook.getNote());
        }
    }
    
    public Book getUpdatedBook() {
        try {
            Book updatedBook = new Book(
                currentBook.getBarcode(),
                callNumberField.getText().trim(),
                titleField.getText().trim(),
                authorField.getText().trim(),
                publisherField.getText().trim(),
                yearField.getText().trim(),
                categoryField.getText().trim(),
                currentBook.isAvailable(),
                (String) statusCombo.getSelectedItem(),
                currentBook.getPurchaseDate(),
                Integer.parseInt(priceField.getText().trim()),
                noteField.getText().trim()
            );
            return updatedBook;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public boolean isValidInput() {
        if (titleField.getText().trim().isEmpty() ||
            authorField.getText().trim().isEmpty() ||
            publisherField.getText().trim().isEmpty() ||
            yearField.getText().trim().isEmpty() ||
            categoryField.getText().trim().isEmpty() ||
            priceField.getText().trim().isEmpty()) {
            return false;
        }
        
        try {
            Integer.parseInt(priceField.getText().trim());
        } catch (NumberFormatException e) {
            return false;
        }
        
        return true;
    }
    
    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }
    
    public void addCancelListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
}
