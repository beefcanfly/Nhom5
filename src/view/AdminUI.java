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
    private DefaultTableModel modelKH;
    
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

 
    //  PHƯƠNG THỨC: TẠO TRANG QUẢN LÝ THẺ PROFILE CARD
    private JPanel createEntityPage(String title, String type) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG_LIGHT);
        mainPanel.setName(type.equals("GV") ? "GiangVien" : "HocVien");

        // --- Thanh tiêu đề và thanh công cụ (Nút Thêm Mới) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(0, 70)); 
        headerPanel.setBorder(new EmptyBorder(15, 25, 10, 25)); 

        JLabel lblTitle = new JLabel("  QUẢN LÝ " + title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(COLOR_CYAN_MAIN);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        toolbar.setOpaque(false);
        
        String btnText = type.equals("GV") ? "Thêm Giảng Viên Mới" : "Thêm Học Viên Mới";
        JButton btnAddEntity = createStyledButton(btnText, new Color(40, 167, 69));
        btnAddEntity.setPreferredSize(new Dimension(190, 40)); 
        btnAddEntity.setOpaque(true);
        
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
                if (ds != null && !ds.isEmpty()) {
                    for (Object[] data : ds) container.add(new ProfileCard(data, "GV"));
                } else {
                    container.add(new JLabel("Chưa có dữ liệu Giảng Viên trong hệ thống."));
                }
            } else {
                List<Object[]> ds = hocVienDAO.getHocVienFullInfo();
                if (ds != null && !ds.isEmpty()) {
                    for (Object[] data : ds) container.add(new ProfileCard(data, "HV"));
                } else {
                    container.add(new JLabel("Chưa có dữ liệu Học Viên trong hệ thống."));
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


    // PHƯƠNG THỨC: POPUP FORM THÊM MỚI ĐA NĂNG 
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
        JTextField tNS = new JTextField("1995-01-01"); 
        JTextField tQue = new JTextField();
        JComboBox<String> cbGT = new JComboBox<>(new String[]{"Nam", "Nữ"});
        
        JTextField tExtra1 = new JTextField(); 
        JTextField tExtra2 = new JTextField(); 
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{ "active", "inactive"}); 

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

        JButton btnSave = createStyledButton("XÁC NHẬN THÊM MỚI", new Color(40, 167, 69));
        btnSave.setPreferredSize(new Dimension(0, 55));
        btnSave.setOpaque(true);
        
        btnSave.addActionListener(e -> {
            String emailText = tEmail.getText().trim();
            String sdtText = tSdt.getText().trim();
            String ngaySinhText = tNS.getText().trim();

            if (tMaDoiTuong.getText().trim().isEmpty() || tMaND.getText().trim().isEmpty() || tTen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin bắt buộc!");
                return;
            }

            // Bộ lọc bẫy định dạng dữ liệu (Regex)
            String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
            String sdtPattern = "^0\\d{9}$"; 
            String datePattern = "^\\d{4}-\\d{2}-\\d{2}$"; 

            if (!emailText.isEmpty() && !emailText.matches(emailPattern)) {
                JOptionPane.showMessageDialog(dialog, "Nhập sai định dạng Email! (Ví dụ: abc@gmail.com)");
                return;
            }
            if (!sdtText.isEmpty() && !sdtText.matches(sdtPattern)) {
                JOptionPane.showMessageDialog(dialog, "Nhập sai định dạng Số điện thoại! (Phải đủ 10 số và bắt đầu bằng số 0)");
                return;
            }
            if (!ngaySinhText.isEmpty() && !ngaySinhText.matches(datePattern)) {
                JOptionPane.showMessageDialog(dialog, "Nhập sai định dạng Ngày sinh! (Yêu cầu cấu trúc mẫu: yyyy-mm-dd)");
                return;
            }

            try {
                NguoiDung nd = new NguoiDung();
                nd.setMaNguoiDung(tMaND.getText().trim());
                nd.setHoTen(tTen.getText().trim());
                nd.setEmail(emailText);
                nd.setSoDienThoai(sdtText);
                nd.setNgaySinh(ngaySinhText);
                nd.setQueQuan(tQue.getText().trim());
                nd.setGioiTinh(cbGT.getSelectedItem().toString());

                if (nguoiDungDAO.insert(nd)) {
                    if (isGV) {
                        GiangVien gv = new GiangVien();
                        gv.setMaGV(tMaDoiTuong.getText().trim());
                        gv.setMaNguoiDung(tMaND.getText().trim());
                        gv.setHocVi(tExtra1.getText().trim());
                        gv.setChuyenMon(tExtra2.getText().trim());
                        giangVienDAO.insert(gv);
                        JOptionPane.showMessageDialog(dialog, "Thêm giảng viên thành công!");
                    } else {
                        HocVien hv = new HocVien();
                        hv.setMaHV(tMaDoiTuong.getText().trim()); 
                        hv.setMaNguoiDung(tMaND.getText().trim());
                        hv.setTrangThai(cbStatus.getSelectedItem().toString());
                        hocVienDAO.insert(hv);
                        JOptionPane.showMessageDialog(dialog, "Thêm học viên thành công!");
                    }
                    dialog.dispose();
                    refreshEntityPage(type);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Mã tài khoản (Mã người dùng) này đã tồn tại trên hệ thống!");
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

    // HÀM BỔ SUNG: CẬP NHẬT ĐỒ HỌA THỜI GIAN THỰC TỪ CƠ SỞ DỮ LIỆU
    public void refreshEntityPage(String type) {
        // Đồng bộ chuẩn tên CardLayout giống với lúc bạn add vào ban đầu ở hàm khởi tạo
        String cardLayoutName = type.equals("GV") ? "GiangVien" : "HocVien";
        String title = type.equals("GV") ? "GIẢNG VIÊN" : "HỌC VIÊN";
        
        // Duyệt tìm tấm Panel trang cũ dựa trên cấu trúc đặt tên để gỡ bỏ khỏi bộ nhớ đệm
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof JPanel && cardLayoutName.equals(comp.getName())) {
                cardPanel.remove(comp);
                break;
            }
        }
        
        // Dựng lại một trang mới tinh vừa quét CSDL MySQL mới nhất
        JPanel newPage = createEntityPage(title, type);
        newPage.setName(cardLayoutName); // Ép tên định danh trùng khớp hoàn toàn để lần xóa sau không bị lỗi
        
        cardPanel.add(newPage, cardLayoutName);
        cardPanel.revalidate();
        cardPanel.repaint();
        
        // Lật giao diện hiển thị trang mới lên màn hình
        cardLayout.show(cardPanel, cardLayoutName);
    }

    
    //  LỚP Ô THÔNG TIN (PROFILE CARD ĐÃ ĐỒNG BỘ HIỂN THỊ MÃ HV / MÃ GV)

    class ProfileCard extends JPanel {
        public ProfileCard(Object[] data, String type) {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(new LineBorder(new Color(235, 235, 235), 1));
            // 👉 TĂNG CHIỀU CAO LÊN 220: Đảm bảo không bị che khuất chữ do thêm dòng mới
            setPreferredSize(new Dimension(280, 220));

            JPanel body = new JPanel();
            body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
            body.setOpaque(false);
            body.setBorder(new EmptyBorder(15, 15, 10, 15)); 

            // Dòng 1: Tên đối tượng (In đậm)
            JLabel name = new JLabel(data[1] != null ? data[1].toString() : "Unknown");
            name.setFont(new Font("Segoe UI", Font.BOLD, 16));
            name.setForeground(new Color(33, 33, 33));
            body.add(name);
            body.add(Box.createVerticalStrut(8)); 

            Font infoFont = new Font("Segoe UI", Font.PLAIN, 13);
            
            // Dòng 2: HIỂN THỊ MÃ HỌC VIÊN / MÃ GIẢNG VIÊN (Ép kiểu hiển thị rõ ràng)
            String textCode = "";
            if (type.equals("GV")) {
                textCode = "Mã GV: " + (data[0] != null ? data[0].toString() : "");
            } else {
                textCode = "Mã HV: " + (data[0] != null ? data[0].toString() : "");
            }
            JLabel lblCode = new JLabel("• " + textCode);
            lblCode.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Cho mã in đậm lên cho dễ nhìn
            lblCode.setForeground(COLOR_CYAN_MAIN.darker()); // Đổi màu xanh đậm nổi bật
            body.add(lblCode);
            body.add(Box.createVerticalStrut(5));

            // Dòng 3: Email
            JLabel lblEmail = new JLabel("• Email: " + (data[2] != null ? data[2] : ""));
            lblEmail.setFont(infoFont);
            body.add(lblEmail);
            body.add(Box.createVerticalStrut(5));

            // Dòng 4: Số điện thoại
            JLabel lblSdt = new JLabel("• SĐT: " + (data[3] != null ? data[3] : ""));
            lblSdt.setFont(infoFont);
            body.add(lblSdt);
            body.add(Box.createVerticalStrut(5));

            // Dòng 5: Trạng thái (HV) hoặc Chuyên môn (GV)
            String extraInfo = type.equals("GV") ? "Chuyên môn: " + (data[5] != null ? data[5] : "") : "Trạng thái: " + (data[4] != null ? data[4] : "");
            JLabel lblExtra = new JLabel("• " + extraInfo);
            lblExtra.setFont(infoFont);
            body.add(lblExtra);

            add(body, BorderLayout.CENTER);

            // Thanh chức năng đáy gồm 2 nút chia đôi đều đặn
            JPanel actionPanel = new JPanel(new GridLayout(1, 2));
            actionPanel.setPreferredSize(new Dimension(0, 35));

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

            JButton btnDelete = new JButton("Xóa bỏ ✖");
            btnDelete.setBackground(new Color(220, 53, 69)); 
            btnDelete.setForeground(Color.WHITE);
            btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnDelete.setFocusPainted(false);
            btnDelete.setBorderPainted(false);
            btnDelete.setOpaque(true);
            btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btnDelete.addActionListener(e -> {
                String id = data[0].toString(); 
                String nameTarget = data[1].toString();
                String maND = type.equals("GV") ? data[9].toString() : data[8].toString(); 
                
                String labelConfirm = type.equals("GV") ? "giảng viên" : "học viên";
                int confirm = JOptionPane.showConfirmDialog(this, 
                        "Bạn có chắc chắn muốn xóa " + labelConfirm + " [" + nameTarget + "] hoàn toàn khỏi hệ thống?", 
                        "Xác nhận hành động xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        boolean deleteSuccess = type.equals("GV") ? giangVienDAO.delete(id) : hocVienDAO.delete(id);
                        
                        if (deleteSuccess) {
                            nguoiDungDAO.delete(maND); 
                            JOptionPane.showMessageDialog(this, "Đã thực hiện xóa thành công!");
                            refreshEntityPage(type); 
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        if (ex.getMessage() != null && ex.getMessage().contains("foreign key constraint fails")) {
                            JOptionPane.showMessageDialog(this, 
                                "Không thể xóa! Đối tượng này đang có lịch học/lớp học liên kết trên hệ thống.\nVui lòng gỡ bỏ lịch học trước.", 
                                "Lỗi ràng buộc dữ liệu", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi hệ thống: " + ex.getMessage());
                        }
                    }
                }
            });
            actionPanel.add(btnDelete);

            add(actionPanel, BorderLayout.SOUTH);
        }
    }
    
    //  POPUP HIỂN THỊ TOÀN BỘ THÔNG TIN & LƯU THAY ĐỔI ĐỒNG BỘ
    private void showFullDetailPopup(Object[] data, String type) {
        JDialog dialog = new JDialog(this, "Thông tin chi tiết", true);
        dialog.setSize(500, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(0, 2, 10, 15));
        p.setBorder(new EmptyBorder(25, 30, 25, 30));

        String maND = (type.equals("GV")) ? data[9].toString() : data[8].toString();
        NguoiDung nd = nguoiDungDAO.findById(maND);

        if (nd == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin chi tiết (Lỗi đồng bộ dữ liệu)!");
            return;
        }

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
        p.add(new JLabel("Quê quan:")); p.add(tQue);

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
        btnSave.setBackground(new Color(40, 167, 69)); 
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
                refreshEntityPage(type);
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

        JLabel lblTitle = new JLabel(" QUẢN LÝ DANH MỤC KHÓA HỌC");
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
        modelKH = new DefaultTableModel(cols, 0); 
        
        JTable table = createAdminTable(modelKH);

        // Load dữ liệu lên bảng
        loadDataToTableKH(modelKH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Sự kiện nút Thêm
        btnAdd.addActionListener(e -> {
            showKhoaHocDialog(null, modelKH);
            loadDataToTableKH(modelKH); 
        });

        // Sự kiện nút Sửa
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa học cần sửa!");
                return;
            }
            String maKH = table.getValueAt(row, 0).toString();
            KhoaHoc kh = khoaHocDAO.getById(maKH);
            if (kh != null) {
                showKhoaHocDialog(kh, modelKH);
                loadDataToTableKH(modelKH);
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy khóa học trong Database!");
            }
        });
 
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa học cần xóa trên bảng!");
                return;
            }
            
            // Lấy mã và tên khóa học từ dòng đang chọn
            String maKH = table.getValueAt(row, 0).toString();
            String tenKH = table.getValueAt(row, 1).toString();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn xóa khóa học [" + tenKH + "] không?\nLưu ý: Hành động này chỉ thành công nếu khóa học chưa được mở lớp!", 
                    "Xác nhận xóa khóa học", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Thực thi lệnh xóa dưới Database
                    boolean isDeleted = khoaHocDAO.delete(maKH);
                    
                    if (isDeleted) {
                        JOptionPane.showMessageDialog(this, "Đã xóa khóa học thành công hoàn toàn!");
                        
                        // 👉 ÉP LÀM MỚI DANH SÁCH: Gọi nạp lại đúng Model toàn cục modelKH của trang
                        loadDataToTableKH(modelKH);
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại! Không tìm thấy mã khóa học tương ứng.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // Bẫy lỗi ràng buộc khi MySQL chặn xóa khóa học đã có dữ liệu lớp học liên kết
                    if (ex.getMessage().contains("Cannot delete or update a parent row")) {
                        JOptionPane.showMessageDialog(this, "Không thể xóa! Khóa học này đang có lớp học hoạt động.\nBạn phải xóa lớp học của môn này trước.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
                    }
                }
            }
        });

        return mainPanel;
    }

    // HÀM HIỂN THỊ DIALOG THÊM / SỬA KHÓA HỌC (ĐÃ FIX LỖI KHÔNG CẬP NHẬT BẢNG)
    private void showKhoaHocDialog(KhoaHoc existingKH, DefaultTableModel model) {
        boolean isEdit = (existingKH != null);
        String dialogTitle = isEdit ? "Sửa thông tin khóa học" : "Thêm khóa học mới";
        JDialog dialog = new JDialog(this, dialogTitle, true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
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

        // Nếu ở chế độ SỬA -> Đổ dữ liệu cũ lên các ô nhập liệu
        if (isEdit) {
            tMa.setText(existingKH.getMaKhoaHoc());
            tMa.setEditable(false); // Khóa không cho sửa Mã khóa học (Primary Key)
            tMa.setBackground(new Color(240, 240, 240));
            tTen.setText(existingKH.getTenKhoaHoc());
            // Tránh hiển thị dạng số mũ e+07 (ví dụ: 5.0E7 -> 50000000)
            tPhi.setText(String.format("%.0f", existingKH.getHocPhi()));
            tTime.setText(String.valueOf(existingKH.getThoiLuong()));
            cbStatus.setSelectedItem(existingKH.getTrangThai());
        }

        JButton btnSave = createStyledButton("XÁC NHẬN LƯU", new Color(40, 167, 69));
        btnSave.setPreferredSize(new Dimension(0, 45));
        btnSave.setOpaque(true);
        
        btnSave.addActionListener(e -> {
            try {
                String ma = tMa.getText().trim();
                String ten = tTen.getText().trim();
                
                if (ma.isEmpty() || ten.isEmpty() || tPhi.getText().trim().isEmpty() || tTime.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                double phi = Double.parseDouble(tPhi.getText().trim());
                int time = Integer.parseInt(tTime.getText().trim());
                String status = cbStatus.getSelectedItem().toString();

                KhoaHoc kh = new KhoaHoc(ma, ten, phi, time, status);
                
                if (isEdit) {
                    khoaHocDAO.update(kh);
                    JOptionPane.showMessageDialog(dialog, "Cập nhật khóa học thành công!");
                } else {
                    KhoaHoc checkExist = khoaHocDAO.getById(ma);
                    if (checkExist != null) {
                        JOptionPane.showMessageDialog(dialog, "Mã khóa học này đã tồn tại!");
                        return;
                    }
                    khoaHocDAO.create(kh);
                    JOptionPane.showMessageDialog(dialog, "Thêm khóa học mới thành công!");
                }
                
                // ĐIỂM CỐT LÕI: Gọi nạp lại dữ liệu từ Database lên lưới JTable tĩnh của bạn
                loadDataToTableKH(model);
                dialog.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Học phí và Thời lượng bắt buộc phải nhập số hợp lệ!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Lỗi hệ thống: " + ex.getMessage());
            }
        });

        dialog.add(p, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JPanel createLopHocPage() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(COLOR_BG_LIGHT);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Header & Toolbar ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(" QUẢN LÝ DANH SÁCH LỚP HỌC");
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

        // --- Bảng hiển thị kết nối Model toàn cục ---
        String[] cols = {"Mã Lớp", "Tên Lớp", "Khóa Học", "Giảng Viên", "Ca", "Lịch", "Trạng Thái"};
        modelLH = new DefaultTableModel(cols, 0); 
        JTable table = createAdminTable(modelLH); 

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);

        loadDataToTableLH(modelLH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

     // Sự kiện nút Thêm lớp mới
        btnAdd.addActionListener(e -> {
            showLopHocDialog(null, modelLH);
        });

        // Sự kiện nút Sửa thông tin lớp học
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học trên bảng để sửa!");
                return;
            }
            Object[] selectedData = new Object[7];
            for (int i = 0; i < 7; i++) {
                selectedData[i] = table.getValueAt(selectedRow, i);
            }
            showLopHocDialog(selectedData, modelLH);
        });

        // Sự kiện nút Xóa lớp học
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học cần xóa!");
                return;
            }
            String maLop = table.getValueAt(selectedRow, 0).toString();
            String tenLop = table.getValueAt(selectedRow, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn xóa lớp [" + tenLop + "] không?", 
                    "Xác nhận hành động xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (lopHocDAO.delete(maLop)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa lớp học thành công!");
                    loadDataToTableLH(modelLH); // Cập nhật lại giao diện lưới
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
            }
        });

        return mainPanel;
    }
    

    // HÀM CHUẨN: FORM LỚP HỌC (TỰ ĐỘNG LỌC GIẢNG VIÊN + ĐỒNG BỘ INSERT/UPDATE)

    private void showLopHocDialog(Object[] data, DefaultTableModel modelLH) {
        boolean isEdit = (data != null);
        JDialog dialog = new JDialog(this, isEdit ? "Chỉnh sửa thông tin lớp học" : "Thiết lập lớp học mới", true);
        dialog.setSize(480, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(0, 1, 5, 8));
        p.setBorder(new EmptyBorder(20, 30, 20, 30));

        JTextField tMaLop = new JTextField();
        JTextField tTenLop = new JTextField();
        JComboBox<String> cbKhoaHoc = new JComboBox<>();
        JComboBox<String> cbGiangVien = new JComboBox<>(); 
        JComboBox<String> cbCa = new JComboBox<>(new String[]{"sang", "toi"});
        JTextField tLich = new JTextField("2-4-6");
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{ "open", "closed"});

        // ĐĂNG KÝ SỰ KIỆN TỰ ĐỘNG ĐỔ GIẢNG VIÊN THEO CHUYÊN MÔN MÔN HỌC
        cbKhoaHoc.addActionListener(e -> {
            if (cbKhoaHoc.getSelectedItem() != null) {
                String maKH = cbKhoaHoc.getSelectedItem().toString().split(" - ")[0];
                updateGiangVienComboBox(maKH, cbGiangVien);
            }
        });

        // Nạp danh mục Khóa học vào ComboBox từ CSDL
        for (KhoaHoc kh : khoaHocDAO.getAll()) {
            cbKhoaHoc.addItem(kh.getMaKhoaHoc() + " - " + kh.getTenKhoaHoc());
        }

        // Kích hoạt nạp Giảng viên cho khóa học đầu tiên hiển thị ngay khi mở form
        if (cbKhoaHoc.getItemCount() > 0) {
            String maKHBanDau = cbKhoaHoc.getItemAt(0).split(" - ")[0];
            updateGiangVienComboBox(maKHBanDau, cbGiangVien);
        }

        p.add(new JLabel("Mã lớp học:")); p.add(tMaLop);
        p.add(new JLabel("Tên lớp học:")); p.add(tTenLop);
        p.add(new JLabel("Chọn Khóa học:")); p.add(cbKhoaHoc);
        p.add(new JLabel("Chọn Giảng viên đảm nhiệm:")); p.add(cbGiangVien);
        p.add(new JLabel("Ca học:")); p.add(cbCa);
        p.add(new JLabel("Lịch học (VD: 2-4-6):")); p.add(tLich);
        p.add(new JLabel("Trạng thái lớp:")); p.add(cbStatus);

        // NẾU LÀ CHẾ ĐỘ SỬA: Đổ ngược dữ liệu cũ từ bảng lên form
        if (isEdit) {
            tMaLop.setText(data[0].toString());
            tMaLop.setEditable(false); 
            tMaLop.setBackground(new Color(240, 240, 240));
            tTenLop.setText(data[1].toString());
            
            for (int i = 0; i < cbKhoaHoc.getItemCount(); i++) {
                if (cbKhoaHoc.getItemAt(i).contains(data[2].toString())) {
                    cbKhoaHoc.setSelectedIndex(i);
                    break;
                }
            }
            
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
        btnSave.setPreferredSize(new Dimension(0, 45));
        btnSave.setOpaque(true);

        btnSave.addActionListener(e -> {
            if (tMaLop.getText().trim().isEmpty() || tTenLop.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            
            if (cbGiangVien.getSelectedItem() == null || cbGiangVien.getSelectedItem().toString().contains("chưa có giảng viên")) {
                JOptionPane.showMessageDialog(dialog, "Không thể lưu lớp khi chưa chọn giảng viên đảm nhiệm!");
                return;
            }

            String maLop = tMaLop.getText().trim();
            String tenLop = tTenLop.getText().trim();
            String maKH = cbKhoaHoc.getSelectedItem().toString().split(" - ")[0];
            String maGV = cbGiangVien.getSelectedItem().toString().split(" - ")[0];
            String caHoc = cbCa.getSelectedItem().toString();
            String lichHoc = tLich.getText().trim();
            String trangThai = cbStatus.getSelectedItem().toString();

            // Khởi tạo thực thể tinh gọn không chứa học phí, thời lượng thừa
            LopHoc lh = new LopHoc(maLop, tenLop, maKH, maGV, caHoc, lichHoc, trangThai);

            boolean success;
            if (isEdit) {
                success = lopHocDAO.update(lh);
            } else {
                if (lopHocDAO.getById(maLop) != null) {
                    JOptionPane.showMessageDialog(dialog, "Mã lớp học này đã tồn tại trên hệ thống!");
                    return;
                }
                success = lopHocDAO.insert(lh);
            }

            if (success) {
                JOptionPane.showMessageDialog(dialog, isEdit ? "Đã cập nhật thông tin lớp học!" : "Đã mở lớp học mới thành công!");
                loadDataToTableLH(modelLH); // Ép nạp lại JTable thời gian thực
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Lưu dữ liệu thất bại, lỗi hệ thống!");
            }
        });

        dialog.add(new JScrollPane(p), BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Hàm tiện ích: Xóa trắng và nạp động giảng viên vào ComboBox theo Mã khóa học
    private void updateGiangVienComboBox(String maKhoaHoc, JComboBox<String> cbGiangVien) {
        cbGiangVien.removeAllItems(); 
        List<Object[]> dsGV = giangVienDAO.getGiangVienByKhoaHoc(maKhoaHoc);
        
        // Bẫy dự phòng: Nếu chưa ai có chuyên môn khớp, tự động lấy tất cả để tránh trống ComboBox
        if (dsGV == null || dsGV.isEmpty()) {
            dsGV = giangVienDAO.getGiangVienFullInfo();
        }
        
        if (dsGV != null && !dsGV.isEmpty()) {
            for (Object[] gv : dsGV) {
                cbGiangVien.addItem(gv[0].toString() + " - " + gv[1].toString());
            }
        } else {
            cbGiangVien.addItem("Hệ thống chưa có giảng viên nào");
        }
    }
    private JPanel createTopHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_CYAN_MAIN);
        header.setPreferredSize(new Dimension(0, 60)); 
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel(" HỆ THỐNG QUẢN TRỊ VIÊN");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(lbl, BorderLayout.WEST);

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
                this.dispose();
                SwingUtilities.invokeLater(() -> {
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
        
        btn.addActionListener(e -> {
            if (cardName.equals("HocVien")) {
                refreshEntityPage("HV");
            } else if (cardName.equals("GiangVien")) {
                refreshEntityPage("GV");
            } else if (cardName.equals("LopHoc")) {
                loadDataToTableLH(modelLH);
                cardLayout.show(cardPanel, cardName);
            } else if (cardName.equals("KhoaHoc")) {
                // 👉 ĐÃ SỬA: Xóa bỏ dòng loadDataToTableKH(modelThongBao) gây lỗi bộ nhớ chéo
                loadDataToTableKH(modelKH);                
                cardLayout.show(cardPanel, cardName);
            } else if (cardName.equals("ThongBao")) {
                refreshThongBaoTable(); 
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
        if (list != null) {
            for (Object[] row : list) {
                model.addRow(row);
            }
        }
        
        if (cardPanel != null) {
            cardPanel.revalidate();
            cardPanel.repaint();
        }
    }

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
    
    private void loadDataToTableKH(DefaultTableModel model) {
        if (model == null) return;
        
        // 1. CỐT LÕI: Phải xóa sạch các dòng cũ trên lưới giao diện trước
        model.setRowCount(0); 

        // 2. Bốc danh sách mới từ Database lên
        List<KhoaHoc> list = khoaHocDAO.getList(); 
        if (list != null) {
            for (KhoaHoc kh : list) {
                model.addRow(new Object[]{
                    kh.getMaKhoaHoc(),
                    kh.getTenKhoaHoc(),
                    kh.getHocPhi(), 
                    kh.getThoiLuong(),
                    kh.getTrangThai()
                });
            }
        }
        
        // 3. Ép giao diện vẽ lại đồ họa thời gian thực
        if (cardPanel != null) {
            cardPanel.revalidate();
            cardPanel.repaint();
        }
    }
    
    private JPanel createNotificationPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 30));
        panel.setBackground(COLOR_BG_LIGHT);
        panel.setBorder(new EmptyBorder(40, 50, 40, 50)); 

        JLabel lblTitle = new JLabel(" QUẢN LÝ THÔNG BÁO HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26)); 
        lblTitle.setForeground(COLOR_CYAN_MAIN);
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(30, 40, 30, 40)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 16);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        JLabel lblT = new JLabel("Tiêu đề:"); lblT.setFont(labelFont);
        formCard.add(lblT, gbc);

        gbc.gridx = 1; gbc.weightx = 0.9;
        JTextField txtT = new JTextField();
        txtT.setFont(inputFont);
        txtT.setPreferredSize(new Dimension(0, 45)); 
        formCard.add(txtT, gbc);

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

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblDt = new JLabel("Đối tượng:"); lblDt.setFont(labelFont);
        formCard.add(lblDt, gbc);

        gbc.gridx = 1;
        JComboBox<String> cb = new JComboBox<>(new String[]{"Tất cả học viên & giảng viên", "Chỉ dành cho Học viên", "Chỉ dành cho Giảng viên"});
        cb.setFont(inputFont);
        cb.setPreferredSize(new Dimension(0, 45));
        cb.setBackground(Color.WHITE);
        formCard.add(cb, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        JButton btnAdd = new JButton("ĐĂNG THÔNG BÁO LÊN HỆ THỐNG");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(COLOR_CYAN_MAIN); 
        btnAdd.setPreferredSize(new Dimension(0, 50));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        formCard.add(btnAdd, gbc);

        modelThongBao = new DefaultTableModel(new String[]{"ID", "Tiêu đề thông báo", "Nội dung", "Gửi đến"}, 0);
        JTable table = createAdminTable(modelThongBao); 
        
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(500);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);

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

    // HIỂN THỊ CHUẨN XÁC TOÀN BỘ DANH SÁCH THÔNG BÁO CHO ADMIN
    private void refreshThongBaoTable() {
        if (modelThongBao == null) return;
        
        modelThongBao.setRowCount(0); // Xóa sạch lưới cũ
        java.util.List<com.trungtam.model.ThongBao> ds = thongBaoService.getThongBaoChoHocVien();
        
        if (ds != null) {
            for (ThongBao tb : ds) {
                // Ánh xạ chuẩn từ số đối tượng sang chữ để hiển thị lên cột "Gửi đến"
                String dt = "Tất cả";
                if (tb.getDoiTuongNhan() == 1) dt = "Học viên";
                else if (tb.getDoiTuongNhan() == 2) dt = "Giảng viên";
                
                // Đổ chuẩn 4 cột: ID | Tiêu đề thông báo | Nội dung | Gửi đến
                modelThongBao.addRow(new Object[]{
                    tb.getMaThongBao(), 
                    tb.getTieuDe(), 
                    tb.getNoiDung(), 
                    dt
                });
            }
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

        JLabel lblTitle = new JLabel(" THỐNG KÊ HOẠT ĐỘNG HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_CYAN_MAIN);
        lblTitle.setBorder(new EmptyBorder(40, 0, 40, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel chartPanel = new JPanel(new GridLayout(1, 2, 50, 0));
        chartPanel.setOpaque(false);
        chartPanel.setBorder(new EmptyBorder(0, 60, 60, 60));

        progressHV = new CircularProgressBar("TỶ LỆ HỌC VIÊN ĐANG HỌC", COLOR_CYAN_MAIN);
        progressGV = new CircularProgressBar("TỶ LỆ GIẢNG VIÊN ĐANG DẠY", new Color(76, 175, 80));

        chartPanel.add(progressHV);
        chartPanel.add(progressGV);
        mainPanel.add(chartPanel, BorderLayout.CENTER);

        refreshDashboardData();

        return mainPanel;
    }
    
    private void refreshDashboardData() {
        int[] stats = adminDAO.getDashboardStats();
        SwingUtilities.invokeLater(() -> {
            progressHV.updateValue(stats[1], stats[0]); 
            progressGV.updateValue(stats[3], stats[2]); 
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
