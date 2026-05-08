package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class AdminUI extends JFrame {

    // ===== HỆ MÀU XANH CYAN =====
    private final Color COLOR_CYAN_MAIN = new Color(52, 170, 220); 
    private final Color COLOR_SIDEBAR_BG = new Color(255, 255, 255);
    private final Color COLOR_BG_LIGHT = new Color(245, 247, 250);   
    private final Color COLOR_TEXT_BLUE = new Color(52, 170, 220);
    private final Color COLOR_LOGOUT = new Color(220, 53, 69); // Màu đỏ cảnh báo

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public AdminUI() {
        setTitle("HỆ THỐNG QUẢN LÝ ĐÀO TẠO");
        setSize(1250, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header & Sidebar
        add(createTopHeader(), BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);

        // Nội dung chính sử dụng CardLayout để chuyển trang
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(COLOR_BG_LIGHT);

        // Thêm các trang quản lý
        cardPanel.add(createDashboardPage(), "Dashboard");
        cardPanel.add(createManagementPage("Quản Lý Học Viên", new String[]{"Mã HV", "Họ Tên", "Lớp Học", "SĐT"}), "HocVien");
        cardPanel.add(createManagementPage("Quản Lý Giảng Viên", new String[]{"Mã GV", "Họ Tên", "Chuyên Môn", "Bằng Cấp"}), "GiangVien");
        cardPanel.add(createManagementPage("Quản Lý Khóa Học", new String[]{"Mã KH", "Tên Khóa", "Thời Lượng", "Học Phí"}), "KhoaHoc");
        cardPanel.add(createManagementPage("Quản Lý Lớp Học", new String[]{"Mã Lớp", "Tên Lớp", "Sĩ Số", "Phòng Học"}), "LopHoc");
        cardPanel.add(createManagementPage("Quản Lý Học Phí", new String[]{"Mã HV", "Họ Tên", "Đã Đóng", "Còn Nợ"}), "HocPhi");
        cardPanel.add(createManagementPage("Thống Kê Báo Cáo", new String[]{"Chỉ Số", "Giá Trị", "Ghi Chú"}), "ThongKe");

        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createTopHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_CYAN_MAIN);
        header.setPreferredSize(new Dimension(0, 50));
        JLabel lblTitle = new JLabel("  HỆ THỐNG QUẢN TRỊ VIÊN");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.add(lblTitle, BorderLayout.WEST);
        return header;
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(260, 0));
        panel.setBackground(COLOR_SIDEBAR_BG);
        panel.setBorder(new MatteBorder(0, 0, 0, 1, new Color(220, 230, 240)));

        // Nhóm các nút điều hướng
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        menuPanel.setOpaque(false);
        menuPanel.add(createMenuButton("Dashboard", "Dashboard"));
        menuPanel.add(createMenuButton("Quản lý học viên", "HocVien"));
        menuPanel.add(createMenuButton("Quản lý giảng viên", "GiangVien"));
        menuPanel.add(createMenuButton("Quản lý khóa học", "KhoaHoc"));
        menuPanel.add(createMenuButton("Quản lý lớp học", "LopHoc"));
        menuPanel.add(createMenuButton("Quản lý học phí", "HocPhi"));
        menuPanel.add(createMenuButton("Thống kê", "ThongKe"));

        panel.add(menuPanel, BorderLayout.CENTER);

        // NÚT ĐĂNG XUẤT - ĐÃ ĐƯỢC CHỈNH SỬA LOGIC QUAY VỀ LOGIN
        JButton btnLogout = new JButton("  [→  Đăng xuất");
        btnLogout.setPreferredSize(new Dimension(260, 55));
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogout.setForeground(COLOR_LOGOUT);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Xử lý sự kiện Logout
        btnLogout.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn đăng xuất và quay lại màn hình đăng nhập?", 
                    "Xác nhận thoát", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            
            if (result == JOptionPane.YES_OPTION) {
                // 1. Đóng cửa sổ Admin hiện tại
                this.dispose(); 
                
                // 2. Mở lại màn hình Login (đảm bảo file LoginUI.java của bạn tồn tại)
                SwingUtilities.invokeLater(() -> {
                    try {
                        // Khởi tạo lại màn hình Login của bạn
                        new FullUI().setVisible(true); 
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Không thể quay lại màn hình Login: " + ex.getMessage());
                    }
                });
            }
        });
        
        // Hiệu ứng hover cho nút Đăng xuất
        btnLogout.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnLogout.setBackground(new Color(255, 240, 240)); }
            public void mouseExited(MouseEvent e) { btnLogout.setBackground(Color.WHITE); }
        });

        panel.add(btnLogout, BorderLayout.SOUTH);
        return panel;
    }

    private JButton createMenuButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(250, 45));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(70, 70, 70));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 20, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(e -> cardLayout.show(cardPanel, cardName));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                btn.setBackground(new Color(240, 250, 255)); 
                btn.setForeground(COLOR_CYAN_MAIN);
            }
            public void mouseExited(MouseEvent e) { 
                btn.setBackground(Color.WHITE); 
                btn.setForeground(new Color(70, 70, 70));
            }
        });
        return btn;
    }

    private JPanel createManagementPage(String title, String[] columns) {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_BG_LIGHT);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("▣ " + title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(COLOR_CYAN_MAIN);
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new LineBorder(new Color(210, 225, 240)));

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionBar.setBackground(Color.WHITE);
        actionBar.add(createActionButton("THÊM MỚI"));
        actionBar.add(createActionButton("SỬA"));
        actionBar.add(createActionButton("XÓA DỮ LIỆU"));

        DefaultTableModel model = new DefaultTableModel(new Object[][]{
            {"001", "Dữ liệu mẫu A", "...", "..."}, 
            {"002", "Dữ liệu mẫu B", "...", "..."}
        }, columns);
        
        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(240, 248, 255));
        table.getTableHeader().setForeground(COLOR_CYAN_MAIN);

        content.add(actionBar, BorderLayout.NORTH);
        content.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE); 
        btn.setForeground(COLOR_TEXT_BLUE); 
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_CYAN_MAIN, 1),
            new EmptyBorder(8, 15, 8, 15)
        ));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(235, 245, 255)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(Color.WHITE); }
        });
        return btn;
    }

    private JPanel createDashboardPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(COLOR_CYAN_MAIN);
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new AdminUI().setVisible(true));
    }
}