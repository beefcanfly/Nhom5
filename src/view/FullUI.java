package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.trungtam.model.TaiKhoan;
import com.trungtam.services.AuthService;
import com.trungtam.utils.RoleRouter;

import com.trungtam.model.SessionUser;
public class FullUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContent;
    private com.trungtam.dao.ThongBaoDAO thongBaoDAO = new com.trungtam.dao.ThongBaoDAO();

    // === BẢNG MÀU CHUẨN PTIT ===
    private final Color BLUE_PRIMARY = new Color(52, 152, 219); 
    private final Color BLUE_LIGHT = new Color(233, 246, 255); 
    private final Color BORDER_COLOR = new Color(180, 215, 240); 
    private final Color TEXT_MAIN = new Color(51, 51, 51);
    private final Color TEXT_MUTED = new Color(119, 119, 119);
    private final Color RED_BADGE = new Color(228, 0, 43); 

    public FullUI() {
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
        
        lblForgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(FullUI.this, "Vui lòng liên hệ Quản trị viên (Admin) để cấp lại mật khẩu!");
            }
        });
        forgotPanel.add(lblForgot);

        // NÚT ĐĂNG NHẬP
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(BLUE_PRIMARY);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(160, 35));
        
        btnLogin.addActionListener(e -> {
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());

            AuthService service = new AuthService();
            SessionUser session = service.login(username, password);

            if (session != null) {
                this.dispose();
                RoleRouter.openUI(session);
            } else {
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
        
        JButton btnHome = new JButton("Trang chủ");
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
        JPanel footerPanel = new JPanel(new BorderLayout()); // Dùng BorderLayout để dễ căn giữa
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(15, 10, 25, 10)); // Tăng lề dưới cho thoáng dế

        // Chuỗi HTML chuẩn: Định dạng chữ to rực rỡ, tự động bẻ dòng nếu quá ngang
        String htmlText = "<html><center>"
                        + "<b style='font-size:13px; color:red; font-family:Segoe UI;'>TRUNG TÂM ĐÀO TẠO NGHỀ</b><br>"
                        + "<b style='font-size:13px; color:red; font-family:Segoe UI;'>MIỀN NAM</b>"
                        + "</center></html>";

        JLabel lblLogo = new JLabel(htmlText, SwingConstants.CENTER); // Ép nhãn lùi về chính giữa ô
        footerPanel.add(lblLogo, BorderLayout.CENTER);

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
    }

    // ===== GIAO DIỆN TRANG CHỦ =====
 // ===== GIAO DIỆN TRANG CHỦ DỮ LIỆU ĐỘNG (BỎ HOÀN TOÀN NGÀY THÁNG) =====
    private JPanel createHomePanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Đọc dữ liệu từ DAO của bạn
        java.util.List<com.trungtam.model.ThongBao> allNotices = thongBaoDAO.getThongBaoByRole(0);

        java.util.List<String[]> thongBaoBadges = new java.util.ArrayList<>();
        java.util.List<String> thongBaoLinks = new java.util.ArrayList<>(); // Đổi thành danh sách chuỗi đơn
        java.util.List<String[]> hocPhiBadges = new java.util.ArrayList<>();

        if (allNotices != null) {
            for (com.trungtam.model.ThongBao tb : allNotices) {
                String titleUpper = tb.getTieuDe().toUpperCase();
                String summary = tb.getNoiDung();
                
                // Phân loại nhóm Học phí
                if (titleUpper.contains("HỌC PHÍ") || titleUpper.contains("NỘP TIỀN")) {
                    if (hocPhiBadges.size() < 1) {
                        // Mảng chỉ gồm 2 phần tử: [0]=Nhãn, [1]=Nội dung gộp
                        hocPhiBadges.add(new String[]{"HƯỚNG DẪN", "<b>" + tb.getTieuDe() + "</b><br>" + summary});
                    }
                } else {
                    // Phân loại nhóm Thông báo
                    if (thongBaoBadges.size() < 2) {
                        // Mảng chỉ gồm 2 phần tử: [0]=Nhãn, [1]=Nội dung gộp
                        thongBaoBadges.add(new String[]{"THÔNG BÁO", "<b>" + tb.getTieuDe() + "</b><br>" + summary});
                    } else if (thongBaoLinks.size() < 3) {
                        // Dòng chữ nhỏ bên phải chỉ cần lưu tiêu đề + nội dung ngắn
                        thongBaoLinks.add(tb.getTieuDe() + " - " + summary);
                    }
                }
            }
        }

        // Bẫy dữ liệu dự phòng nếu DB trống
        if (thongBaoBadges.isEmpty()) {
            thongBaoBadges.add(new String[]{"THÔNG BÁO", "Hiện tại chưa có thông báo mới nào từ ban quản trị."});
        }
        if (hocPhiBadges.isEmpty()) {
            hocPhiBadges.add(new String[]{"HƯỚNG DẪN", "Chưa có hướng dẫn nộp học phí mới."});
        }

        String[][] badges1 = thongBaoBadges.toArray(new String[0][0]);
        String[] links1 = thongBaoLinks.toArray(new String[0]); // Mảng 1 chiều chứa text link
        String[][] badges2 = hocPhiBadges.toArray(new String[0][0]);

        // Đổ dữ liệu lên UI
        panel.add(createNewsSection("Thông báo", badges1, links1)); 
        panel.add(Box.createVerticalStrut(25)); 

        panel.add(createNewsSection("Học phí", badges2, new String[0])); 

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    // ===== HÀM TIỆN ÍCH: TẠO KHUNG BẢN TIN LỚN =====
 // ===== HÀM TIỆN ÍCH: TẠO KHUNG BẢN TIN (ĐÃ XÓA NGÀY) =====
    private JPanel createNewsSection(String headerTitle, String[][] badges, String[] links) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(new LineBorder(BORDER_COLOR, 1, true)); 

        // Header Section
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

        // --- Cụm Badge Đỏ bên trái ---
        JPanel badgeContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        badgeContainer.setBackground(Color.WHITE);
        for(String[] b : badges) {
            badgeContainer.add(createBadgeItem(b[0], b[1])); // Chỉ truyền 2 tham số: Nhãn và Nội dung
        }
        body.add(badgeContainer, BorderLayout.WEST);

        // --- Danh sách tin tức dòng nhỏ bên phải (Xóa hẳn cột ngày) ---
        if (links.length > 0) {
            JPanel newsListPanel = new JPanel();
            newsListPanel.setLayout(new BoxLayout(newsListPanel, BoxLayout.Y_AXIS));
            newsListPanel.setBackground(Color.WHITE);
            newsListPanel.setBorder(new MatteBorder(0, 1, 0, 0, new Color(240, 240, 240))); 

            for (String textItem : links) {
                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(Color.WHITE);
                row.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
                    new EmptyBorder(10, 15, 10, 15)
                ));

                // Chiều rộng text link co giãn thoải mái trong ô
                JLabel lblNewsTitle = new JLabel("<html><p style='width:380px;'>» <span style='color:#3498db'>" + textItem + "</span></p></html>");
                lblNewsTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                
                row.add(lblNewsTitle, BorderLayout.CENTER);
                newsListPanel.add(row);
            }
            body.add(newsListPanel, BorderLayout.CENTER);
        }

        section.add(body, BorderLayout.CENTER);
        return section;
    }
    // ===== HÀM TIỆN ÍCH: TẠO 1 KHỐI Ô ĐỎ + CHỮ TRÍCH DẪN =====
 // ===== HÀM TIỆN ÍCH: TẠO 1 KHỐI Ô ĐỎ + CHỮ TRÍCH DẪN (ĐÃ XÓA NGÀY) =====
    private JPanel createBadgeItem(String badgeText, String summaryText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(240, 160)); 

        // Ô viền đỏ trên cùng
        JPanel redBoxWrapper = new JPanel(new BorderLayout());
        redBoxWrapper.setBackground(Color.WHITE);
        redBoxWrapper.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        JPanel redBox = new JPanel(new BorderLayout());
        redBox.setBackground(Color.WHITE);
        redBox.setPreferredSize(new Dimension(240, 95));
        redBox.setBorder(new LineBorder(RED_BADGE, 3, true)); 
        
        JLabel lblBadge = new JLabel(badgeText, SwingConstants.CENTER);
        lblBadge.setFont(new Font("Arial", Font.BOLD, 26));
        lblBadge.setForeground(RED_BADGE);
        redBox.add(lblBadge, BorderLayout.CENTER);
        
        redBoxWrapper.add(redBox, BorderLayout.CENTER);
        panel.add(redBoxWrapper, BorderLayout.NORTH);

        // Khối chữ nội dung bên dưới (Không còn thanh kẻ ngang gạch ngày)
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(Color.WHITE);

        JLabel lblSummary = new JLabel("<html><div style='width:215px; text-align:left;'>" + summaryText + "</div></html>");
        lblSummary.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSummary.setForeground(new Color(85, 85, 85));
        textPanel.add(lblSummary, BorderLayout.NORTH); // Đưa lên vùng NORTH hiển thị trực tiếp sát hộp đỏ

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
            new FullUI().setVisible(true);
        });
    }
}