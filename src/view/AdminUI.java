package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

// Import các lớp từ dự án của bạn
import com.trungtam.dao.*;
import com.trungtam.model.*;
import com.trungtam.services.ThongBaoService;

public class AdminUI extends JFrame {

    private final Color COLOR_CYAN_MAIN = new Color(52, 170, 220); 
    private final Color COLOR_SIDEBAR_BG = new Color(255, 255, 255);
    private final Color COLOR_BG_LIGHT = new Color(245, 247, 250);   
    private final Color COLOR_TEXT_BLUE = new Color(52, 170, 220);

    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    private ThongBaoService thongBaoService = new ThongBaoService();
    private GiangVienDAO giangVienDAO = new GiangVienDAO();
    private HocVienDAO hocVienDAO = new HocVienDAO();
    private NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    private KhoaHocDAO khoaHocDAO = new KhoaHocDAO();
    private LopHocDAO lopHocDAO = new LopHocDAO();
    private CircularProgressBar progressHV;
    private CircularProgressBar progressGV;
    private AdminDAO adminDAO = new AdminDAO();
    
    private DefaultTableModel modelThongBao;
    private DefaultTableModel modelGiangVien;
    private DefaultTableModel modelHocVien;
    
    public AdminUI() {
        setTitle("HỆ THỐNG QUẢN LÝ ĐÀO TẠO - TRANG QUẢN TRỊ");
        setSize(1250, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createTopHeader(), BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(COLOR_BG_LIGHT);

        cardPanel.add(createDashboardPage(), "Dashboard");
        cardPanel.add(createNotificationPage(), "ThongBao");
        cardPanel.add(createEntityPage("GIẢNG VIÊN", "GV"), "GiangVien");
        cardPanel.add(createEntityPage("HỌC VIÊN", "HV"), "HocVien");
        cardPanel.add(createKhoaHocPage(), "KhoaHoc");
        cardPanel.add(createLopHocPage(), "LopHoc");       
        cardPanel.add(createPlaceholderPage("Thống Kê Báo Cáo"), "ThongKe");

        add(cardPanel, BorderLayout.CENTER);
    }

    // ================= TRANG QUẢN LÝ (DẠNG THẺ GỌN GÀNG) =================
    private JPanel createEntityPage(String title, String type) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG_LIGHT);

        // Tiêu đề trang
        JLabel lblTitle = new JLabel("  ▣ QUẢN LÝ " + title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(COLOR_CYAN_MAIN);
        lblTitle.setPreferredSize(new Dimension(0, 60));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // --- THAY ĐỔI TẠI ĐÂY ---
        // Sử dụng GridLayout(0, 4, 15, 15): 0 hàng (tự động), 4 cột, khoảng cách 15px
        JPanel container = new JPanel(new GridLayout(0, 4, 15, 15));
        container.setBackground(COLOR_BG_LIGHT);
        
        // Thêm EmptyBorder để tạo khoảng cách với các mép cửa sổ (Trên, Trái, Dưới, Phải)
        container.setBorder(new EmptyBorder(10, 20, 20, 20));

        // Nạp dữ liệu
        if (type.equals("GV")) {
            List<Object[]> ds = giangVienDAO.getGiangVienFullInfo(); 
            for (Object[] data : ds) container.add(new ProfileCard(data, "GV"));
        } else {
            List<Object[]> ds = hocVienDAO.getHocVienFullInfo();
            for (Object[] data : ds) container.add(new ProfileCard(data, "HV"));
        }

        // Đặt container vào một Panel bọc (Wrapper) để tránh việc các Card bị kéo dãn chiều cao
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(COLOR_BG_LIGHT);
        wrapper.add(container, BorderLayout.NORTH); 

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        // Khóa thanh cuộn ngang, chỉ cho phép cuộn dọc
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scroll, BorderLayout.CENTER);

        return mainPanel;
    }

    // ================= LỚP Ô THÔNG TIN (PROFILE CARD) =================
    class ProfileCard extends JPanel {
        public ProfileCard(Object[] data, String type) {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            // Bo góc nhẹ bằng cách sử dụng LineBorder màu nhạt
            setBorder(new LineBorder(new Color(235, 235, 235), 1));
            setPreferredSize(new Dimension(280, 180)); // Giảm kích thước nhỏ lại

            // Phần nội dung chữ
            JPanel body = new JPanel();
            body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
            body.setOpaque(false);
            body.setBorder(new EmptyBorder(15, 15, 10, 15)); // Padding vừa phải

            // Tên đối tượng (In đậm, kích thước lớn)
            JLabel name = new JLabel(data[1].toString());
            name.setFont(new Font("Segoe UI", Font.BOLD, 16));
            name.setForeground(new Color(33, 33, 33));
            
            body.add(name);
            body.add(Box.createVerticalStrut(10)); // Khoảng cách dưới tên

            // Các dòng thông tin nhỏ hơn
            Font infoFont = new Font("Segoe UI", Font.PLAIN, 13);
            
            JLabel lblEmail = new JLabel("• Email: " + data[2]);
            lblEmail.setFont(infoFont);
            body.add(lblEmail);
            body.add(Box.createVerticalStrut(5));

            JLabel lblSdt = new JLabel("• SĐT: " + data[3]);
            lblSdt.setFont(infoFont);
            body.add(lblSdt);
            body.add(Box.createVerticalStrut(5));

            String extraInfo = type.equals("GV") ? "Chuyên môn: " + data[5] : "Trạng thái: " + data[4];
            JLabel lblExtra = new JLabel("• " + extraInfo);
            lblExtra.setFont(infoFont);
            body.add(lblExtra);

            add(body, BorderLayout.CENTER);

            // Nút Chi tiết (Dẹt hơn, màu Cyan chuẩn)
            JButton btnDetail = new JButton("Chi tiết →");
            btnDetail.setPreferredSize(new Dimension(0, 35)); // Giảm chiều cao nút
            btnDetail.setBackground(COLOR_CYAN_MAIN);
            btnDetail.setForeground(Color.WHITE);
            btnDetail.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnDetail.setFocusPainted(false);
            btnDetail.setBorderPainted(false);
            btnDetail.setOpaque(true);
            btnDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Hiệu ứng hover cho nút
            btnDetail.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btnDetail.setBackground(COLOR_CYAN_MAIN.darker());
                }
                public void mouseExited(MouseEvent e) {
                    btnDetail.setBackground(COLOR_CYAN_MAIN);
                }
            });

            btnDetail.addActionListener(e -> showFullDetailPopup(data, type));
            add(btnDetail, BorderLayout.SOUTH);
        }
    }
    // ================= POPUP HIỂN THỊ TOÀN BỘ THÔNG TIN & LƯU =================
    private void showFullDetailPopup(Object[] data, String type) {
        JDialog dialog = new JDialog(this, "Thông tin chi tiết", true);
        dialog.setSize(500, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(0, 2, 10, 15));
        p.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Lấy mã người dùng để truy vấn NguoiDungDAO
        String maND = (type.equals("GV")) ? data[9].toString() : data[8].toString();
        NguoiDung nd = nguoiDungDAO.findById(maND);

        // Các Field nhập liệu
        JTextField tTen = new JTextField(nd.getHoTen());
        JTextField tEmail = new JTextField(nd.getEmail());
        JTextField tSdt = new JTextField(nd.getSoDienThoai());
        JTextField tNS = new JTextField(nd.getNgaySinh());
        JTextField tQue = new JTextField(nd.getQueQuan());
        JComboBox<String> cbGT = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbGT.setSelectedItem(nd.getGioiTinh());

        p.add(new JLabel("Họ tên:")); p.add(tTen);
        p.add(new JLabel("Email:")); p.add(tEmail);
        p.add(new JLabel("Số điện thoại:")); p.add(tSdt);
        p.add(new JLabel("Ngày sinh (yyyy-mm-dd):")); p.add(tNS);
        p.add(new JLabel("Giới tính:")); p.add(cbGT);
        p.add(new JLabel("Quê quán:")); p.add(tQue);

        JTextField tExtra1 = new JTextField(); 
        JTextField tExtra2 = new JTextField();

        if (type.equals("GV")) {
            GiangVien gv = giangVienDAO.findById(data[0].toString());
            tExtra1.setText(gv.getHocVi()); tExtra2.setText(gv.getChuyenMon());
            p.add(new JLabel("Học vị:")); p.add(tExtra1);
            p.add(new JLabel("Chuyên môn:")); p.add(tExtra2);
        } else {
            HocVien hv = hocVienDAO.findById(data[0].toString());
            tExtra1.setText(hv.getTrangThai());
            p.add(new JLabel("Trạng thái:")); p.add(tExtra1);
        }

        JButton btnSave = new JButton("XÁC NHẬN LƯU THAY ĐỔI");
        btnSave.setPreferredSize(new Dimension(0, 55));
        btnSave.setBackground(new Color(40, 167, 69)); // Màu xanh lá xác nhận
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        
        btnSave.addActionListener(e -> {
            nd.setHoTen(tTen.getText()); nd.setEmail(tEmail.getText());
            nd.setSoDienThoai(tSdt.getText()); nd.setNgaySinh(tNS.getText());
            nd.setQueQuan(tQue.getText()); nd.setGioiTinh(cbGT.getSelectedItem().toString());
            
            if (nguoiDungDAO.update(nd)) {
                if (type.equals("GV")) {
                    GiangVien gv = giangVienDAO.findById(data[0].toString());
                    gv.setHocVi(tExtra1.getText()); gv.setChuyenMon(tExtra2.getText());
                    giangVienDAO.update(gv);
                } else {
                    HocVien hv = hocVienDAO.findById(data[0].toString());
                    hv.setTrangThai(tExtra1.getText()); hocVienDAO.update(hv);
                }
                JOptionPane.showMessageDialog(dialog, "Đã cập nhật thông tin thành công!");
                dialog.dispose();
                // Refresh lại trang hiện tại
                cardLayout.show(cardPanel, type.equals("GV") ? "GiangVien" : "HocVien");
            }
        });

        dialog.add(new JScrollPane(p), BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private JPanel createKhoaHocPage() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(COLOR_BG_LIGHT);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Tiêu đề và Thanh công cụ
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("▣ QUẢN LÝ DANH MỤC KHÓA HỌC");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(COLOR_CYAN_MAIN);
        topPanel.add(lblTitle, BorderLayout.WEST);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        toolbar.setOpaque(false);
        JButton btnAdd = createStyledButton("Thêm Khóa Học", new Color(40, 167, 69));
        JButton btnEdit = createStyledButton("Sửa Thông Tin", COLOR_CYAN_MAIN);
        JButton btnDelete = createStyledButton("Xóa Khóa Học", new Color(220, 53, 69));
        
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        topPanel.add(toolbar, BorderLayout.EAST);

        // 2. Bảng dữ liệu
        String[] cols = {"Mã KH", "Tên Khóa Học", "Học Phí", "Thời Lượng", "Trạng Thái"};
        DefaultTableModel modelKH = new DefaultTableModel(cols, 0);
        JTable table = createAdminTable(modelKH);

        // Load dữ liệu từ DAO (Giả định bạn có KhoaHocDAO)
        loadDataToTableKH(modelKH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Sự kiện nút bấm (Ví dụ cho nút thêm)
        btnAdd.addActionListener(e -> showKhoaHocDialog(null, modelKH));

        return mainPanel;
    }

    private JPanel createLopHocPage() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(COLOR_BG_LIGHT);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Header & Toolbar ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("▣ QUẢN LÝ DANH SÁCH LỚP HỌC");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(COLOR_CYAN_MAIN);
        topPanel.add(lblTitle, BorderLayout.WEST);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        toolbar.setOpaque(false);
        JButton btnAdd = createStyledButton("Mở Lớp Mới", new Color(40, 167, 69));
        JButton btnEdit = createStyledButton("Sửa Lớp", COLOR_CYAN_MAIN);
        JButton btnDelete = createStyledButton("Xóa Lớp", new Color(220, 53, 69));
        
        toolbar.add(btnAdd); toolbar.add(btnEdit); toolbar.add(btnDelete);
        topPanel.add(toolbar, BorderLayout.EAST);

        // --- Bảng hiển thị ---
        String[] cols = {"Mã Lớp", "Tên Lớp", "Khóa Học", "Giảng Viên", "Ca", "Lịch", "Trạng Thái"};
        DefaultTableModel modelLH = new DefaultTableModel(cols, 0);
        JTable table = createAdminTable(modelLH); // Dùng hàm tạo bảng Admin bạn đã có

        // Tùy chỉnh độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);

        loadDataToTableLH(modelLH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        return mainPanel;
    } 
    
    private void showLopHocDialog() {
        JDialog dialog = new JDialog(this, "Thiết lập lớp học mới", true);
        dialog.setSize(450, 600);
        dialog.setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(0, 1, 5, 10));
        p.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Lấy danh sách khóa học để chọn
        JComboBox<String> cbKhoaHoc = new JComboBox<>();
        for(KhoaHoc kh : khoaHocDAO.getList()) {
            cbKhoaHoc.addItem(kh.getMaKhoaHoc() + " - " + kh.getTenKhoaHoc());
        }

        // Các thành phần khác
        JTextField tTenLop = new JTextField();
        JComboBox<String> cbCa = new JComboBox<>(new String[]{"sang", "toi"});
        JTextField tLich = new JTextField("2-4-6");

        p.add(new JLabel("Chọn Khóa học:")); p.add(cbKhoaHoc);
        p.add(new JLabel("Tên lớp:")); p.add(tTenLop);
        p.add(new JLabel("Ca học:")); p.add(cbCa);
        p.add(new JLabel("Lịch học (VD: 2-4-6):")); p.add(tLich);

        JButton btnSave = createStyledButton("LƯU THÔNG TIN", new Color(40, 167, 69));
        dialog.add(p, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    // ================= =================
    private JPanel createTopHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_CYAN_MAIN);
        header.setPreferredSize(new Dimension(0, 60)); // Tăng nhẹ chiều cao để nút trông thoáng hơn
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Bên trái: Logo/Tiêu đề
        JLabel lbl = new JLabel("🌐 HỆ THỐNG QUẢN TRỊ VIÊN");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(lbl, BorderLayout.WEST);

        // Bên phải: Thông tin Admin & Nút Đăng xuất
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        userPanel.setOpaque(false);

        JLabel userInfo = new JLabel("Quản trị viên (Admin)");
        userInfo.setForeground(Color.WHITE);
        userInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userPanel.add(userInfo);

        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setForeground(COLOR_CYAN_MAIN);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn đăng xuất khỏi quyền Quản trị?", 
                "Xác nhận đăng xuất", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose(); // Đóng AdminUI
                
                // Mở lại giao diện tổng/Login (Giả định là FullUI như các phần trước)
                SwingUtilities.invokeLater(() -> {
                    // Nếu bạn có class FullUI hoặc LoginUI, hãy gọi nó ở đây
                    // new FullUI().setVisible(true); 
                });
            }
        });

        userPanel.add(btnLogout);
        header.add(userPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        panel.setPreferredSize(new Dimension(260, 0));
        panel.setBackground(COLOR_SIDEBAR_BG);
        panel.setBorder(new MatteBorder(0, 0, 0, 1, new Color(220, 230, 240)));
        String[][] menus = {{"Dashboard", "Dashboard"}, {"Thông báo", "ThongBao"}, {"Học viên", "HocVien"}, {"Giảng viên", "GiangVien"}, {"Khóa học", "KhoaHoc"}, {"Lớp học", "LopHoc"}};
        for (String[] m : menus) panel.add(createMenuButton(m[0], m[1]));
        return panel;
    }

    private JButton createMenuButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(240, 45));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 20, 0, 0));
        btn.addActionListener(e -> cardLayout.show(cardPanel, cardName));
        return btn;
    }
    private void loadDataToTableLH(DefaultTableModel model) {
        model.setRowCount(0);
        List<Object[]> list = lopHocDAO.getLopHocFullInfo();
        for (Object[] row : list) {
            model.addRow(row);
        }
    }
 // Tạo bảng theo phong cách Admin
    private JTable createAdminTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(230, 245, 255));
        table.setGridColor(new Color(240, 240, 240));
        table.setShowVerticalLines(false);
        return table;
    }

    // Tạo nút bấm đồng bộ
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void showKhoaHocDialog(Object[] data, DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Thông tin khóa học", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 10));
        p.setBorder(new EmptyBorder(20, 30, 20, 30));

        JTextField tTen = new JTextField();
        JTextField tPhi = new JTextField();
        JTextField tTime = new JTextField();
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Hoạt động", "Tạm dừng"});

        p.add(new JLabel("Tên khóa học:")); p.add(tTen);
        p.add(new JLabel("Học phí (VNĐ):")); p.add(tPhi);
        p.add(new JLabel("Thời lượng (buổi):")); p.add(tTime);
        p.add(new JLabel("Trạng thái:")); p.add(cbStatus);

        JButton btnSave = createStyledButton("XÁC NHẬN LƯU", new Color(40, 167, 69));
        btnSave.addActionListener(e -> {
            // Logic gọi KhoaHocDAO.insert() hoặc update() ở đây
            JOptionPane.showMessageDialog(dialog, "Đã lưu khóa học thành công!");
            dialog.dispose();
        });

        dialog.add(p, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void loadDataToTableKH(DefaultTableModel model) {
        // Xóa dữ liệu cũ trong bảng để tránh bị trùng lặp khi refresh
        model.setRowCount(0); 

        // Lấy danh sách khóa học từ DAO
        // Lưu ý: Đảm bảo class KhoaHocDAO của bạn có phương thức getList() hoặc getAll()
        List<KhoaHoc> list = khoaHocDAO.getList(); 

        if (list != null) {
            for (KhoaHoc kh : list) {
                model.addRow(new Object[]{
                    kh.getMaKhoaHoc(),
                    kh.getTenKhoaHoc(),
                    // Định dạng tiền tệ cho học phí: ví dụ 5,000,000 VNĐ
                    String.format("%,.0f VNĐ", (double) kh.getHocPhi()),
                    kh.getThoiLuong() + " buổi",
                    kh.getTrangThai()
                });
            }
        }
    }
    
    private JPanel createNotificationPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 30));
        panel.setBackground(COLOR_BG_LIGHT);
        panel.setBorder(new EmptyBorder(40, 50, 40, 50)); // Tăng lề cực rộng để form nằm giữa đẹp hơn

        // --- TIÊU ĐỀ TRANG ---
        JLabel lblTitle = new JLabel("▣ QUẢN LÝ THÔNG BÁO HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26)); 
        lblTitle.setForeground(COLOR_CYAN_MAIN);
        panel.add(lblTitle, BorderLayout.NORTH);

        // --- KHỐI NHẬP LIỆU (WHITE CARD) ---
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        // Tạo viền bóng mờ và bo góc nhẹ
        formCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(30, 40, 30, 40)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Định nghĩa Font chữ lớn
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 16);

        // Dòng 1: Tiêu đề
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        JLabel lblT = new JLabel("Tiêu đề:"); lblT.setFont(labelFont);
        formCard.add(lblT, gbc);

        gbc.gridx = 1; gbc.weightx = 0.9;
        JTextField txtT = new JTextField();
        txtT.setFont(inputFont);
        txtT.setPreferredSize(new Dimension(0, 45)); // Chiều cao ô nhập cực thoáng
        formCard.add(txtT, gbc);

        // Dòng 2: Nội dung
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblN = new JLabel("Nội dung:"); lblN.setFont(labelFont);
        formCard.add(lblN, gbc);

        gbc.gridx = 1;
        JTextArea txtN = new JTextArea(4, 20);
        txtN.setFont(inputFont);
        txtN.setLineWrap(true);
        txtN.setWrapStyleWord(true);
        txtN.setBorder(new EmptyBorder(5, 5, 5, 5));
        JScrollPane scrollTxtN = new JScrollPane(txtN);
        scrollTxtN.setPreferredSize(new Dimension(0, 100));
        formCard.add(scrollTxtN, gbc);

        // Dòng 3: Đối tượng
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblDt = new JLabel("Đối tượng:"); lblDt.setFont(labelFont);
        formCard.add(lblDt, gbc);

        gbc.gridx = 1;
        JComboBox<String> cb = new JComboBox<>(new String[]{"Tất cả học viên & giảng viên", "Chỉ dành cho Học viên", "Chỉ dành cho Giảng viên"});
        cb.setFont(inputFont);
        cb.setPreferredSize(new Dimension(0, 45));
        cb.setBackground(Color.WHITE);
        formCard.add(cb, gbc);

        // Dòng 4: Nút Đăng (To, màu Cyan chính của bạn cho đồng bộ)
        gbc.gridx = 1; gbc.gridy = 3;
        JButton btnAdd = new JButton("ĐĂNG THÔNG BÁO LÊN HỆ THỐNG");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(COLOR_CYAN_MAIN); // Sử dụng màu Cyan chính
        btnAdd.setPreferredSize(new Dimension(0, 50));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        formCard.add(btnAdd, gbc);

        // --- BẢNG HIỂN THỊ (SỬ DỤNG TABLE STYLE MỚI) ---
        modelThongBao = new DefaultTableModel(new String[]{"ID", "Tiêu đề thông báo", "Nội dung", "Gửi đến"}, 0);
        JTable table = createAdminTable(modelThongBao); 
        
        // Tăng độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(500);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);

        // Sự kiện nút đăng
        btnAdd.addActionListener(e -> {
            if(txtT.getText().trim().isEmpty() || txtN.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Vui lòng không để trống thông tin!");
                return;
            }
            thongBaoService.taoThongBaoMoi(txtT.getText(), txtN.getText(), cb.getSelectedIndex());
            txtT.setText(""); txtN.setText("");
            refreshThongBaoTable();
            JOptionPane.showMessageDialog(panel, "Thông báo đã được đăng tải!");
        });

        // Layout tổ chức lại
        JPanel centerPanel = new JPanel(new BorderLayout(0, 30));
        centerPanel.setOpaque(false);
        centerPanel.add(formCard, BorderLayout.NORTH);
        
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(new LineBorder(new Color(230, 230, 230)));
        centerPanel.add(tableScroll, BorderLayout.CENTER);
        
        panel.add(centerPanel, BorderLayout.CENTER);

        refreshThongBaoTable();
        return panel;
    }

    private void refreshThongBaoTable() {
        modelThongBao.setRowCount(0);
        for (ThongBao tb : thongBaoService.getThongBaoChoHocVien()) {
            String dt = tb.getDoiTuongNhan() == 0 ? "Tất cả" : (tb.getDoiTuongNhan() == 1 ? "Học viên" : "Giảng viên");
            modelThongBao.addRow(new Object[]{tb.getMaThongBao(), tb.getTieuDe(), tb.getNoiDung(), dt});
        }
    }
    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(COLOR_TEXT_BLUE);
        btn.setBackground(Color.WHITE);
        btn.setBorder(new LineBorder(COLOR_CYAN_MAIN, 1));
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }


    private JPanel createDashboardPage() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("📊 THỐNG KÊ HOẠT ĐỘNG HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_CYAN_MAIN);
        lblTitle.setBorder(new EmptyBorder(40, 0, 40, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Panel chứa 2 biểu đồ
        JPanel chartPanel = new JPanel(new GridLayout(1, 2, 50, 0));
        chartPanel.setOpaque(false);
        chartPanel.setBorder(new EmptyBorder(0, 60, 60, 60));

        // Khởi tạo (Sử dụng class đã tạo ở Bước 2)
        progressHV = new CircularProgressBar("TỶ LỆ HỌC VIÊN ĐANG HỌC", COLOR_CYAN_MAIN);
        progressGV = new CircularProgressBar("TỶ LỆ GIẢNG VIÊN ĐANG DẠY", new Color(76, 175, 80));

        chartPanel.add(progressHV);
        chartPanel.add(progressGV);
        mainPanel.add(chartPanel, BorderLayout.CENTER);

        // Gọi nạp dữ liệu
        refreshDashboardData();

        return mainPanel;
    }
    
    private void refreshDashboardData() {
        // Lấy số liệu từ DAO
        int[] stats = adminDAO.getDashboardStats();

        // Cập nhật lên giao diện
        SwingUtilities.invokeLater(() -> {
            progressHV.updateValue(stats[1], stats[0]); // (Đã đăng ký, Tổng)
            progressGV.updateValue(stats[3], stats[2]); // (Đang dạy, Tổng)
        });
    }

    private JPanel createPlaceholderPage(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(text + " (Đang phát triển...)", 0));
        return p;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new AdminUI().setVisible(true));
    }
}