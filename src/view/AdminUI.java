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
    private DefaultTableModel modelLH;
    
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

 // =========================================================================
    // 1. PHƯƠNG THỨC: TẠO TRANG QUẢN LÝ (Đã tối ưu sạch log)
    // =========================================================================
    private JPanel createEntityPage(String title, String type) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG_LIGHT);
        mainPanel.setName(type.equals("GV") ? "GiangVien" : "HocVien");

        // --- Thanh tiêu đề và công cụ ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(0, 70)); 
        headerPanel.setBorder(new EmptyBorder(15, 25, 10, 25)); 

        JLabel lblTitle = new JLabel(" QUẢN LÝ " + title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(COLOR_CYAN_MAIN);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        toolbar.setOpaque(false);
        
        String btnText = type.equals("GV") ? "Thêm Giảng Viên Mới" : "Thêm Học Viên Mới";
        JButton btnAddEntity = createStyledButton(btnText, new Color(40, 167, 69));
        btnAddEntity.setPreferredSize(new Dimension(190, 40)); 
        
        // Sự kiện click mở form popup nhập liệu
        btnAddEntity.addActionListener(e -> showAddEntityDialog(type));
        
        toolbar.add(btnAddEntity);
        headerPanel.add(toolbar, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
       
        // --- Lưới hiển thị danh sách các thẻ Card ---
        JPanel container = new JPanel(new GridLayout(0, 4, 15, 15));
        container.setBackground(COLOR_BG_LIGHT);
        container.setBorder(new EmptyBorder(10, 25, 25, 25)); 

        try {
            if (type.equals("GV")) {
                List<Object[]> ds = giangVienDAO.getGiangVienFullInfo(); 
                if (ds != null) {
                    for (Object[] data : ds) container.add(new ProfileCard(data, "GV"));
                }
            } else {
                List<Object[]> ds = hocVienDAO.getHocVienFullInfo();
                if (ds != null) {
                    for (Object[] data : ds) container.add(new ProfileCard(data, "HV"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            container.add(new JLabel("Đã xảy ra lỗi khi nạp dữ liệu từ cơ sở dữ liệu!"));
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(COLOR_BG_LIGHT);
        wrapper.add(container, BorderLayout.NORTH); 

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scroll, BorderLayout.CENTER);

        return mainPanel;
    }

    // =========================================================================
    // 2. PHƯƠNG THỨC: POPUP FORM THÊM MỚI ĐA NĂNG (Đã tối ưu sạch log)
    // =========================================================================
    private void showAddEntityDialog(String type) {
        boolean isGV = type.equals("GV");
        JDialog dialog = new JDialog(this, isGV ? "Đăng ký giảng viên mới" : "Đăng ký học viên mới", true);
        dialog.setSize(500, 680);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(0, 2, 10, 15));
        p.setBorder(new EmptyBorder(25, 30, 25, 30));

        JTextField tMaDoiTuong = new JTextField(); 
        JTextField tMaND = new JTextField();
        JTextField tTen = new JTextField();
        JTextField tEmail = new JTextField();
        JTextField tSdt = new JTextField();
        JTextField tNS = new JTextField();
        JTextField tQue = new JTextField();
        JComboBox<String> cbGT = new JComboBox<>(new String[]{"Nam", "Nữ"});
        
        JTextField tExtra1 = new JTextField(); 
        JTextField tExtra2 = new JTextField(); 
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Đang học", "Bảo lưu", "Đã tốt nghiệp"}); 

        p.add(new JLabel(isGV ? "Mã giảng viên (VD: GV001):" : "Mã học viên (VD: HV001):")); p.add(tMaDoiTuong);
        p.add(new JLabel("Mã tài khoản (VD: ND001):")); p.add(tMaND);
        p.add(new JLabel("Họ và tên:")); p.add(tTen);
        p.add(new JLabel("Email liên hệ:")); p.add(tEmail);
        p.add(new JLabel("Số điện thoại:")); p.add(tSdt);
        p.add(new JLabel("Ngày sinh (yyyy-mm-dd):")); p.add(tNS);
        p.add(new JLabel("Giới tính:")); p.add(cbGT);
        p.add(new JLabel("Quê quan:")); p.add(tQue);

        if (isGV) {
            p.add(new JLabel("Học vị (Thạc sĩ, Tiến sĩ...):")); p.add(tExtra1);
            p.add(new JLabel("Chuyên môn giảng dạy:")); p.add(tExtra2);
        } else {
            p.add(new JLabel("Trạng thái học tập:")); p.add(cbStatus);
        }

        JButton btnSave = new JButton("XÁC NHẬN THÊM MỚI");
        btnSave.setPreferredSize(new Dimension(0, 55));
        btnSave.setBackground(new Color(40, 167, 69)); 
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        
        btnSave.addActionListener(e -> {
            if (tMaDoiTuong.getText().trim().isEmpty() || tMaND.getText().trim().isEmpty() || tTen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin bắt buộc!");
                return;
            }

            try {
                NguoiDung nd = new NguoiDung();
                nd.setMaNguoiDung(tMaND.getText().trim());
                nd.setHoTen(tTen.getText().trim());
                nd.setEmail(tEmail.getText().trim());
                nd.setSoDienThoai(tSdt.getText().trim());
                nd.setNgaySinh(tNS.getText().trim());
                nd.setQueQuan(tQue.getText().trim());
                nd.setGioiTinh(cbGT.getSelectedItem().toString());

                if (isGV) {
                    GiangVien gv = new GiangVien();
                    gv.setMaGV(tMaDoiTuong.getText().trim());
                    gv.setMaNguoiDung(tMaND.getText().trim());
                    gv.setHocVi(tExtra1.getText().trim());
                    gv.setChuyenMon(tExtra2.getText().trim());
                    
                    if (nguoiDungDAO.insert(nd)) {
                        giangVienDAO.insert(gv);
                        JOptionPane.showMessageDialog(dialog, "Thêm giảng viên thành công!");
                        dialog.dispose();
                        refreshEntityPage("GV");
                    }
                } else {
                    HocVien hv = new HocVien();
                    hv.setMaHV(tMaDoiTuong.getText().trim()); 
                    hv.setMaNguoiDung(tMaND.getText().trim());
                    hv.setTrangThai(cbStatus.getSelectedItem().toString());
                    
                    if (nguoiDungDAO.insert(nd)) { 
                        hocVienDAO.insert(hv); 
                        JOptionPane.showMessageDialog(dialog, "Thêm học viên thành công!");
                        dialog.dispose();
                        refreshEntityPage("HV");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Lỗi khi lưu dữ liệu: " + ex.getMessage());
            }
        });

        dialog.add(new JScrollPane(p), BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
 // =========================================================================
    // HÀM BỔ SUNG: LÀM MỚI VÀ ĐỒNG BỘ ĐỒ HỌA TRANG CARD LAYOUT
    // =========================================================================
    public void refreshEntityPage(String type) {
        String cardName = type.equals("GV") ? "GiangVien" : "HocVien";
        String title = type.equals("GV") ? "GIẢNG VIÊN" : "HỌC VIÊN";
        
        // Quét tìm tấm Panel cũ dựa trên Name để xóa bỏ an toàn, tránh dùng index cứng
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof JPanel && cardName.equals(comp.getName())) {
                cardPanel.remove(comp);
                break;
            }
        }
        
        // Dựng lại trang mới tinh chứa dữ liệu vừa cập nhật từ Database
        cardPanel.add(createEntityPage(title, type), cardName);
        cardPanel.revalidate();
        cardPanel.repaint();
        cardLayout.show(cardPanel, cardName);
    }
    // ================= LỚP Ô THÔNG TIN (PROFILE CARD) =================
 // ================= LỚP Ô THÔNG TIN (PROFILE CARD) =================
    class ProfileCard extends JPanel {
        public ProfileCard(Object[] data, String type) {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(new LineBorder(new Color(235, 235, 235), 1));
            setPreferredSize(new Dimension(280, 180));
            
            // Phần nội dung chữ thông tin cá nhân
            JPanel body = new JPanel();
            body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
            body.setOpaque(false);
            body.setBorder(new EmptyBorder(15, 15, 10, 15)); 

            JLabel name = new JLabel(data[1].toString());
            name.setFont(new Font("Segoe UI", Font.BOLD, 16));
            name.setForeground(new Color(33, 33, 33));
            
            body.add(name);
            body.add(Box.createVerticalStrut(10)); 

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

            // Cấu trúc thanh nút bấm bên dưới đáy card gồm 2 nút cho cả GV và HV
            JPanel actionPanel = new JPanel(new GridLayout(1, 2));
            actionPanel.setPreferredSize(new Dimension(0, 35));

            // Nút Chi tiết
            JButton btnDetail = new JButton("Chi tiết →");
            btnDetail.setBackground(COLOR_CYAN_MAIN);
            btnDetail.setForeground(Color.WHITE);
            btnDetail.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnDetail.setFocusPainted(false);
            btnDetail.setBorderPainted(false);
            btnDetail.setOpaque(true);
            btnDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDetail.addActionListener(e -> showFullDetailPopup(data, type));
            actionPanel.add(btnDetail);

            // Nút Xóa bỏ (Dùng chung linh hoạt)
            JButton btnDelete = new JButton("Xóa bỏ ✖");
            btnDelete.setBackground(new Color(220, 53, 69)); 
            btnDelete.setForeground(Color.WHITE);
            btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnDelete.setFocusPainted(false);
            btnDelete.setBorderPainted(false);
            btnDelete.setOpaque(true);
            btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
         // Sự kiện Xóa bỏ (Đã sửa lỗi không tự mất thẻ Card)
            btnDelete.addActionListener(e -> {
                String id = data[0].toString(); // MaGV hoặc MaHV
                String nameTarget = data[1].toString();
                String maND = type.equals("GV") ? data[9].toString() : data[8].toString(); 
                
                String labelConfirm = type.equals("GV") ? "giảng viên" : "học viên";
                int confirm = JOptionPane.showConfirmDialog(this, 
                        "Bạn có chắc chắn muốn xóa " + labelConfirm + " [" + nameTarget + "] hoàn toàn khỏi hệ thống?", 
                        "Xác nhận hành động xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleteSuccess = false;
                    
                    // Thực thi xóa dưới Database
                    if (type.equals("GV")) {
                        deleteSuccess = giangVienDAO.delete(id);
                    } else {
                        deleteSuccess = hocVienDAO.delete(id);
                    }
                    
                    if (deleteSuccess) {
                        nguoiDungDAO.delete(maND); // Xóa nốt bản ghi ở bảng NguoiDung cha
                        JOptionPane.showMessageDialog(this, "Đã thực hiện xóa thành công!");
                        
                        // 👉 SỬA TẠI ĐÂY: Ép AdminUI xóa trang cũ, nạp lại dữ liệu mới từ CSDL
                        refreshEntityPage(type); 
                    }
                }
            });
            actionPanel.add(btnDelete);

            add(actionPanel, BorderLayout.SOUTH);
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

        if (nd == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin chi tiết (Lỗi đồng bộ dữ liệu)!");
            return;
        }

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
            tExtra1.setText(gv != null ? gv.getHocVi() : ""); tExtra2.setText(gv != null ? gv.getChuyenMon() : "");
            p.add(new JLabel("Học vị:")); p.add(tExtra1);
            p.add(new JLabel("Chuyên môn:")); p.add(tExtra2);
        } else {
            HocVien hv = hocVienDAO.findById(data[0].toString());
            tExtra1.setText(hv != null ? hv.getTrangThai() : "");
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
                    if (gv != null) {
                        gv.setHocVi(tExtra1.getText()); gv.setChuyenMon(tExtra2.getText());
                        giangVienDAO.update(gv);
                    }
                } else {
                    HocVien hv = hocVienDAO.findById(data[0].toString());
                    if (hv != null) {
                        hv.setTrangThai(tExtra1.getText()); hocVienDAO.update(hv);
                    }
                }
                JOptionPane.showMessageDialog(dialog, "Đã cập nhật thông tin thành công!");
                dialog.dispose();
                // Tải lại nội dung component để hiển thị dữ liệu mới
                cardPanel.add(createEntityPage(type.equals("GV") ? "GIẢNG VIÊN" : "HỌC VIÊN", type), type.equals("GV") ? "GiangVien" : "HocVien");
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

        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC KHÓA HỌC");
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

        // Load dữ liệu từ DAO
        loadDataToTableKH(modelKH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Sự kiện nút Thêm
        btnAdd.addActionListener(e -> showKhoaHocDialog(null, modelKH));

        // Sự kiện nút Sửa
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa học cần sửa!");
                return;
            }
            String maKH = table.getValueAt(row, 0).toString();
            KhoaHoc kh = khoaHocDAO.getById(maKH);
            // Sửa lỗi: Cập nhật lại UI sau khi nhấn Sửa Thông Tin
            if (kh != null) {
                showKhoaHocDialog(kh, modelKH);
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy khóa học trong Database!");
            }
        });

        // Sự kiện nút Xóa
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa học cần xóa!");
                return;
            }
            String maKH = table.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khóa học " + maKH + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                khoaHocDAO.delete(maKH);
                JOptionPane.showMessageDialog(this, "Đã xóa khóa học thành công!");
                loadDataToTableKH(modelKH);
            }
        });

        return mainPanel;
    }

    private JPanel createLopHocPage() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(COLOR_BG_LIGHT);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Header & Toolbar ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ DANH SÁCH LỚP HỌC");
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
        
        // SỬA TẠI ĐÂY: Gán trực tiếp vào biến toàn cục, KHÔNG khai báo "DefaultTableModel modelLH =" nữa
        modelLH = new DefaultTableModel(cols, 0); 
        
        JTable table = createAdminTable(modelLH); 

        // Tùy chỉnh độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);

        loadDataToTableLH(modelLH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Sự kiện Mở lớp mới (Truyền modelLH toàn cục vào)
        btnAdd.addActionListener(e -> showLopHocDialog(null, modelLH));

        // Sự kiện Sửa lớp học
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Vui lòng chọn một lớp học trên bảng để sửa!");
                return;
            }
            Object[] selectedData = new Object[7];
            for (int i = 0; i < 7; i++) {
                selectedData[i] = table.getValueAt(selectedRow, i);
            }
            showLopHocDialog(selectedData, modelLH);
        });

        // Sự kiện Xóa lớp học
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Vui lòng chọn một lớp học trên bảng trước khi xóa!");
                return;
            }
            String maLop = table.getValueAt(selectedRow, 0).toString();
            String tenLop = table.getValueAt(selectedRow, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(mainPanel, 
                    "Bạn có chắc chắn muốn xóa lớp [" + tenLop + "] không?", 
                    "Xác nhận xóa lớp học", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                lopHocDAO.delete(maLop);
                loadDataToTableLH(modelLH); // Gọi làm mới dữ liệu
                JOptionPane.showMessageDialog(mainPanel, "Đã xóa lớp học thành công!");
            }
        });

        return mainPanel;
    }
 // Hàm phụ trợ: Xóa trắng và nạp lại Giảng viên theo Mã khóa học được chọn
    private void updateGiangVienComboBox(String maKH, JComboBox<String> cbGiangVien) {
        cbGiangVien.removeAllItems(); // Xóa sạch danh sách cũ
        
        // Bước 1: Thử lọc giảng viên theo đúng chuyên môn môn học
        List<Object[]> dsGV = giangVienDAO.getGiangVienByKhoaHoc(maKH);
        
        // Bước 2: BẪY DỰ PHÒNG (Fallback)
        // Nếu trong Database chưa có giảng viên nào có chữ chuyên môn khớp với tên khóa học,
        // ta tự động lấy TOÀN BỘ danh sách giảng viên để tránh bị rỗng dữ liệu gây lỗi hiển thị bảng.
        if (dsGV == null || dsGV.isEmpty()) {
            dsGV = giangVienDAO.getGiangVienFullInfo();
        }
        
        // Nạp dữ liệu chuẩn vào ComboBox (Luôn đảm bảo định dạng: "Mã - Tên")
        if (dsGV != null && !dsGV.isEmpty()) {
            for (Object[] gv : dsGV) {
                cbGiangVien.addItem(gv[0].toString() + " - " + gv[1].toString());
            }
        } else {
            cbGiangVien.addItem("Hệ thống chưa có giảng viên nào");
        }
    }

    private void showLopHocDialog(Object[] data, DefaultTableModel modelLH) {
        boolean isEdit = (data != null);
        JDialog dialog = new JDialog(this, isEdit ? "Chỉnh sửa thông tin lớp học" : "Thiết lập lớp học mới", true);
        dialog.setSize(480, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(0, 1, 5, 8));
        p.setBorder(new EmptyBorder(20, 30, 20, 30));

        JTextField tMaLop = new JTextField();
        JTextField tTenLop = new JTextField();
        JComboBox<String> cbKhoaHoc = new JComboBox<>();
        JComboBox<String> cbGiangVien = new JComboBox<>(); // Để trống để nạp động hoàn toàn, KHÔNG dùng vòng lặp getGiangVienFullInfo() ở đây nữa
        JComboBox<String> cbCa = new JComboBox<>(new String[]{"sang", "toi"});
        JTextField tLich = new JTextField("2-4-6");
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Chưa bắt đầu", "Đang diễn ra", "Đã kết thúc"});

        // ĐĂNG KÝ SỰ KIỆN LẮNG NGHE TRƯỚC KHI NẠP DỮ LIỆU
        cbKhoaHoc.addActionListener(e -> {
            if (cbKhoaHoc.getSelectedItem() != null) {
                String maKH = cbKhoaHoc.getSelectedItem().toString().split(" - ")[0];
                updateGiangVienComboBox(maKH, cbGiangVien);
            }
        });

        // Bây giờ mới nạp danh mục Khóa học vào (Mỗi phần tử được thêm sẽ tự kích hoạt sự kiện lọc ở trên)
        for (KhoaHoc kh : khoaHocDAO.getAll()) {
            cbKhoaHoc.addItem(kh.getMaKhoaHoc() + " - " + kh.getTenKhoaHoc());
        }

        // Ép buộc nạp dữ liệu Giảng viên cho khóa học đầu tiên hiển thị ngay khi mở form
        if (cbKhoaHoc.getItemCount() > 0) {
            String maKHBanDau = cbKhoaHoc.getItemAt(0).split(" - ")[0];
            updateGiangVienComboBox(maKHBanDau, cbGiangVien);
        }

        // Giao diện sắp xếp các ô nhập liệu
        p.add(new JLabel("Mã lớp học:")); p.add(tMaLop);
        p.add(new JLabel("Tên lớp học:")); p.add(tTenLop);
        p.add(new JLabel("Chọn Khóa học:")); p.add(cbKhoaHoc);
        p.add(new JLabel("Chọn Giảng viên đảm nhiệm:")); p.add(cbGiangVien);
        p.add(new JLabel("Ca học:")); p.add(cbCa);
        p.add(new JLabel("Lịch học (VD: 2-4-6):")); p.add(tLich);
        p.add(new JLabel("Trạng thái lớp:")); p.add(cbStatus);

        // NẾU LÀ CHẾ ĐỘ SỬA: Đổ dữ liệu cũ lên form
        if (isEdit) {
            tMaLop.setText(data[0].toString());
            tMaLop.setEditable(false); 
            tTenLop.setText(data[1].toString());
            
            // Chọn Khóa học cũ -> Tự động kích hoạt đổi danh sách giảng viên trong ComboBox
            for (int i = 0; i < cbKhoaHoc.getItemCount(); i++) {
                if (cbKhoaHoc.getItemAt(i).contains(data[2].toString())) {
                    cbKhoaHoc.setSelectedIndex(i);
                    break;
                }
            }
            
            // Chọn đúng Giảng viên đang đảm nhiệm lớp này
            for (int i = 0; i < cbGiangVien.getItemCount(); i++) {
                if (cbGiangVien.getItemAt(i).contains(data[3].toString())) {
                    cbGiangVien.setSelectedIndex(i);
                    break;
                }
            }
            
            cbCa.setSelectedItem(data[4].toString().toLowerCase());
            tLich.setText(data[5].toString());
            cbStatus.setSelectedItem(data[6].toString());
        }

        JButton btnSave = createStyledButton("LƯU THÔNG TIN HỆ THỐNG", new Color(40, 167, 69));
        btnSave.addActionListener(e -> {
            // Kiểm tra trống các ô text cơ bản
            if (tMaLop.getText().trim().isEmpty() || tTenLop.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            
            // Kiểm tra xem đã chọn giảng viên hợp lệ chưa
            if (cbGiangVien.getSelectedItem() == null || 
                cbGiangVien.getSelectedItem().toString().contains("chưa có giảng viên")) {
                JOptionPane.showMessageDialog(dialog, "Không thể lưu lớp khi chưa chọn giảng viên đảm nhiệm!");
                return;
            }

            String maLop = tMaLop.getText().trim();
            String tenLop = tTenLop.getText().trim();
            
            // Tách chuỗi chuẩn: bốc chuẩn xác Mã khóa học và Mã giảng viên (Ví dụ: "GV01") để nạp xuống DB
            String maKH = cbKhoaHoc.getSelectedItem().toString().split(" - ")[0];
            String maGV = cbGiangVien.getSelectedItem().toString().split(" - ")[0];
            
            String caHoc = cbCa.getSelectedItem().toString();
            String lichHoc = tLich.getText().trim();
            String trangThai = cbStatus.getSelectedItem().toString();

            LopHoc lh = new LopHoc(maLop, tenLop, maKH, maGV, caHoc, lichHoc, trangThai);
            
            // Nạp học phí & thời lượng từ khóa học tương ứng
            KhoaHoc khSelected = khoaHocDAO.getById(maKH);
            if (khSelected != null) {
                lh.setHocPhi(khSelected.getHocPhi());
                lh.setThoiLuong(khSelected.getThoiLuong());
            }

            if (isEdit) {
                lopHocDAO.update(lh);
                JOptionPane.showMessageDialog(dialog, "Đã cập nhật thông tin lớp học thành công!");
            } else {
                if (lopHocDAO.getById(maLop) != null) {
                    JOptionPane.showMessageDialog(dialog, "Mã lớp học này đã tồn tại!");
                    return;
                }
                lopHocDAO.insert(lh);
                JOptionPane.showMessageDialog(dialog, "Đã mở lớp học mới thành công!");
            }

            loadDataToTableLH(modelLH); // Gọi nạp lại và ép JTable render lại đồ họa mới
            dialog.dispose();
        });

        dialog.add(new JScrollPane(p), BorderLayout.CENTER);
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
                     new FullUI().setVisible(true); 
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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // KÍCH HOẠT LUỒNG TẢI DỮ LIỆU THỜI GIAN THỰC KHI CLICK MENU
        btn.addActionListener(e -> {
            if (cardName.equals("HocVien")) {
                refreshEntityPage("HV");
            } else if (cardName.equals("GiangVien")) {
                refreshEntityPage("GV");
            } else if (cardName.equals("LopHoc")) {
                loadDataToTableLH(modelLH);
                cardLayout.show(cardPanel, cardName);
            } else if (cardName.equals("KhoaHoc")) {
                loadDataToTableKH(modelThongBao); // Nếu cần làm mới bảng khóa học
                cardLayout.show(cardPanel, cardName);
            } else {
                cardLayout.show(cardPanel, cardName);
            }
        });
        return btn;
    }
    
    private void loadDataToTableLH(DefaultTableModel model) {
        if (model == null) return;
        
        model.setRowCount(0); // Xóa trắng dữ liệu cũ hiển thị trên lưới JTable
        List<Object[]> list = lopHocDAO.getLopHocFullInfo();
        for (Object[] row : list) {
            model.addRow(row);
        }
        
        // Thêm dòng này để ép các thành phần giao diện chứa bảng vẽ lại dữ liệu mới nạp
        if (cardPanel != null) {
            cardPanel.revalidate();
            cardPanel.repaint();
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
    
    private void showKhoaHocDialog(KhoaHoc existingKH, DefaultTableModel model) {
        boolean isEdit = (existingKH != null);
        String dialogTitle = isEdit ? "Sửa thông tin khóa học" : "Thêm khóa học mới";
        JDialog dialog = new JDialog(this, dialogTitle, true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 10));
        p.setBorder(new EmptyBorder(20, 30, 20, 30));

        JTextField tMa = new JTextField();
        JTextField tTen = new JTextField();
        JTextField tPhi = new JTextField();
        JTextField tTime = new JTextField();
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Hoạt động", "Tạm dừng"});

        p.add(new JLabel("Mã khóa học:")); p.add(tMa);
        p.add(new JLabel("Tên khóa học:")); p.add(tTen);
        p.add(new JLabel("Học phí (VNĐ):")); p.add(tPhi);
        p.add(new JLabel("Thời lượng (buổi):")); p.add(tTime);
        p.add(new JLabel("Trạng thái:")); p.add(cbStatus);

        if (isEdit) {
            tMa.setText(existingKH.getMaKhoaHoc());
            tMa.setEditable(false); // Không cho sửa mã
            tMa.setBackground(new Color(240, 240, 240));
            tTen.setText(existingKH.getTenKhoaHoc());
            // Format học phí để không hiện định dạng exponential, ví dụ: 5.0E7 -> 50000000
            tPhi.setText(String.format("%.0f", existingKH.getHocPhi()));
            tTime.setText(String.valueOf(existingKH.getThoiLuong()));
            cbStatus.setSelectedItem(existingKH.getTrangThai());
        }

        JButton btnSave = createStyledButton("XÁC NHẬN LƯU", new Color(40, 167, 69));
        btnSave.addActionListener(e -> {
            try {
                String ma = tMa.getText().trim();
                String ten = tTen.getText().trim();
                double phi = Double.parseDouble(tPhi.getText().trim());
                int time = Integer.parseInt(tTime.getText().trim());
                String status = cbStatus.getSelectedItem().toString();

                if (ma.isEmpty() || ten.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                KhoaHoc kh = new KhoaHoc(ma, ten, phi, time, status);
                if (isEdit) {
                    khoaHocDAO.update(kh);
                    JOptionPane.showMessageDialog(dialog, "Cập nhật khóa học thành công!");
                } else {
                    KhoaHoc checkExist = khoaHocDAO.getById(ma);
                    if (checkExist != null) {
                        JOptionPane.showMessageDialog(dialog, "Mã khóa học đã tồn tại!");
                        return;
                    }
                    khoaHocDAO.create(kh);
                    JOptionPane.showMessageDialog(dialog, "Thêm khóa học thành công!");
                }
                loadDataToTableKH(model);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Học phí và thời lượng phải là số hợp lệ!");
            }
        });

        dialog.add(p, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void loadDataToTableKH(DefaultTableModel model) {
        // Xóa dữ liệu cũ trong bảng để tránh bị trùng lặp khi refresh
        model.setRowCount(0); 

        // Lấy danh sách khóa học từ DAO
        List<KhoaHoc> list = khoaHocDAO.getList(); 

        if (list != null) {
            for (KhoaHoc kh : list) {
                model.addRow(new Object[]{
                    kh.getMaKhoaHoc(),
                    kh.getTenKhoaHoc(),
                    kh.getHocPhi(), // Giữ nguyên số để khi edit dễ parse
                    kh.getThoiLuong(),
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