package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import com.trungtam.model.HocVien;
import com.trungtam.model.NguoiDung;
import com.trungtam.model.SessionUser;
import com.trungtam.model.TaiKhoan;
import com.trungtam.model.DangKy;
import com.trungtam.dao.DangKyDAO;
import com.trungtam.dao.HocVienDAO;
import com.trungtam.dao.ThongBaoDAO;
import com.trungtam.utils.SessionManager;

public class HocVienUI extends JFrame {

    private TaiKhoan taiKhoan;
    private NguoiDung nguoiDung;
    private HocVien hocVien;
    private DangKyDAO dkDAO = new DangKyDAO();
    private ThongBaoDAO tbDAO = new ThongBaoDAO();
    private HocVienDAO hocVienDAO = new HocVienDAO();

    private final Color BLUE_PRIMARY = new Color(33, 150, 243);
    private final Color TEXT_MAIN = new Color(51, 51, 51);
    private final Color TEXT_LABEL = new Color(96, 103, 112);
    private final Color BORDER_COLOR = new Color(220, 224, 228);
    private final Color GREEN_CHART = new Color(76, 175, 80);

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private DefaultTableModel modelKH, modelLop;
    private JTable tblKhoaHoc, tblLopHoc;

    public HocVienUI() {
        SessionUser session = SessionManager.getSession();
        if (session != null) {
            this.taiKhoan = session.getTaiKhoan();
            this.nguoiDung = session.getNguoiDung();
            this.hocVien = session.getHocVien();
            
            // Fix lấy lại dữ liệu nếu thiếu
            if (this.taiKhoan != null && this.taiKhoan.getUsername() != null && this.hocVien == null) {
                this.hocVien = hocVienDAO.findById(this.taiKhoan.getUsername());
            }
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        setTitle("Cổng thông tin học viên");
        setSize(1280, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(createHomePanel(), "Trang chủ");
        cardPanel.add(createThongBaoPage(), "Thông báo");
        cardPanel.add(createChuongTrinhDaoTaoPage(), "Xem chương trình đào tạo");
        cardPanel.add(createDangKyMonPage(), "Đăng ký môn"); 
        cardPanel.add(createTaiLieuHocTapPage(), "Tài liệu học");
        cardPanel.add(createThoiKhoaBieuPage(), "Thời khóa biểu");
        cardPanel.add(createHocPhiPage(), "Học phí");

        add(cardPanel, BorderLayout.CENTER);

        // Gọi nạp dữ liệu SAU KHI các Panel đã được add vào CardPanel
        SwingUtilities.invokeLater(() -> {
            loadKHData();
        });
    }
//--- TRANG ĐĂNG KÝ MÔN--- //
    private JPanel createDangKyMonPage() {
        JPanel panel = new JPanel(new BorderLayout(15, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblTitle = new JLabel(" Đăng ký Khóa học & Chọn lớp");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(BLUE_PRIMARY);
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout(0, 25));
        mainContent.setOpaque(false);

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setOpaque(false);
        JLabel lblKH = new JLabel("1. Chọn Khóa học (Click để xem lớp):");
        lblKH.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topPanel.add(lblKH, BorderLayout.NORTH);

        modelKH = new DefaultTableModel(new Object[]{"Mã KH", "Tên Khóa học"}, 0);
        tblKhoaHoc = createStyledTable(modelKH);
        topPanel.add(new JScrollPane(tblKhoaHoc), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(0, 280));

        JLabel lblLop = new JLabel("2. Chọn Lớp học tương ứng:");
        lblLop.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bottomPanel.add(lblLop, BorderLayout.NORTH);

        modelLop = new DefaultTableModel(new Object[]{"Mã Lớp", "Tên Lớp", "Ca học", "Lịch"}, 0);
        tblLopHoc = createStyledTable(modelLop);
        bottomPanel.add(new JScrollPane(tblLopHoc), BorderLayout.CENTER);

        JButton btnReg = new JButton("XÁC NHẬN ĐĂNG KÝ MÔN");
        btnReg.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnReg.setBackground(GREEN_CHART);
        btnReg.setForeground(Color.RED);
        btnReg.setPreferredSize(new Dimension(0, 55));
        btnReg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottomPanel.add(btnReg, BorderLayout.SOUTH);

        mainContent.add(topPanel, BorderLayout.CENTER);
        mainContent.add(bottomPanel, BorderLayout.SOUTH);
        panel.add(mainContent, BorderLayout.CENTER);

        tblKhoaHoc.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblKhoaHoc.getSelectedRow();
                if (row != -1) {
                    loadLopData(tblKhoaHoc.getValueAt(row, 0).toString());
                }
            }
        });

        btnReg.addActionListener(e -> handleReg());

        return panel;
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
 //=== HÀM BỔ TRỢ LOAD DATA ===//   
    public void loadKHData() {
        if (modelKH == null) return;
        modelKH.setRowCount(0);
        List<String[]> list = dkDAO.getListKhoaHoc();
        for (String[] kh : list) {
            modelKH.addRow(kh);
        }
        // Ép bảng vẽ lại sau khi nạp dữ liệu
        if (tblKhoaHoc != null) tblKhoaHoc.revalidate();
    }

    public void loadLopData(String maKH) {
        if (modelLop == null) return;
        modelLop.setRowCount(0);
        List<String[]> list = dkDAO.getLopByKhoaHoc(maKH);
        for (String[] lop : list) {
            modelLop.addRow(lop);
        }
    }

//=== TẠO TRANG THÔNG BÁO ===//
    private JPanel createThongBaoPage() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30)); // Tăng lề cho thoáng

        // --- TIÊU ĐỀ NỔI BẬT ---
        JLabel lbl = new JLabel(" THÔNG BÁO HỆ THỐNG");
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
        List<com.trungtam.model.ThongBao> list = tbDAO.getThongBaoByRole(1); 
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
//=== TẠO TRANG THỜI KHÓA BIỂU ===//
    private JPanel createThoiKhoaBieuPage() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- TIÊU ĐỀ ---
        JLabel lblTitle = new JLabel(" THỜI KHÓA BIỂU TỔNG QUAN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(BLUE_PRIMARY);
        lblTitle.setBorder(new MatteBorder(0, 0, 3, 0, BLUE_PRIMARY));
        panel.add(lblTitle, BorderLayout.NORTH);

        // --- CẤU TRÚC BẢNG (Thứ 2 -> Chủ Nhật) ---
        String[] cols = {"Buổi", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật"};
        
        // Tạo dữ liệu trống ban đầu cho 2 hàng: Sáng và Chiều
        Object[][] data = {
            {"SÁNG", "", "", "", "", "", "", ""},
            {"TỐi", "", "", "", "", "", "", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);
        
        // --- ĐỊNH DẠNG GIAO DIỆN BẢNG ---
        table.setRowHeight(120); // Tăng chiều cao để hiện được nhiều dòng trong 1 ô
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.getTableHeader().setBackground(BLUE_PRIMARY);
        table.getTableHeader().setForeground(Color.RED);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(232, 242, 254));

        // Căn giữa nội dung trong ô
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // --- NẠP DỮ LIỆU TỪ DATABASE ---
        loadFullTimetable(model);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
//--- HÀM BỔ TRỢ LOAD TKB ---//    
    private void loadFullTimetable(DefaultTableModel model) {
        if (hocVien == null) return;

        String sql = "SELECT l.tenLop, l.lichHoc, l.caHoc FROM dangky d " +
                     "JOIN lophoc l ON d.maLop = l.maLop " +
                     "WHERE d.maHV = ? AND d.trangThai = 'open'";

        try (java.sql.Connection conn = com.trungtam.utils.DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, hocVien.getMaHV());
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String tenLop = rs.getString("tenLop");
                String lichHoc = rs.getString("lichHoc"); // Lấy chuỗi "2-4-6" hoặc "3-5-7"
                String caHoc = rs.getString("caHoc");     // Lấy "sang" hoặc "toi"
               

                // 1. Xác định hàng (Sáng = 0, Tối/Chiều = 1) dựa trên dữ liệu "sang" và "toi" của bạn
                int rowIndex = caHoc.equalsIgnoreCase("sang") ? 0 : 1;

                // Nội dung hiển thị trong ô
                String content = "<html><center><b style='color:#1a73e8'>" + tenLop + "</b><br>" ;
                               

                // 2. Kiểm tra chuỗi lichHoc và điền vào TẤT CẢ các thứ có mặt
                // Cột 1: Thứ 2, Cột 2: Thứ 3, ..., Cột 7: Chủ Nhật
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
//=== TẠO TRANG HỌC PHÍ ===//
    private JPanel createHocPhiPage() {
        JPanel panel = new JPanel(new BorderLayout(25, 25));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblTitle = new JLabel("QUẢN LÝ HỌC PHÍ ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(BLUE_PRIMARY);
        lblTitle.setBorder(new MatteBorder(0, 0, 3, 0, BLUE_PRIMARY));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Thêm cột Mã Lớp (ẩn hoặc hiện) để phục vụ việc xóa
        String[] cols = {"Khóa học", "Mã Lớp", "Học phí", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = createStyledTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // --- NÚT HỦY ĐĂNG KÝ ---
        JButton btnDelete = new JButton("Hủy đăng ký môn đã chọn");
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDelete.setBackground(new Color(211, 47, 47)); // Màu đỏ
        btnDelete.setForeground(Color.RED);
        btnDelete.setPreferredSize(new Dimension(0, 50));
        
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn môn muốn hủy!");
                return;
            }

            String tenMon = table.getValueAt(row, 0).toString();
            String maLop = table.getValueAt(row, 1).toString();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn hủy đăng ký môn: " + tenMon + "?", 
                "Xác nhận hủy", JOptionPane.YES_NO_OPTION);

         // Bên trong btnDelete.addActionListener ở hàm createHocPhiPage():
            if (confirm == JOptionPane.YES_OPTION) {
                if (dkDAO.delete(hocVien.getMaHV(), maLop)) {
                    JOptionPane.showMessageDialog(this, "Đã hủy đăng ký môn học thành công!");
                    
                    // 👉 ĐÃ SỬA: Thay vì chỉ add tay 2 trang cũ, gọi hàm tổng thể để đồng bộ dọn dẹp sạch sẽ tài liệu/lộ trình
                    refreshPagesAfterReg(); 
                }            
            }
        });

        panel.add(btnDelete, BorderLayout.SOUTH);

        // Nạp dữ liệu vào bảng (Giữ nguyên logic SQL cũ của bạn)
        loadDataToHocPhi(model);

        return panel;
    }

    private void loadDataToHocPhi(DefaultTableModel model) {
        if (hocVien == null) return;
        String sql = "SELECT k.tenKhoaHoc, l.maLop, k.hocPhi, d.trangThai FROM dangky d " +
                     "JOIN lophoc l ON d.maLop = l.maLop " +
                     "JOIN khoahoc k ON l.maKhoaHoc = k.maKhoaHoc WHERE d.maHV = ?";
        try (java.sql.Connection conn = com.trungtam.utils.DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hocVien.getMaHV());
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString(1), 
                    rs.getString(2), 
                    String.format("%,.0f VNĐ", rs.getDouble(3)), 
                    rs.getString(4).toUpperCase()
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
//=== HÀM KIỂM TRA ===//    
    private void handleReg() {
        // 1. Kiểm tra xem người dùng đã chọn dòng nào trong bảng lớp học chưa
        int row = tblLopHoc.getSelectedRow();
        if (row == -1) { 
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học trong danh sách để đăng ký!"); 
            return; 
        }

        // 2. Kiểm tra thông tin học viên từ Session
        if (hocVien == null || hocVien.getMaHV() == null || hocVien.getMaHV().equals("Unknown")) {
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: Không xác định được mã học viên.\nVui lòng đăng xuất và đăng nhập lại!");
            return;
        }

        // Lấy thông tin cần thiết
        String maLop = tblLopHoc.getValueAt(row, 0).toString();
        String maHV = hocVien.getMaHV(); 

        // Debug kiểm tra giá trị thực tế gửi xuống Database
        System.out.println("--- TIẾN TRÌNH ĐĂNG KÝ ---");
        System.out.println("Student ID: " + maHV);
        System.out.println("Class ID: " + maLop);

        // 3. Kiểm tra trùng lặp
        if (dkDAO.checkTonTai(maHV, maLop)) {
            JOptionPane.showMessageDialog(this, "Bạn đã đăng ký lớp học này rồi. Vui lòng kiểm tra trong Thời khóa biểu!");
        } else {
            // 4. Tạo đối tượng đăng ký mới
            DangKy dk = new DangKy(); 
            dk.setMaHV(maHV); 
            dk.setMaLop(maLop); 
            dk.setTrangThai("open"); // Trạng thái mặc định khi mới đăng ký

            // 5. Thực hiện lưu vào Database
            if (dkDAO.insert(dk)) {
                JOptionPane.showMessageDialog(this, "Chúc mừng! Bạn đã đăng ký thành công lớp: " + maLop);
                
                // 6. Cập nhật lại toàn bộ các trang dữ liệu
                refreshPagesAfterReg();
            } else {
                JOptionPane.showMessageDialog(this, "Đăng ký thất bại! Vui lòng kiểm tra lại kết nối mạng hoặc Database.");
            }
        }
    }
    // Hàm hỗ trợ làm mới các tab dữ liệu
 // =========================================================================
    // ĐÃ SỬA: LÀM MỚI TẤT CẢ CÁC TRANG SAU KHI ĐĂNG KÝ MÔN THÀNH CÔNG
    // =========================================================================
    private void refreshPagesAfterReg() {
        if (cardPanel == null || cardLayout == null) return;

        // 1. Ép nạp lại dữ liệu động cho Trang chủ (Thông tin học viên)
        cardPanel.add(createHomePanel(), "Trang chủ");

        // 2. Ép nạp lại lộ trình học động dựa trên môn học mới
        cardPanel.add(createChuongTrinhDaoTaoPage(), "Xem chương trình đào tạo");

        // 3. Ép nạp lại kho tài liệu tham khảo theo môn học mới
        cardPanel.add(createTaiLieuHocTapPage(), "Tài liệu học");

        // 4. Khởi tạo lại Thời khóa biểu và Học phí 
        cardPanel.add(createThoiKhoaBieuPage(), "Thời khóa biểu");
        cardPanel.add(createHocPhiPage(), "Học phí");

        // Thông báo cho hệ thống vẽ lại đồ họa render
        cardPanel.revalidate();
        cardPanel.repaint();

        // Chuyển màn hình về trang Học phí để học viên kiểm tra hóa đơn trực quan
        cardLayout.show(cardPanel, "Học phí");
    }
//=== TẠO TRANG CHƯƠNG TRÌNH ĐÀO TẠO ===//
    private JPanel createChuongTrinhDaoTaoPage() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- TIÊU ĐỀ TRANG ---
        JLabel lblTitle = new JLabel(" LỘ TRÌNH ĐÀO TẠO CỦA TÔI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(BLUE_PRIMARY);
        lblTitle.setBorder(new MatteBorder(0, 0, 3, 0, BLUE_PRIMARY));
        panel.add(lblTitle, BorderLayout.NORTH);

        // --- KHU VỰC CHỌN MÔN HỌC ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setOpaque(false);
        
        JLabel lblSelect = new JLabel("Môn học đã đăng ký:");
        lblSelect.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        JComboBox<String> cbKhoaHoc = new JComboBox<>();
        cbKhoaHoc.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cbKhoaHoc.setPreferredSize(new Dimension(450, 45));

        // --- BẢNG HIỂN THỊ LỘ TRÌNH (Dùng hàm StyledTable bạn đã có) ---
        String[] cols = {"STT", "Tên bài học", "Nội dung bài học"};
        DefaultTableModel modelLotrinh = new DefaultTableModel(cols, 0);
        JTable table = createStyledTable(modelLotrinh); 
        
        // Chỉnh độ rộng cột cho đẹp
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(600);

        // --- LOGIC: CHỈ NẠP CÁC MÔN ĐÃ ĐĂNG KÝ VÀO COMBOBOX ---
        if (hocVien != null) {
            // Query lấy các khóa học từ bảng dangky -> lophoc -> khoahoc
            String sql = "SELECT DISTINCT k.maKhoaHoc, k.tenKhoaHoc FROM dangky d " +
                         "JOIN lophoc l ON d.maLop = l.maLop " +
                         "JOIN khoahoc k ON l.maKhoaHoc = k.maKhoaHoc WHERE d.maHV = ?";
            try (java.sql.Connection conn = com.trungtam.utils.DBConnection.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, hocVien.getMaHV());
                java.sql.ResultSet rs = ps.executeQuery();
                
                boolean hasData = false;
                while (rs.next()) {
                    cbKhoaHoc.addItem(rs.getString("maKhoaHoc") + " - " + rs.getString("tenKhoaHoc"));
                    hasData = true;
                }
                
                if (!hasData) {
                    cbKhoaHoc.addItem("--- Bạn chưa đăng ký môn học nào ---");
                    cbKhoaHoc.setEnabled(false);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        // --- SỰ KIỆN: KHI CHỌN MÔN TRÊN COMBOBOX THÌ HIỆN BUỔI HỌC ---
        cbKhoaHoc.addActionListener(e -> {
            String selected = (String) cbKhoaHoc.getSelectedItem();
            if (selected != null && selected.contains(" - ")) {
                String maKH = selected.split(" - ")[0]; // Tách lấy mã KHxxx
                loadDataLotrinh(modelLotrinh, maKH);
            }
        });

        // Tự động load môn đầu tiên nếu có
        if (cbKhoaHoc.getItemCount() > 0 && cbKhoaHoc.isEnabled()) {
            cbKhoaHoc.setSelectedIndex(0);
            String firstMaKH = ((String)cbKhoaHoc.getItemAt(0)).split(" - ")[0];
            loadDataLotrinh(modelLotrinh, firstMaKH);
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
    
    private void loadDataLotrinh(DefaultTableModel model, String maKH) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        // Truy vấn dựa trên bảng lotrinh bạn đã nạp dữ liệu mẫu
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//=== TẠO TRANG TÀI LIỆU HỌC ===//
    private JPanel createTaiLieuHocTapPage() {
        JPanel panel = new JPanel(new BorderLayout(25, 25));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- TIÊU ĐỀ ---
        JLabel lblTitle = new JLabel(" KHO TÀI LIỆU HỌC TẬP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(BLUE_PRIMARY);
        lblTitle.setBorder(new MatteBorder(0, 0, 3, 0, BLUE_PRIMARY));
        panel.add(lblTitle, BorderLayout.NORTH);

        // --- KHU VỰC CHỌN MÔN ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setOpaque(false);
        
        JLabel lblSelect = new JLabel("Chọn môn học để xem tài liệu:");
        lblSelect.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        JComboBox<String> cbKhoaHoc = new JComboBox<>();
        cbKhoaHoc.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cbKhoaHoc.setPreferredSize(new Dimension(450, 45));

        // --- BẢNG HIỂN THỊ TÀI LIỆU ---
        String[] cols = {"STT", "Tên tài liệu / Sách tham khảo", "Loại", "Trạng thái"};
        DefaultTableModel modelTailieu = new DefaultTableModel(cols, 0);
        JTable table = createStyledTable(modelTailieu); 

        // --- LOGIC: NẠP DANH SÁCH MÔN ĐÃ ĐĂNG KÝ ---
        if (hocVien != null) {
            String sql = "SELECT DISTINCT k.maKhoaHoc, k.tenKhoaHoc FROM dangky d " +
                         "JOIN lophoc l ON d.maLop = l.maLop " +
                         "JOIN khoahoc k ON l.maKhoaHoc = k.maKhoaHoc WHERE d.maHV = ?";
            try (java.sql.Connection conn = com.trungtam.utils.DBConnection.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, hocVien.getMaHV());
                java.sql.ResultSet rs = ps.executeQuery();
                
                while (rs.next()) {
                    cbKhoaHoc.addItem(rs.getString("maKhoaHoc") + " - " + rs.getString("tenKhoaHoc"));
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        // --- SỰ KIỆN: CHỌN MÔN TRÊN COMBOBOX ---
        cbKhoaHoc.addActionListener(e -> {
            String selected = (String) cbKhoaHoc.getSelectedItem();
            if (selected != null) {
                String maKH = selected.split(" - ")[0]; 
                loadDataTailieuToTable(modelTailieu, maKH);
            }
        });

        // Load dữ liệu môn đầu tiên mặc định
        if (cbKhoaHoc.getItemCount() > 0) {
            cbKhoaHoc.setSelectedIndex(0);
            loadDataTailieuToTable(modelTailieu, ((String)cbKhoaHoc.getItemAt(0)).split(" - ")[0]);
        }

        topPanel.add(lblSelect);
        topPanel.add(cbKhoaHoc);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }
    private void loadDataTailieuToTable(DefaultTableModel model, String maKH) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        // Chỉ lấy tài liệu dành cho Học viên (doituong = 1)
        String sql = "SELECT tentailieu, loai FROM tailieu WHERE maKhoaHoc = ? AND doituong = 1";
        
        try (java.sql.Connection conn = com.trungtam.utils.DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            java.sql.ResultSet rs = ps.executeQuery();
            
            int stt = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    stt++, 
                    rs.getString("tentailieu"), 
                    rs.getString("loai"),
                    "Sẵn sàng (Công khai)"
                });
            }
            
            if (stt == 1) { // Nếu không tìm thấy cuốn sách nào
                 model.addRow(new Object[]{"-", "Chưa có tài liệu cho môn học này", "-", "-"});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 // Tạo MENU
    private JPanel createSimpleTablePage(String title, String[] cols, Object[][] data) {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(BLUE_PRIMARY);
        p.add(lbl, BorderLayout.NORTH);
        JTable table = new JTable(data, cols);
        table.setRowHeight(30);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private JScrollPane createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        String[] menuItems = {"Trang chủ", "Thông báo", "Xem chương trình đào tạo", "Đăng ký môn", "Tài liệu học", "Thời khóa biểu", "Học phí"};
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
                public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(245, 247, 255)); }
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
        String hoTen = (nguoiDung != null && nguoiDung.getHoTen() != null) ? nguoiDung.getHoTen() : "Unknown";
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

    private JScrollPane createHomePanel() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(new EmptyBorder(10, 15, 10, 15));
        String hoTen = (nguoiDung != null && nguoiDung.getHoTen() != null) ? nguoiDung.getHoTen() : "Unknown";
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel lblWelcome = new JLabel(" Chào mừng học viên, " + hoTen);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 23));
        lblWelcome.setForeground(BLUE_PRIMARY);
        titlePanel.add(lblWelcome); 
        JLabel lblDate = new JLabel(" Hôm nay: " + java.time.LocalDate.now());
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDate.setForeground(TEXT_LABEL);        
        titlePanel.add(lblDate);
        mainContent.add(titlePanel);
        mainContent.add(Box.createVerticalStrut(12));
        JPanel infoCard = createStudentInfoCard();
        infoCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, infoCard.getPreferredSize().height));
        mainContent.add(infoCard);
        mainContent.add(Box.createVerticalStrut(12));

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

//=== TRANG CHỦ DỮ LIỆU HỌC VIÊN ===//
    private JPanel createStudentInfoCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(BLUE_PRIMARY, 1, true));

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER_COLOR), 
                new EmptyBorder(8, 12, 8, 12)));

        JLabel lblTitle = new JLabel(" Thông tin học viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(TEXT_MAIN);
        header.add(lblTitle, BorderLayout.WEST);
        card.add(header, BorderLayout.NORTH);

        // --- BODY ---
        JPanel body = new JPanel(new GridLayout(1, 2, 0, 0));
        body.setBackground(Color.WHITE);

        // Lấy thông tin cơ bản từ session
        String maHV = (taiKhoan != null) ? taiKhoan.getUsername() : "Unknown";
        String hoTen = (nguoiDung != null && nguoiDung.getHoTen() != null) ? nguoiDung.getHoTen() : "Unknown";
        String email = (nguoiDung != null && nguoiDung.getEmail() != null) ? nguoiDung.getEmail() : "Chưa cập nhật";
        String ngaySinh = (nguoiDung != null && nguoiDung.getNgaySinh() != null) ? nguoiDung.getNgaySinh() : "Chưa cập nhật";
        String sdt = (nguoiDung != null && nguoiDung.getSoDienThoai() != null) ? nguoiDung.getSoDienThoai() : "Chưa cập nhật";
        String gioiTinh = (nguoiDung != null && nguoiDung.getGioiTinh() != null) ? nguoiDung.getGioiTinh() : "Chưa cập nhật";
        String queQuan = (nguoiDung != null && nguoiDung.getQueQuan() != null) ? nguoiDung.getQueQuan() : "Chưa cập nhật";

        // --- LOGIC: TRUY VẤN KHÓA HỌC VÀ LỚP HỌC ---
        String tenCacKhoaHoc = "Chưa đăng ký";
        String tenCacLopHoc = "Chưa có lớp";

        if (!maHV.equals("Unknown")) {
            // SQL JOIN để lấy tên khóa học và tên lớp từ bảng dangky
            String sql = "SELECT k.tenKhoaHoc, l.tenLop FROM dangky d " +
                         "JOIN lophoc l ON d.maLop = l.maLop " +
                         "JOIN khoahoc k ON l.maKhoaHoc = k.maKhoaHoc " +
                         "WHERE d.maHV = ? AND d.trangThai = 'open'";
            
            java.util.StringJoiner sjKhoaHoc = new java.util.StringJoiner(", ");
            java.util.StringJoiner sjLopHoc = new java.util.StringJoiner(", ");
            
            try (java.sql.Connection conn = com.trungtam.utils.DBConnection.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maHV);
                java.sql.ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    sjKhoaHoc.add(rs.getString("tenKhoaHoc"));
                    sjLopHoc.add(rs.getString("tenLop"));
                }
                if (sjKhoaHoc.length() > 0) tenCacKhoaHoc = sjKhoaHoc.toString();
                if (sjLopHoc.length() > 0) tenCacLopHoc = sjLopHoc.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Cột 1: Ảnh và thông tin cơ bản
        JPanel col1Wrapper = new JPanel(new BorderLayout(15, 0));
        col1Wrapper.setBackground(Color.WHITE);
        col1Wrapper.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 0, 1, BORDER_COLOR), 
                new EmptyBorder(10, 10, 10, 10)));

        JLabel avatar = new JLabel("Ảnh", SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(90, 110));
        avatar.setOpaque(true);
        avatar.setBackground(new Color(230, 235, 240));
        col1Wrapper.add(avatar, BorderLayout.WEST);

        String[][] data1 = {
            {"Mã HV:", maHV}, 
            {"Họ tên:", hoTen}, 
            {"Ngày sinh:", ngaySinh}, 
            {"Giới tính:", gioiTinh}, 
            {"SĐT:", sdt}
        };
        col1Wrapper.add(createDataGrid(data1), BorderLayout.CENTER);

        // Cột 2: Thông tin học tập (Lấy từ DB)
        JPanel col2Wrapper = new JPanel(new BorderLayout());
        col2Wrapper.setBackground(Color.WHITE);
        col2Wrapper.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[][] data2 = {
            {"Email:", email}, 
            {"Khóa học:", tenCacKhoaHoc}, // Đã nạp động
            {"Lớp học:", tenCacLopHoc},   // Đã nạp động
            {"Quê quán:", queQuan}, 
            {"Trạng thái:", "Đang theo học"}
        };
        col2Wrapper.add(createDataGrid(data2), BorderLayout.CENTER);

        body.add(col1Wrapper);
        body.add(col2Wrapper);
        card.add(body, BorderLayout.CENTER);

        return card;
    }
    private JPanel createDataGrid(String[][] data) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST; gbc.insets = new Insets(4, 5, 4, 15);
        for (int i = 0; i < data.length; i++) {
            gbc.gridy = i; gbc.gridx = 0; gbc.weightx = 0;
            JLabel lbl = new JLabel(data[i][0]);
            lbl.setForeground(TEXT_LABEL); lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            panel.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            JLabel val = new JLabel(data[i][1]);
            val.setForeground(TEXT_MAIN); val.setFont(new Font("Segoe UI", Font.BOLD, 13));
            panel.add(val, gbc);
        }
        return panel;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HocVienUI().setVisible(true));
    }
}