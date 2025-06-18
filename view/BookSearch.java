package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Book;

public class BookSearch extends JFrame {
    private JComboBox<String> searchTypeCombo;
    private JTextField searchField;
    private JButton searchButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JButton viewDetailButton;
    private JButton backButton;
    
    public BookSearch() {
        setTitle("도서관 관리 시스템 - 도서 검색");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // 상단 검색 패널
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel searchLabel = new JLabel("검색:");
        searchLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        searchLabel.setForeground(Color.BLACK);
        searchPanel.add(searchLabel);
        
        searchTypeCombo = new JComboBox<>(new String[]{"전체", "제목", "저자", "카테고리", "청구기호"});
        searchTypeCombo.setPreferredSize(new Dimension(120, 35));
        searchTypeCombo.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        searchPanel.add(searchTypeCombo);
        
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(400, 35));
        searchField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        searchField.setForeground(Color.BLACK);
        searchPanel.add(searchField);
        
        searchButton = createModernButton("검색", new Color(52, 152, 219), new Color(41, 128, 185));
        searchButton.setPreferredSize(new Dimension(90, 40));
        searchPanel.add(searchButton);
        
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        
        // 중앙 테이블 패널
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        String[] columnNames = {"바코드", "청구기호", "제목", "저자", "출판사", "카테고리", "상태", "대출현황"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        resultTable = new JTable(tableModel);
        resultTable.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        resultTable.setRowHeight(30);
        resultTable.setForeground(Color.BLACK);
        resultTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14));
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 상태 컬럼 렌더러
        resultTable.getColumnModel().getColumn(6).setCellRenderer(new StatusRenderer());
        
        // 더블클릭 이벤트
        resultTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && viewDetailButton.getActionListeners().length > 0) {
                    viewDetailButton.getActionListeners()[0].actionPerformed(null);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // 하단 버튼 패널
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        viewDetailButton = createModernButton("상세보기", new Color(46, 204, 113), new Color(39, 174, 96));
        viewDetailButton.setPreferredSize(new Dimension(120, 40));
        
        backButton = createModernButton("뒤로가기", new Color(149, 165, 166), new Color(127, 140, 141));
        backButton.setPreferredSize(new Dimension(120, 40));
        
        bottomPanel.add(viewDetailButton);
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
        
        button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    // Getters
    public String getSearchKeyword() { return searchField.getText(); }
    public String getSearchType() { return (String) searchTypeCombo.getSelectedItem(); }
    
    public String getSelectedBarcode() {
        int selectedRow = resultTable.getSelectedRow();
        return selectedRow >= 0 ? (String) tableModel.getValueAt(selectedRow, 0) : null;
    }
    
    public void displaySearchResults(List<Book> books) {
        tableModel.setRowCount(0);
        
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "검색 결과가 없습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Book book : books) {
                // 대출 현황 표시: 대출가능하면 "대출가능", 불가능하면 "대출중"
                String loanStatus = book.isAvailable() ? "대출가능" : "대출중";
                
                Object[] row = {
                    book.getBarcode(),
                    book.getCallNumber(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublisher(),
                    book.getCategory(),
                    book.getStatus(),
                    loanStatus
                };
                tableModel.addRow(row);
            }
        }
    }
    
    public void clearFields() {
        searchField.setText("");
        searchTypeCombo.setSelectedIndex(0);
        tableModel.setRowCount(0);
    }
    
    // 이벤트 리스너
    public void addSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
        searchField.addActionListener(listener);
    }
    
    public void addViewDetailListener(ActionListener listener) {
        viewDetailButton.addActionListener(listener);
    }
    
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
    
    // 상태 컬럼 렌더러
    class StatusRenderer extends JLabel implements TableCellRenderer {
        public StatusRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            setForeground(Color.BLACK);
            
            switch(value.toString()) {
                case "정상":
                    setForeground(new Color(46, 204, 113));
                    break;
                case "훼손":
                    setForeground(new Color(241, 196, 15));
                    break;
                case "분실":
                case "폐기":
                    setForeground(new Color(231, 76, 60));
                    break;
            }
            
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
}
