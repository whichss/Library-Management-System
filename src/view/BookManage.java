package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Book;

public class BookManage extends JFrame {
    private JTextField titleField, authorField, publisherField, yearField;
    private JTextField categoryField, priceField;
    private JSpinner quantitySpinner;
    private JButton addBookButton;
    
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JButton searchButton, deleteButton, editButton, backButton;
    
    public BookManage() {
        setTitle("도서관 관리 시스템 - 도서 관리");
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
        
        JLabel titleLabel = new JLabel("도서 관리");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // 분할 패널
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setLeftComponent(createAddPanel());
        splitPane.setRightComponent(createListPanel());
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
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
    
    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel sectionTitle = new JLabel("신규 도서 등록");
        sectionTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        sectionTitle.setForeground(Color.BLACK);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 10));
        formPanel.setBackground(Color.WHITE);
        
        addFormField(formPanel, "제목:", titleField = new JTextField());
        addFormField(formPanel, "저자:", authorField = new JTextField());
        addFormField(formPanel, "출판사:", publisherField = new JTextField());
        addFormField(formPanel, "출판년도:", yearField = new JTextField());
        addFormField(formPanel, "카테고리:", categoryField = new JTextField());
        addFormField(formPanel, "가격:", priceField = new JTextField());
        
        JLabel quantityLabel = new JLabel("수량:");
        quantityLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        quantityLabel.setForeground(Color.BLACK);
        formPanel.add(quantityLabel);
        
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantitySpinner.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        formPanel.add(quantitySpinner);
        
        addBookButton = createModernButton("도서 추가", new Color(46, 204, 113), new Color(39, 174, 96));
        addBookButton.setPreferredSize(new Dimension(160, 45));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.add(addBookButton);
        
        panel.add(sectionTitle, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
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
    
    private void addFormField(JPanel panel, String label, JTextField field) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        jLabel.setForeground(Color.BLACK);
        field.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        field.setForeground(Color.BLACK);
        panel.add(jLabel);
        panel.add(field);
    }
    
    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel sectionTitle = new JLabel("도서 목록 관리");
        sectionTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        sectionTitle.setForeground(Color.BLACK);
        
        // 검색 패널
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        
        searchTypeCombo = new JComboBox<>(new String[]{"전체", "바코드", "청구기호", "제목", "저자"});
        searchTypeCombo.setPreferredSize(new Dimension(100, 30));
        
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setForeground(Color.BLACK);
        
        searchButton = createModernButton("검색", new Color(52, 152, 219), new Color(41, 128, 185));
        searchButton.setPreferredSize(new Dimension(80, 35));
        
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // 테이블
        String[] columnNames = {"바코드", "청구기호", "제목", "저자", "출판사", "상태", "대출현황"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        bookTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 13));
        bookTable.setRowHeight(30);
        bookTable.setForeground(Color.BLACK);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        editButton = createModernButton("수정", new Color(241, 196, 15), new Color(212, 172, 13));
        editButton.setPreferredSize(new Dimension(90, 35));
        editButton.setEnabled(false);
        
        deleteButton = createModernButton("삭제", new Color(231, 76, 60), new Color(192, 57, 43));
        deleteButton.setPreferredSize(new Dimension(90, 35));
        deleteButton.setEnabled(false);
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // 테이블 선택 이벤트
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String loanStatus = (String) tableModel.getValueAt(selectedRow, 6);
                    String status = (String) tableModel.getValueAt(selectedRow, 5);
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(loanStatus.equals("대출가능") && status.equals("정상"));
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            }
        });
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(sectionTitle, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Getters (ISBN 제거됨)
    public String getBookTitle() { return titleField.getText(); }
    public String getAuthor() { return authorField.getText(); }
    public String getPublisher() { return publisherField.getText(); }
    public String getYear() { return yearField.getText(); }
    public String getCategory() { return categoryField.getText(); }
    public String getPrice() { return priceField.getText(); }
    public int getQuantity() { return (Integer) quantitySpinner.getValue(); }
    public String getSearchType() { return (String) searchTypeCombo.getSelectedItem(); }
    public String getSearchKeyword() { return searchField.getText(); }
    
    public String getSelectedBarcode() {
        int selectedRow = bookTable.getSelectedRow();
        return selectedRow >= 0 ? (String) tableModel.getValueAt(selectedRow, 0) : null;
    }
    
    public void displayBooks(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book book : books) {
            // 대출 현황 표시
            String loanStatus = book.isAvailable() ? "대출가능" : "대출중";
            
            Object[] row = {book.getBarcode(), book.getCallNumber(), book.getTitle(),
                           book.getAuthor(), book.getPublisher(), book.getStatus(), loanStatus};
            tableModel.addRow(row);
        }
    }
    
    public void clearFields() {
        titleField.setText("");
        authorField.setText("");
        publisherField.setText("");
        yearField.setText("");
        categoryField.setText("");
        priceField.setText("");
        quantitySpinner.setValue(1);
    }
    
    // 이벤트 리스너
    public void addAddBookListener(ActionListener listener) { addBookButton.addActionListener(listener); }
    public void addSearchListener(ActionListener listener) { 
        searchButton.addActionListener(listener);
        searchField.addActionListener(listener);
    }
    public void addEditListener(ActionListener listener) { editButton.addActionListener(listener); }
    public void addDeleteListener(ActionListener listener) { deleteButton.addActionListener(listener); }
    public void addBackListener(ActionListener listener) { backButton.addActionListener(listener); }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
}
