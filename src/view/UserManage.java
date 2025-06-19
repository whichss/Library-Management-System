package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.User;

public class UserManage extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton backButton;
    private JLabel totalLabel;
    
    public UserManage() {
        setTitle("도서관 관리 시스템 - 회원 관리");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // 상단 제목 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(119, 147, 60));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel titleLabel = new JLabel("회원 관리");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        totalLabel = new JLabel();
        totalLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        totalLabel.setForeground(Color.WHITE);
        topPanel.add(totalLabel, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // 중앙 테이블 패널
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[] columnNames = {"아이디", "이름", "회원유형", "가입일"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        userTable = new JTable(tableModel);
        userTable.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        userTable.setRowHeight(35);
        userTable.setForeground(Color.BLACK);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14));
        
        // 회원유형 컬럼 렌더러
        userTable.getColumnModel().getColumn(2).setCellRenderer(new UserTypeRenderer());
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // 하단 버튼 패널
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        refreshButton = createModernButton("새로고침", new Color(91, 192, 222), new Color(73, 169, 191));
        refreshButton.setPreferredSize(new Dimension(120, 40));
        
        addButton = createModernButton("회원 추가", new Color(46, 204, 113), new Color(39, 174, 96));
        addButton.setPreferredSize(new Dimension(120, 40));
        
        deleteButton = createModernButton("회원 탈퇴", new Color(217, 83, 79), new Color(192, 57, 43));
        deleteButton.setPreferredSize(new Dimension(120, 40));
        deleteButton.setEnabled(false);
        
        backButton = createModernButton("뒤로가기", new Color(108, 117, 125), new Color(90, 98, 104));
        backButton.setPreferredSize(new Dimension(120, 40));
        
        bottomPanel.add(refreshButton);
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(backButton);
        
        // 테이블 선택 이벤트
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String userId = (String) tableModel.getValueAt(selectedRow, 0);
                    deleteButton.setEnabled(!userId.equals("admin"));
                } else {
                    deleteButton.setEnabled(false);
                }
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
    
    public void displayUsers(List<User> users) {
        tableModel.setRowCount(0);
        
        int adminCount = 0;
        int memberCount = 0;
        
        for (User user : users) {
            String userType = user.getUserType();
            String userTypeDisplay = userType.equals("ADMIN") ? "관리자" : "일반회원";
            
            if (userType.equals("ADMIN")) {
                adminCount++;
            } else {
                memberCount++;
            }
            
            Object[] row = {
                user.getUserId(),
                user.getName(),
                userTypeDisplay,
                user.getSignupDate().toString()
            };
            tableModel.addRow(row);
        }
        
        totalLabel.setText("총 " + users.size() + "명 (관리자: " + adminCount + ", 회원: " + memberCount + ")");
    }
    
    public String getSelectedUserId() {
        int selectedRow = userTable.getSelectedRow();
        return selectedRow >= 0 ? (String) tableModel.getValueAt(selectedRow, 0) : null;
    }
    
    // 이벤트 리스너
    public void addRefreshListener(ActionListener listener) { refreshButton.addActionListener(listener); }
    public void addAddListener(ActionListener listener) { addButton.addActionListener(listener); }
    public void addDeleteListener(ActionListener listener) { deleteButton.addActionListener(listener); }
    public void addBackListener(ActionListener listener) { backButton.addActionListener(listener); }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
    
    // 회원유형 렌더러
    class UserTypeRenderer extends JLabel implements TableCellRenderer {
        public UserTypeRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            setForeground(Color.BLACK);
            
            if (value.toString().equals("관리자")) {
                setForeground(new Color(220, 53, 69));
                setFont(new Font("맑은 고딕", Font.BOLD, 13));
            } else {
                setForeground(new Color(40, 167, 69));
                setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            }
            
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
}
