package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Book;
import model.Loan;

public class LoanView extends JFrame {
    private JTable loanTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton returnButton;
    private JButton backButton;
    private JLabel titleLabel;
    private JLabel statusLabel;
    
    private boolean isAdmin;
    private List<Loan> currentLoans;
    
    public LoanView(boolean isAdmin) {
        this.isAdmin = isAdmin;
        
        if (isAdmin) {
            setTitle("도서관 관리 시스템 - 전체 대출 현황");
        } else {
            setTitle("도서관 관리 시스템 - 나의 대출 현황");
        }
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // 상단 제목 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(44, 62, 80));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        if (isAdmin) {
            titleLabel = new JLabel("전체 대출 현황");
        } else {
            titleLabel = new JLabel("나의 대출 현황");
        }
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE);
        topPanel.add(statusLabel, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // 중앙 테이블 패널
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 테이블 모델 설정
        String[] columnNames;
        if (isAdmin) {
            columnNames = new String[]{"대출번호", "사용자ID", "바코드", "도서명", "저자", "대출일", "반납예정일", "상태"};
        } else {
            columnNames = new String[]{"대출번호", "바코드", "도서명", "저자", "대출일", "반납예정일", "상태"};
        }
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        loanTable = new JTable(tableModel);
        loanTable.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        loanTable.setRowHeight(35);
        loanTable.setForeground(Color.BLACK);
        loanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loanTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14));
        
        // 상태 컬럼 렌더러 설정
        int statusColumn = isAdmin ? 7 : 6;
        loanTable.getColumnModel().getColumn(statusColumn).setCellRenderer(new StatusRenderer());
        
        // 더블클릭으로 반납 처리
        loanTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    returnSelectedBook();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(loanTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // 하단 버튼 패널
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        refreshButton = createModernButton("새로고침", new Color(52, 152, 219), new Color(41, 128, 185));
        refreshButton.setPreferredSize(new Dimension(120, 40));
        
        returnButton = createModernButton("선택 도서 반납", new Color(46, 204, 113), new Color(39, 174, 96));
        returnButton.setPreferredSize(new Dimension(150, 40));
        returnButton.setEnabled(false);
        
        backButton = createModernButton("뒤로가기", new Color(149, 165, 166), new Color(127, 140, 141));
        backButton.setPreferredSize(new Dimension(120, 40));
        
        bottomPanel.add(refreshButton);
        bottomPanel.add(returnButton);
        bottomPanel.add(backButton);
        
        // 테이블 선택 이벤트
        loanTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                returnButton.setEnabled(loanTable.getSelectedRow() >= 0);
            }
        });
        
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
    
    // 대출 목록 표시
    public void displayLoans(List<Loan> loans, List<Book> books) {
        this.currentLoans = loans;
        tableModel.setRowCount(0);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        if (loans.isEmpty()) {
            statusLabel.setText("대출 중인 도서: 0권");
        } else {
            int overdueCount = 0;
            
            for (Loan loan : loans) {
                Book book = findBookById(books, loan.getBookId());
                
                String bookTitle = book != null ? book.getTitle() : "정보 없음";
                String author = book != null ? book.getAuthor() : "정보 없음";
                String status = loan.isOverdue() ? "연체중" : "정상";
                
                if (loan.isOverdue()) {
                    overdueCount++;
                }
                
                Object[] row;
                if (isAdmin) {
                    row = new Object[]{
                        loan.getLoanId(),
                        loan.getUserId(),
                        loan.getBookId(),
                        bookTitle,
                        author,
                        loan.getLoanDate().format(formatter),
                        loan.getDueDate().format(formatter),
                        status
                    };
                } else {
                    row = new Object[]{
                        loan.getLoanId(),
                        loan.getBookId(),
                        bookTitle,
                        author,
                        loan.getLoanDate().format(formatter),
                        loan.getDueDate().format(formatter),
                        status
                    };
                }
                tableModel.addRow(row);
            }
            
            String statusText = "대출 중인 도서: " + loans.size() + "권";
            if (overdueCount > 0) {
                statusText += " (연체: " + overdueCount + "권)";
            }
            statusLabel.setText(statusText);
        }
    }
    
    private Book findBookById(List<Book> books, String barcode) {
        for (Book book : books) {
            if (book.getBarcode().equals(barcode)) {
                return book;
            }
        }
        return null;
    }
    
    public String getSelectedBookId() {
        int selectedRow = loanTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookIdColumn = isAdmin ? 2 : 1;
            return (String) tableModel.getValueAt(selectedRow, bookIdColumn);
        }
        return null;
    }
    
    public String getSelectedUserId() {
        if (isAdmin) {
            int selectedRow = loanTable.getSelectedRow();
            if (selectedRow >= 0) {
                return (String) tableModel.getValueAt(selectedRow, 1);
            }
        }
        return null;
    }
    
    private void returnSelectedBook() {
        if (returnButton != null && returnButton.getActionListeners().length > 0) {
            returnButton.getActionListeners()[0].actionPerformed(null);
        }
    }
    
    // 이벤트 리스너
    public void addRefreshListener(ActionListener listener) { refreshButton.addActionListener(listener); }
    public void addReturnListener(ActionListener listener) { returnButton.addActionListener(listener); }
    public void addBackListener(ActionListener listener) { backButton.addActionListener(listener); }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
    
    // 상태 렌더러
    class StatusRenderer extends JLabel implements TableCellRenderer {
        public StatusRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            setForeground(Color.BLACK);
            
            if (value.toString().equals("연체중")) {
                setForeground(new Color(231, 76, 60));
                setFont(new Font("맑은 고딕", Font.BOLD, 13));
            } else {
                setForeground(new Color(46, 204, 113));
                setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            }
            
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
}
