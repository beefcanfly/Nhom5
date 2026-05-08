package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class GiangVienUI extends JFrame {

    // Bảng màu chuẩn theo giao diện web PTIT
    private final Color BLUE_PRIMARY = new Color(33, 150, 243);
    private final Color TEXT_MAIN = new Color(51, 51, 51);
    private final Color TEXT_LABEL = new Color(96, 103, 112); // Màu xám nhạt cho nhãn
    private final Color BORDER_COLOR = new Color(220, 224, 228); // Màu đường kẻ dọc

    public GiangVienUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Cổng thông tin giảng viên");
        setSize(1280, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. HEADER
        add(createHeader(), BorderLayout.NORTH);

        // 2. SIDEBAR
        add(createSidebar(), BorderLayout.WEST);

        // 3. MAIN CONTENT
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Tiêu đề & Ngày tháng ---
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 8));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblWelcome = new JLabel("👋 Chào mừng giảng viên, Nguyễn Văn A");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(BLUE_PRIMARY);
        
        JLabel lblDate = new JLabel("📅 Thứ 5, 07 Tháng 05");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblDate.setForeground(TEXT_LABEL);
        
        titlePanel.add(lblWelcome);
        titlePanel.add(lblDate);
        mainContent.add(titlePanel);
        mainContent.add(Box.createVerticalStrut(25));

        // --- CARD 1: THÔNG TIN GIẢNG VIÊN ---
        mainContent.add(createTeacherInfoCard());
        mainContent.add(Box.createVerticalStrut(25)); // Khoảng cách giữa 2 Card

        // --- CARD 2: DANH SÁCH LỚP GIẢNG DẠY ---
        mainContent.add(createClassListCard());

        // Bọc trong ScrollPane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ================= CÁC HÀM TẠO THÀNH PHẦN GIAO DIỆN =================

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BLUE_PRIMARY);
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel logo = new JLabel("🌐 Cổng thông tin đào tạo"); 
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(logo, BorderLayout.WEST);
        
     // Khung chứa thông tin User và nút Đăng xuất (Căn phải)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        userPanel.setOpaque(false);
        JLabel userInfo = new JLabel("Nguyễn Văn A - GV001");
        userInfo.setForeground(Color.WHITE);
        userInfo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        userPanel.add(userInfo);
        
     // --- 2. NÚT ĐĂNG XUẤT ---
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setForeground(BLUE_PRIMARY);
        btnLogout.setBackground(Color.WHITE); // Nút màu trắng để nổi bật trên nền xanh
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Xử lý sự kiện click Đăng xuất
        btnLogout.addActionListener(e -> {
            // Hiển thị hộp thoại hỏi đáp
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn đăng xuất khỏi hệ thống?", 
                    "Xác nhận đăng xuất", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose(); // 1. Đóng màn hình hiện tại (Dashboard)
                new FullUI().setVisible(true); // 2. Mở lại màn hình Đăng nhập (FullUI)
            }
        });
        
        userPanel.add(btnLogout);
        header.add(userPanel, BorderLayout.EAST);
        
        return header;
    }

    private JScrollPane createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        
        String[] menuItems = {
            "Trang chủ", "Thông báo", "Khóa học", "Thời khóa biểu",
            "Lịch sử dạy học", "Báo cáo đánh giá", "Nhập điểm", "Thống kê"
        };

        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            btn.setForeground(BLUE_PRIMARY);
            btn.setBackground(Color.WHITE);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMaximumSize(new Dimension(280, 45));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setMargin(new Insets(0, 20, 0, 0));
            
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(Color.WHITE);
            wrapper.setBorder(new MatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
            wrapper.add(btn, BorderLayout.CENTER);
            wrapper.setMaximumSize(new Dimension(280, 50));
            
            sidebar.add(wrapper);
        }

        JScrollPane scrollSidebar = new JScrollPane(sidebar);
        scrollSidebar.setPreferredSize(new Dimension(260, 0));
        scrollSidebar.setBorder(new MatteBorder(0, 0, 0, 1, BORDER_COLOR)); // Kẻ dọc phải của menu
        scrollSidebar.getVerticalScrollBar().setUnitIncrement(16);
        return scrollSidebar;
    }

    private JPanel createTeacherInfoCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(BLUE_PRIMARY, 1, true)); // Viền xanh toàn Card
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Header Card
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(12, 15, 12, 15)
        ));
        JLabel lblTitle = new JLabel("👤 Thông tin giảng viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_MAIN);
        header.add(lblTitle, BorderLayout.WEST);
        card.add(header, BorderLayout.NORTH);

        // --- BODY CHỨA 3 CỘT (CÓ ĐƯỜNG KẺ DỌC) ---
        JPanel body = new JPanel(new GridLayout(1, 3, 0, 0)); 
        body.setBackground(Color.WHITE);

        // Dữ liệu giả lập
        String[][] data1 = {
            {"Mã GV:", "GV001"},
            {"Tên giảng viên:", "Nguyễn Văn A"},
            {"Học vị:", "Thạc sĩ"}
        };
        String[][] data2 = {
            {"Chuyên môn:", "Điện tử số"},
            {"Trạng thái:", "Đang công tác"}
        };
        String[][] data3 = {
            {"Số điện thoại:", "0909123456"},
            {"Email:", "nguyenvana@ptithcm.edu.vn"},
            {"Văn phòng:", "Tòa A1, Cơ sở Quận 9"}
        };

        // Cột 1 (Có ảnh + kẻ dọc phải)
        JPanel col1Wrapper = new JPanel(new BorderLayout(15, 0));
        col1Wrapper.setBackground(Color.WHITE);
        col1Wrapper.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 0, 1, BORDER_COLOR), 
                new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel avatar = new JLabel("Ảnh", SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(110, 140));
        avatar.setOpaque(true);
        avatar.setBackground(new Color(230, 235, 240));
        
        col1Wrapper.add(avatar, BorderLayout.WEST);
        col1Wrapper.add(createDataGrid(data1), BorderLayout.CENTER);

        // Cột 2 (Kẻ dọc phải)
        JPanel col2Wrapper = new JPanel(new BorderLayout());
        col2Wrapper.setBackground(Color.WHITE);
        col2Wrapper.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 0, 1, BORDER_COLOR),
                new EmptyBorder(20, 20, 20, 20)
        ));
        col2Wrapper.add(createDataGrid(data2), BorderLayout.CENTER); // Căn giữa dọc

        // Cột 3 (KHÔNG có kẻ dọc)
        JPanel col3Wrapper = new JPanel(new BorderLayout());
        col3Wrapper.setBackground(Color.WHITE);
        col3Wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));
        col3Wrapper.add(createDataGrid(data3), BorderLayout.CENTER);

        body.add(col1Wrapper);
        body.add(col2Wrapper);
        body.add(col3Wrapper);

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel createClassListCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(BLUE_PRIMARY, 1, true));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Header Card
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(12, 15, 12, 15)
        ));
        JLabel lblTitle = new JLabel("📚 Danh sách lớp đang giảng dạy");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_MAIN);
        header.add(lblTitle, BorderLayout.WEST);
        card.add(header, BorderLayout.NORTH);

        // Body chứa Bảng
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columns = {"Mã lớp", "Tên môn", "Số SV"};
        Object[][] data = {
                {"INT101", "Mạng máy tính", 45},
                {"INT202", "Lập trình Java", 50},
                {"INT303", "An toàn thông tin", 40},
                {"INT404", "Hệ thống nhúng", 35},
                {"INT505", "Internet of Things", 42}
        };

        JTable table = new JTable(data, columns);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(0, 230));

        body.add(scroll, BorderLayout.CENTER);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    // --- HÀM TẠO LƯỚI THÔNG TIN VỚI KHOẢNG CÁCH DÒNG (VGAP) CHUẨN ---
    private JPanel createDataGrid(String[][] data) {
        JPanel panel = new JPanel(new GridLayout(data.length, 2, 5, 25)); // vgap = 25px
        panel.setBackground(Color.WHITE);
        
        for (String[] row : data) {
            JLabel lbl = new JLabel(row[0]);
            lbl.setForeground(TEXT_LABEL); 
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            // Xử lý text dài rớt dòng
            String valText = "<html><body style='width: 140px'>" + row[1] + "</body></html>";
            JLabel val = new JLabel(valText);
            val.setForeground(TEXT_MAIN); 
            val.setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            panel.add(lbl);
            panel.add(val);
        }
        return panel;
    }

    // --- HÀM TÙY CHỈNH BẢNG (JTABLE) ---
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(40); 
        table.setSelectionBackground(new Color(230, 247, 255));
        table.setSelectionForeground(TEXT_MAIN);
        table.setShowVerticalLines(false);
        table.setGridColor(BORDER_COLOR);
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(TEXT_MAIN);
        header.setPreferredSize(new Dimension(0, 45)); 
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GiangVienUI().setVisible(true);
        });
    }
}