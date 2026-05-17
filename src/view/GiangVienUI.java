package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import com.trungtam.dao.ThongBaoDAO;
import com.trungtam.model.GiangVien;
import com.trungtam.model.NguoiDung;
import com.trungtam.model.SessionUser;
import com.trungtam.model.TaiKhoan;
import com.trungtam.utils.SessionManager;

public class GiangVienUI extends JFrame {

    private TaiKhoan taiKhoan;
    private NguoiDung nguoiDung;
    private GiangVien giangVien;
    private ThongBaoDAO tbDAO = new ThongBaoDAO();

    private final Color BLUE_PRIMARY = new Color(33, 150, 243);
    private final Color TEXT_MAIN = new Color(51, 51, 51);
    private final Color TEXT_LABEL = new Color(96, 103, 112);
    private final Color BORDER_COLOR = new Color(220, 224, 228);

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public GiangVienUI() {
        SessionUser session = SessionManager.getSession();
        if (session != null) {
            this.taiKhoan = session.getTaiKhoan();
            this.nguoiDung = session.getNguoiDung();
            this.giangVien = session.getGiangVien();
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        setTitle("Cổng thông tin giảng viên");
        setSize(1280, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Đăng ký các trang
        cardPanel.add(createTrangChuPanel(), "Trang chủ");    
        cardPanel.add(createThongBaoPage(), "Thông báo");
        cardPanel.add(createChuongTrinhGiangDayPage(), "Xem chương trình giảng dạy");
        cardPanel.add(createTaiLieuDayHocPage(), "Tài liệu dạy học");
        cardPanel.add(createThoiKhoaBieuPage(), "Thời khóa biểu");

        add(cardPanel, BorderLayout.CENTER);
    }

    // ================= TRANG CHỦ  =================
    private JScrollPane createTrangChuPanel() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(new EmptyBorder(10, 10, 10, 10));

        String hoTen = (nguoiDung != null) ? nguoiDung.getHoTen() : "Unknown";

        // Tiêu đề chào mừng
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lblWelcome = new JLabel(" Chào mừng giảng viên, " + hoTen);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(BLUE_PRIMARY);

        JLabel lblDate = new JLabel("📅 Hôm nay: " + java.time.LocalDate.now());
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDate.setForeground(TEXT_LABEL);

        titlePanel.add(lblWelcome);
        titlePanel.add(lblDate);
        mainContent.add(titlePanel);
        mainContent.add(Box.createVerticalStrut(12));

        // Card thông tin giảng viên
        JPanel infoCard = createTeacherInfoCard();
        infoCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(infoCard);
        
        mainContent.add(Box.createVerticalStrut(12));
        
        JScrollPane scroll = new JScrollPane(mainContent);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createTeacherInfoCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(BLUE_PRIMARY, 1, true));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Header của Card
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(8, 12, 8, 12)));
        JLabel lblTitle = new JLabel(" Thông tin giảng viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.add(lblTitle, BorderLayout.WEST);
        card.add(header, BorderLayout.NORTH);

        // Body chia làm 2 cột
        JPanel body = new JPanel(new GridLayout(1, 2, 0, 0));
        body.setBackground(Color.WHITE);

        // Dữ liệu
        String maGV = (taiKhoan != null) ? taiKhoan.getUsername() : "";
        String hoTen = (nguoiDung != null) ? nguoiDung.getHoTen() : "";
        String hocVi = (giangVien != null) ? giangVien.getHocVi() : "";
        String chuyenMon = (giangVien != null) ? giangVien.getChuyenMon() : "";

        // --- COL 1 ---
        String[][] data1 = {
                {"Mã GV:", maGV},
                {"Họ và tên:", hoTen},
                {"Ngày sinh:", (nguoiDung != null) ? nguoiDung.getNgaySinh() : ""},
                {"Giới tính:", (nguoiDung != null) ? nguoiDung.getGioiTinh() : ""},
                {"Số điện thoại:", (nguoiDung != null) ? nguoiDung.getSoDienThoai() : ""}
        };
        JPanel col1Wrapper = new JPanel(new BorderLayout(8, 0));
        col1Wrapper.setBackground(Color.WHITE);
        col1Wrapper.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 0, 1, BORDER_COLOR), new EmptyBorder(8, 8, 8, 8)));
        
        JLabel avatar = new JLabel("Ảnh", SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(80, 100));
        avatar.setOpaque(true);
        avatar.setBackground(new Color(230, 235, 240));
        col1Wrapper.add(avatar, BorderLayout.WEST);
        col1Wrapper.add(createDataGrid(data1), BorderLayout.CENTER);

        // --- COL 2 ---
        String[][] data2 = {
                {"Email:", (nguoiDung != null) ? nguoiDung.getEmail() : ""},
                {"Học vị:", hocVi},
                {"Chuyên môn:", chuyenMon},
                {"Quê quán:", (nguoiDung != null) ? nguoiDung.getQueQuan() : ""},
                {"Trạng thái:", "Đang công tác"}
        };
        JPanel col2Wrapper = new JPanel(new BorderLayout());
        col2Wrapper.setBackground(Color.WHITE);
        col2Wrapper.setBorder(new EmptyBorder(8, 8, 8, 8));
        col2Wrapper.add(createDataGrid(data2), BorderLayout.CENTER);

        body.add(col1Wrapper);
        body.add(col2Wrapper);
        card.add(body, BorderLayout.CENTER);

        return card;
    }
// TẠO TRANG THÔNG BÁO//
    private JPanel createThongBaoPage() {
    JPanel panel = new JPanel(new BorderLayout(20, 20));
    panel.setBackground(Color.WHITE);
    panel.setBorder(new EmptyBorder(30, 30, 30, 30)); // Tăng lề cho thoáng

    // --- TIÊU ĐỀ NỔI BẬT ---
    JLabel lbl = new JLabel("🔔 THÔNG BÁO HỆ THỐNG");
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Chữ tiêu đề rất to
    lbl.setForeground(BLUE_PRIMARY);
    lbl.setBorder(new MatteBorder(0, 0, 2, 0, BLUE_PRIMARY)); // Thêm đường gạch chân tiêu đề
    panel.add(lbl, BorderLayout.NORTH);

    // --- CẤU HÌNH BẢNG ---
    String[] cols = {"Mã số", "Tiêu đề thông báo", "Nội dung chi tiết"};
    DefaultTableModel model = new DefaultTableModel(cols, 0);
    
    // Sử dụng hàm createStyledTable để khóa chỉnh sửa và tăng cỡ chữ
    JTable table = createStyledTable(model);
    
    // Tùy chỉnh riêng độ rộng cột cho trang thông báo
    table.getColumnModel().getColumn(0).setPreferredWidth(100);  // Mã số nhỏ
    table.getColumnModel().getColumn(1).setPreferredWidth(300);  // Tiêu đề vừa
    table.getColumnModel().getColumn(2).setPreferredWidth(600);  // Nội dung rộng nhất

    // --- NẠP DỮ LIỆU ---
    List<com.trungtam.model.ThongBao> list = tbDAO.getThongBaoByRole(2); 
    if (list != null) {
        for (com.trungtam.model.ThongBao tb : list) {
            model.addRow(new Object[]{
                "TB-" + tb.getMaThongBao(), 
                tb.getTieuDe(), 
                tb.getNoiDung()
            });
        }
    }
   
    // Đưa bảng vào ScrollPane với viền bo tròn nhẹ hoặc không viền
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(new LineBorder(new Color(240, 240, 240), 1));
    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
}
    
// TẠO TRANG CHƯƠNG TRÌNH GIẢNG DẠY //
    private JPanel createChuongTrinhGiangDayPage() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. Tiêu đề
        JLabel lblTitle = new JLabel(" CHƯƠNG TRÌNH GIẢNG DẠY");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(BLUE_PRIMARY);
        lblTitle.setBorder(new MatteBorder(0, 0, 3, 0, BLUE_PRIMARY));
        panel.add(lblTitle, BorderLayout.NORTH);

        // 2. Khu vực chọn khóa học (ComboBox)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setOpaque(false);
        JLabel lblSelect = new JLabel("Khóa học đang phụ trách:");
        lblSelect.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        JComboBox<String> cbKhoaHoc = new JComboBox<>();
        cbKhoaHoc.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cbKhoaHoc.setPreferredSize(new Dimension(450, 45));

        // 3. Bảng hiển thị (Sử dụng StyledTable đã có)
        String[] cols = {"STT", "Tên bài giảng", "Nội dung tóm tắt"};
        DefaultTableModel modelLotrinh = new DefaultTableModel(cols, 0);
        JTable table = createStyledTable(modelLotrinh); 
        
        // Tùy chỉnh độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(600);

        // 4. Logic nạp dữ liệu vào ComboBox từ bảng lophoc
        if (giangVien != null) {
            String sql = "SELECT DISTINCT k.maKhoaHoc, k.tenKhoaHoc " +
                         "FROM lophoc l JOIN khoahoc k ON l.maKhoaHoc = k.maKhoaHoc " +
                         "WHERE l.maGV = ?";
            try (java.sql.Connection conn = com.trungtam.utils.DBConnection.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, giangVien.getMaGV());
                java.sql.ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    cbKhoaHoc.addItem(rs.getString("maKhoaHoc") + " - " + rs.getString("tenKhoaHoc"));
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        // 5. Sự kiện khi chọn khóa học
        cbKhoaHoc.addActionListener(e -> {
            String selected = (String) cbKhoaHoc.getSelectedItem();
            if (selected != null && selected.contains(" - ")) {
                loadDataLotrinh(modelLotrinh, selected.split(" - ")[0]);
            }
        });

        // Load mặc định môn đầu tiên
        if (cbKhoaHoc.getItemCount() > 0) {
            cbKhoaHoc.setSelectedIndex(0);
            loadDataLotrinh(modelLotrinh, cbKhoaHoc.getItemAt(0).split(" - ")[0]);
        }

        topPanel.add(lblSelect);
        topPanel.add(cbKhoaHoc);
        
        JPanel mainContent = new JPanel(new BorderLayout(0, 20));
        mainContent.setOpaque(false);
        mainContent.add(topPanel, BorderLayout.NORTH);
        mainContent.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(mainContent, BorderLayout.CENTER);

        return panel;
    }  
  //=== TẠO TRANG TÀI LIỆU DẠY HỌC ===//
    private JPanel createTaiLieuDayHocPage() {
        JPanel panel = new JPanel(new BorderLayout(25, 25));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- TIÊU ĐỀ ---
        JLabel lblTitle = new JLabel(" KHO TÀI LIỆU TỔNG HỢP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(BLUE_PRIMARY);
        lblTitle.setBorder(new MatteBorder(0, 0, 3, 0, BLUE_PRIMARY));
        panel.add(lblTitle, BorderLayout.NORTH);

        // --- BẢNG HIỂN THỊ TÀI LIỆU ---
        // Thêm cột "Đối tượng sử dụng" để phân biệt tài liệu HV và GV
        String[] cols = {"STT", "Môn học", "Tên tài liệu", "Loại", "Đối tượng sử dụng"};
        DefaultTableModel modelTailieu = new DefaultTableModel(cols, 0);
        JTable table = createStyledTable(modelTailieu); 

        // Chỉnh độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(400);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);

        // Nạp dữ liệu tự động cho tất cả các môn giảng viên phụ trách
        loadDataTailieuToTable(modelTailieu);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadDataTailieuToTable(DefaultTableModel model) {
        if (giangVien == null) return;
        model.setRowCount(0); 
        
        // SQL: Lấy tài liệu của các môn GV dạy, bao gồm cả doituong 1 (Học viên) và 2 (Giảng viên)
        String sql = "SELECT DISTINCT k.tenKhoaHoc, t.tentailieu, t.loai, t.doituong " +
                     "FROM tailieu t " +
                     "JOIN lophoc l ON t.maKhoaHoc = l.maKhoaHoc " +
                     "JOIN khoahoc k ON l.maKhoaHoc = k.maKhoaHoc " +
                     "WHERE l.maGV = ? AND t.doituong IN (1, 2) " +
                     "ORDER BY k.tenKhoaHoc, t.doituong ASC";
        
        try (java.sql.Connection conn = com.trungtam.utils.DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, giangVien.getMaGV());
            java.sql.ResultSet rs = ps.executeQuery();
            
            int stt = 1;
            while (rs.next()) {
                // Chuyển đổi mã đối tượng (1, 2) sang tên hiển thị
                int dtCode = rs.getInt("doituong");
                String doiTuongText = (dtCode == 1) ? "👤 Học viên" : "🔑 Giảng viên";
                
                model.addRow(new Object[]{
                    stt++, 
                    rs.getString("tenKhoaHoc"),
                    rs.getString("tentailieu"), 
                    rs.getString("loai"),
                    doiTuongText
                });
            }
            
            if (stt == 1) {
                 model.addRow(new Object[]{"-", "Không tìm thấy tài liệu nào", "-", "-", "-"});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
// TẠO TRANG THỜI KHÓA BIỂU //
  //=== TẠO TRANG THỜI KHÓA BIỂU GIẢNG VIÊN ===
    private JPanel createThoiKhoaBieuPage() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- TIÊU ĐỀ ---
        JLabel lblTitle = new JLabel(" LỊCH GIẢNG DẠY TỔNG QUAN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(BLUE_PRIMARY);
        lblTitle.setBorder(new MatteBorder(0, 0, 3, 0, BLUE_PRIMARY));
        panel.add(lblTitle, BorderLayout.NORTH);

        // --- CẤU TRÚC BẢNG (Thứ 2 -> Chủ Nhật) ---
        String[] cols = {"Buổi", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật"};
        Object[][] data = {
            {"SÁNG", "", "", "", "", "", "", ""},
            {"TỐI", "", "", "", "", "", "", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(120); 
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.getTableHeader().setBackground(BLUE_PRIMARY);
        table.getTableHeader().setForeground(Color.RED);
        table.setGridColor(new Color(230, 230, 230));

        // Căn giữa nội dung ô
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // --- NẠP DỮ LIỆU ---
        loadFullTimetable(model);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }   
    
// HÀM HỖ TRỢ //
    private void loadFullTimetable(DefaultTableModel model) {
        if (giangVien == null) return;

        // Truy vấn trực tiếp từ bảng lophoc dựa trên maGV
        String sql = "SELECT tenLop, lichHoc, caHoc FROM lophoc " +
                     "WHERE maGV = ? AND trangThai = 'open'";

        try (java.sql.Connection conn = com.trungtam.utils.DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, giangVien.getMaGV());
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String tenLop = rs.getString("tenLop");
                String lichHoc = rs.getString("lichHoc"); // Ví dụ: "2-4-6"
                String caHoc = rs.getString("caHoc");     // "sang" hoặc "toi"

                // 1. Xác định hàng (Sáng = 0, Tối = 1)
                int rowIndex = caHoc.equalsIgnoreCase("sang") ? 0 : 1;

                // Định dạng màu sắc khác một chút để giảng viên dễ nhận diện lịch dạy của mình
                String content = "<html><center><b style='color:#1a73e8'>" + tenLop + "</b><br>" +
                                 "<i style='color:#1a73e8'>(Giảng dạy)</i></center></html>";

                // 2. Điền vào các cột tương ứng dựa trên chuỗi lichHoc
                if (lichHoc.contains("2")) model.setValueAt(content, rowIndex, 1);
                if (lichHoc.contains("3")) model.setValueAt(content, rowIndex, 2);
                if (lichHoc.contains("4")) model.setValueAt(content, rowIndex, 3);
                if (lichHoc.contains("5")) model.setValueAt(content, rowIndex, 4);
                if (lichHoc.contains("6")) model.setValueAt(content, rowIndex, 5);
                if (lichHoc.contains("7")) model.setValueAt(content, rowIndex, 6);
                if (lichHoc.toLowerCase().contains("cn") || lichHoc.contains("8")) 
                    model.setValueAt(content, rowIndex, 7);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadDataLotrinh(DefaultTableModel model, String maKH) {
        model.setRowCount(0); 
        String sql = "SELECT sttbuoi, tenbaihoc, motangan FROM lotrinh WHERE maKhoaHoc = ? ORDER BY sttbuoi ASC";
        try (java.sql.Connection conn = com.trungtam.utils.DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    "Buổi " + rs.getInt("sttbuoi"), 
                    rs.getString("tenbaihoc"), 
                    rs.getString("motangan")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private JPanel createDataGrid(String[][] data) {
        JPanel panel = new JPanel(new GridLayout(data.length, 2, 2, 4));
        panel.setBackground(Color.WHITE);
        for (String[] row : data) {
            JLabel lbl = new JLabel(row[0]);
            lbl.setForeground(TEXT_LABEL);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            JLabel val = new JLabel(row[1]);
            val.setForeground(TEXT_MAIN);
            val.setFont(new Font("Segoe UI", Font.BOLD, 13));
            panel.add(lbl); panel.add(val);
        }
        return panel;
    }

    private JScrollPane createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);

        String[] menuItems = {"Trang chủ", "Thông báo", "Xem chương trình giảng dạy", "Tài liệu dạy học", "Thời khóa biểu"};

        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            btn.setForeground(BLUE_PRIMARY);
            btn.setBackground(Color.WHITE);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMaximumSize(new Dimension(280, 42));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setMargin(new Insets(0, 15, 0, 0));
            btn.addActionListener(e -> cardLayout.show(cardPanel, item));
            
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(245, 245, 245)); }
                public void mouseExited(MouseEvent e) { btn.setBackground(Color.WHITE); }
            });

            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(Color.WHITE);
            wrapper.setBorder(new MatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
            wrapper.add(btn, BorderLayout.CENTER);
            wrapper.setMaximumSize(new Dimension(280, 42));
            sidebar.add(wrapper);
        }

        JScrollPane scrollSidebar = new JScrollPane(sidebar);
        scrollSidebar.setPreferredSize(new Dimension(240, 0));
        scrollSidebar.setBorder(new MatteBorder(0, 0, 0, 1, BORDER_COLOR));
        return scrollSidebar;
    }

    private JPanel createHeader() {
    	 JPanel header = new JPanel(new BorderLayout());
         header.setBackground(BLUE_PRIMARY);
         header.setPreferredSize(new Dimension(0, 60));
         header.setBorder(new EmptyBorder(0, 20, 0, 20));
         JLabel logo = new JLabel(" Cổng thông tin đào tạo");
         logo.setForeground(Color.WHITE);
         logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
         header.add(logo, BorderLayout.WEST);
         String hoTen = (nguoiDung != null) ? nguoiDung.getHoTen() : "Guest";
         String maHV_Str = (taiKhoan != null) ? taiKhoan.getUsername() : "";
         JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
         userPanel.setOpaque(false);
         JLabel userInfo = new JLabel(hoTen + " - " + maHV_Str);
         userInfo.setForeground(Color.WHITE);
         userInfo.setFont(new Font("Segoe UI", Font.BOLD, 15));
         userPanel.add(userInfo);
         JButton btnLogout = new JButton("Đăng xuất");
         btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
         btnLogout.setForeground(BLUE_PRIMARY);
         btnLogout.setBackground(Color.WHITE);
         btnLogout.addActionListener(e -> {
             int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
             if (confirm == JOptionPane.YES_OPTION) {
                 this.dispose(); // Đóng cửa sổ hiện tại (HocVienUI)
                 
                 // Mở lại giao diện tổng (FullUI)
                 SwingUtilities.invokeLater(() -> {
                     new FullUI().setVisible(true); 
                 });
             }
         });
         userPanel.add(btnLogout);
         header.add(userPanel, BorderLayout.EAST);
         return header;
    }
    
    private JTable createStyledTable(DefaultTableModel model) {
        // Ghi đè phương thức isCellEditable để trả về false cho tất cả các ô
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa bất kỳ ô nào
            }
        };
                
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16)); 
        table.setRowHeight(40); 
        table.setSelectionBackground(new Color(232, 244, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(245, 245, 245));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(250, 250, 250));
        header.setPreferredSize(new Dimension(0, 45));
        
        return table;
    }

    private JPanel createPlaceholderPage(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(text, SwingConstants.CENTER));
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GiangVienUI().setVisible(true));
    }
}