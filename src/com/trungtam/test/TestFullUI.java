package com.trungtam.test;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import com.trungtam.model.TaiKhoan;
import com.trungtam.services.AuthService;
import com.trungtam.utils.RoleRouter;

public class TestFullUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContent;

    // === BẢNG MÀU CHUẨN PTIT ===
    private final Color BLUE_PRIMARY = new Color(52, 152, 219); 
    private final Color BLUE_LIGHT = new Color(233, 246, 255); 
    private final Color BORDER_COLOR = new Color(180, 215, 240); 
    private final Color TEXT_MAIN = new Color(51, 51, 51);
    private final Color TEXT_MUTED = new Color(119, 119, 119);
    private final Color RED_BADGE = new Color(228, 0, 43); 

    public TestFullUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Cổng thông tin đào tạo");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== HEADER TOP =====
        JPanel headerTop = new JPanel(new BorderLayout());
        headerTop.setBackground(BLUE_PRIMARY);
        headerTop.setPreferredSize(new Dimension(0, 45));
        add(headerTop, BorderLayout.NORTH);

        // ===== SIDEBAR (LEFT PANEL) =====
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        // --- Khu vực Form Đăng Nhập ---
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(new EmptyBorder(15, 20, 20, 20));

        // TÀI KHOẢN (Căn trái)
        JLabel lblUser = new JLabel("Tài khoản");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(TEXT_MUTED);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn lề trái

        JTextField txtUser = createTextField(""); 
        txtUser.setBackground(Color.WHITE); 
        txtUser.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn lề trái

        // MẬT KHẨU (Căn trái)
        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPass.setForeground(TEXT_MUTED);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn lề trái

        JPasswordField txtPass = new JPasswordField(""); 
        txtPass.setBackground(Color.WHITE);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
new EmptyBorder(5, 10, 5, 10)
        ));
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPass.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn lề trái

        // QUÊN MẬT KHẨU
        JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        forgotPanel.setBackground(Color.WHITE);
        forgotPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25)); // Đảm bảo chiếm trọn chiều ngang
        forgotPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Ép khung chứa căn trái
        
        JLabel lblForgot = new JLabel("Quên mật khẩu?");
        lblForgot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblForgot.setForeground(BLUE_PRIMARY);
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPanel.add(lblForgot);

        // NÚT ĐĂNG NHẬP
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(BLUE_PRIMARY);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(100, 35));
        
        btnLogin.addActionListener(e -> {
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());

            AuthService service = new AuthService();
            // Gọi xuống AuthService để query Database
            TaiKhoan tk = service.login(username, password);

            if (tk != null) {
                // Đăng nhập thành công
                this.dispose(); 
                // Gọi RoleRouter để mở giao diện
                RoleRouter.openUI(tk); 
            } else {
                // Đăng nhập thất bại
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!");
                txtPass.setText("");
                txtPass.requestFocus();
            }
        });
        
        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnWrapper.setBackground(Color.WHITE);
        btnWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Đảm bảo chiếm trọn chiều ngang
        btnWrapper.setAlignmentX(Component.LEFT_ALIGNMENT); // Ép khung chứa căn trái
        btnWrapper.add(btnLogin);

        // --- Thêm vào loginPanel theo thứ tự ---
        loginPanel.add(lblUser);
        loginPanel.add(Box.createVerticalStrut(5));
        loginPanel.add(txtUser);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(lblPass);
        loginPanel.add(Box.createVerticalStrut(5));
        loginPanel.add(txtPass);
        loginPanel.add(Box.createVerticalStrut(5));
        loginPanel.add(forgotPanel);
        loginPanel.add(Box.createVerticalStrut(15));
        loginPanel.add(btnWrapper);
loginPanel.add(Box.createVerticalStrut(20));

        // --- Menu Trang Chủ ---
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(Color.WHITE);
        
        JButton btnHome = new JButton("Trang chủ"); // Bỏ icon bị lỗi
        btnHome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHome.setForeground(BLUE_PRIMARY);
        btnHome.setBackground(BLUE_LIGHT); 
        btnHome.setHorizontalAlignment(SwingConstants.LEFT);
        btnHome.setFocusPainted(false);
        btnHome.setBorderPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.setMargin(new Insets(0, 15, 0, 0)); // Tạo khoảng cách thụt vào
        
        JPanel menuWrapper = new JPanel(new BorderLayout());
        menuWrapper.setBorder(new MatteBorder(0, 4, 0, 0, BLUE_PRIMARY));
        menuWrapper.add(btnHome, BorderLayout.CENTER);
        menuPanel.add(menuWrapper, BorderLayout.NORTH);

        // --- Footer Logo ---
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
        JLabel lblLogo = new JLabel("<html><center><b style='color:red;'>TRUNG TÂM ĐÀO TẠO NGHỀ</b><br><span style='font-size:9px; color:gray;'>Thiết kế bởi aqtech.vn</span></center></html>");
        footerPanel.add(lblLogo);

        JPanel sidebarTop = new JPanel(new BorderLayout());
        sidebarTop.add(loginPanel, BorderLayout.NORTH);
        sidebarTop.add(menuPanel, BorderLayout.CENTER);
        
        sidebar.add(sidebarTop, BorderLayout.CENTER);
        sidebar.add(footerPanel, BorderLayout.SOUTH);
        add(sidebar, BorderLayout.WEST);

        // ===== MAIN CONTENT =====
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.add(createHomePanel(), "HOME");
        add(mainContent, BorderLayout.CENTER);

        // ===== EVENT =====
        btnLogin.addActionListener(e -> {
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());
            AuthService service = new AuthService();
            TaiKhoan tk = service.login(username, password);

            if (tk != null) {
                this.dispose();
                RoleRouter.openUI(tk);
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!");
                txtPass.setText("");
                txtPass.requestFocus();
            }
        });
        txtPass.addActionListener(e -> btnLogin.doClick());
        btnHome.addActionListener(e -> cardLayout.show(mainContent, "HOME"));
    }

    // ===== GIAO DIỆN TRANG CHỦ =====
    private JPanel createHomePanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);

        JPanel panel = new JPanel();
panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. SECTION THÔNG BÁO 
        String[][] badges1 = {
            {"THÔNG BÁO", "14/04/2026", "V/v: Nghỉ lễ và điều chỉnh lịch giảng dạy - học tập dịp Giỗ tổ Hùng Vương, ngày Chiến thắ..."},
            {"THÔNG BÁO", "08/04/2026", "V/v đóng tiền thi lại hết môn học kỳ 1 năm học (2025-2026),..."}
        };
        String[][] links1 = {
            {"V/v đóng tiền mua tài khoản ED cho học kỳ II năm học 2025-2026 (đợt 2)", "11/03/2026"},
            {"THÔNG BÁO V/v thi kiểm tra Tiếng Anh chuẩn đầu ra cho sinh viên khóa 2022 và các khóa cũ...", "03/03/2026"},
            {"V/v: Thu học phí Học kỳ 2 năm học 2025-2026 tại Học viện cơ sở Tp.HCM đối với sinh viên Khóa 2024 trở về trước", "27/01/2026"}
        };
        panel.add(createNewsSection("Thông báo", badges1, links1)); // Bỏ icon
        panel.add(Box.createVerticalStrut(25)); 

        // 2. SECTION HỌC PHÍ 
        String[][] badges2 = {
            {"HƯỚNG DẪN", "12/01/2024", "[Mới] Thông báo V/v: Hướng dẫn nộp tiền học phí và các khoản thu khác của sinh viên q..."}
        };
        panel.add(createNewsSection("Học phí", badges2, new String[0][0])); // Bỏ icon

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    // ===== HÀM TIỆN ÍCH: TẠO KHUNG BẢN TIN LỚN =====
    private JPanel createNewsSection(String headerTitle, String[][] badges, String[][] links) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(new LineBorder(BORDER_COLOR, 1, true)); 

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel lblTitle = new JLabel(headerTitle);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(TEXT_MAIN);
        header.add(lblTitle, BorderLayout.WEST);

        JLabel lblMore = new JLabel("Xem tiếp >");
        lblMore.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMore.setForeground(BLUE_PRIMARY);
        lblMore.setCursor(new Cursor(Cursor.HAND_CURSOR));
        header.add(lblMore, BorderLayout.EAST);
        section.add(header, BorderLayout.NORTH);

        // Body chia 2 phần
        JPanel body = new JPanel(new BorderLayout(25, 0));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(20, 20, 20, 20));
// --- Cụm Badge Đỏ ---
        JPanel badgeContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        badgeContainer.setBackground(Color.WHITE);
        for(String[] b : badges) {
            badgeContainer.add(createBadgeItem(b[0], b[1], b[2]));
        }
        body.add(badgeContainer, BorderLayout.WEST);

        // --- Danh sách tin tức ---
        if (links.length > 0) {
            JPanel newsListPanel = new JPanel();
            newsListPanel.setLayout(new BoxLayout(newsListPanel, BoxLayout.Y_AXIS));
            newsListPanel.setBackground(Color.WHITE);
            newsListPanel.setBorder(new MatteBorder(0, 1, 0, 0, new Color(240, 240, 240))); 

            for (String[] item : links) {
                JPanel row = new JPanel(new BorderLayout(10, 0));
                row.setBackground(Color.WHITE);
                row.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
                    new EmptyBorder(10, 15, 10, 0)
                ));

                JLabel lblNewsTitle = new JLabel("<html><p style='width:350px;'>» <span style='color:#3498db'>" + item[0] + "</span></p></html>");
                lblNewsTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                
                JLabel lblNewsDate = new JLabel(item[1]);
                lblNewsDate.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblNewsDate.setForeground(TEXT_MUTED);

                row.add(lblNewsTitle, BorderLayout.CENTER);
                row.add(lblNewsDate, BorderLayout.EAST);
                
                newsListPanel.add(row);
            }
            body.add(newsListPanel, BorderLayout.CENTER);
        }

        section.add(body, BorderLayout.CENTER);
        return section;
    }

    // ===== HÀM TIỆN ÍCH: TẠO 1 KHỐI Ô ĐỎ + CHỮ TRÍCH DẪN =====
    private JPanel createBadgeItem(String badgeText, String dateText, String summaryText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(240, 160)); 

        // Ô viền đỏ
        JPanel redBoxWrapper = new JPanel(new BorderLayout());
        redBoxWrapper.setBackground(Color.WHITE);
        redBoxWrapper.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        JPanel redBox = new JPanel(new BorderLayout());
        redBox.setBackground(Color.WHITE);
        redBox.setPreferredSize(new Dimension(240, 100));
        redBox.setBorder(new LineBorder(RED_BADGE, 3, true)); 
        
        JLabel lblBadge = new JLabel(badgeText, SwingConstants.CENTER);
        lblBadge.setFont(new Font("Arial", Font.BOLD, 26));
        lblBadge.setForeground(RED_BADGE);
        redBox.add(lblBadge, BorderLayout.CENTER);
        
        redBoxWrapper.add(redBox, BorderLayout.CENTER);
        panel.add(redBoxWrapper, BorderLayout.NORTH);

        // Khối chữ bên dưới
JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(Color.WHITE);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 3));
        datePanel.setBackground(Color.WHITE);
        datePanel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(240, 240, 240)));
        JLabel lblDate = new JLabel(dateText);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDate.setForeground(TEXT_MUTED);
        datePanel.add(lblDate);
        textPanel.add(datePanel, BorderLayout.NORTH);

        JLabel lblSummary = new JLabel("<html><p style='width:230px; color:#555;'>" + summaryText + "</p></html>");
        lblSummary.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textPanel.add(lblSummary, BorderLayout.CENTER);

        panel.add(textPanel, BorderLayout.CENTER);
        return panel;
    }

    // ===== HÀM TIỆN ÍCH: TẠO TEXT FIELD =====
    private JTextField createTextField(String text) {
        JTextField txt = new JTextField(text);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 10, 5, 10) 
        ));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return txt;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TestFullUI().setVisible(true);
        });
    }
}