package com.trungtam.test;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.table.DefaultTableCellRenderer;

import view.FullUI;

public class TestHocVienUI extends JFrame {

    // === ĐỊNH NGHĨA BẢNG MÀU ===
    private final Color BLUE_PRIMARY = new Color(52, 152, 219);
    private final Color BLUE_LIGHT = new Color(230, 247, 255);
    private final Color BLUE_BORDER = new Color(144, 202, 249);
    private final Color TEXT_MAIN = new Color(51, 51, 51);
    private final Color TEXT_LABEL = new Color(119, 119, 119);
    private final Color GREEN_CHART = new Color(76, 175, 80);
    private final Color BG_CONTENT = Color.WHITE;

    public TestHocVienUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Cổng thông tin sinh viên");
        setSize(1280, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. HEADER (Thanh trên cùng)
        add(createHeader(), BorderLayout.NORTH);

        // 2. SIDEBAR (Menu trái)
        add(createSidebar(), BorderLayout.WEST);

        // 3. MAIN CONTENT (Nội dung chính)
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(BG_CONTENT);
        mainContent.setBorder(new EmptyBorder(15, 20, 20, 20));

        // --- Tiêu đề & Ngày tháng ---
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setBackground(BG_CONTENT);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblWelcome = new JLabel("👋 Chào mừng Nguyễn Thị Kim Ngân");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblWelcome.setForeground(BLUE_PRIMARY);
        
        JLabel lblDate = new JLabel("📅 Thứ 5, 07 Tháng 05");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDate.setForeground(TEXT_LABEL);
        
        titlePanel.add(lblWelcome);
        titlePanel.add(lblDate);
        mainContent.add(titlePanel);
        mainContent.add(Box.createVerticalStrut(15));

        // --- Card 1: Thông tin sinh viên ---
        mainContent.add(createStudentInfoCard());
        mainContent.add(Box.createVerticalStrut(15));

        // --- Card 2: Thông tin khóa học ---
        mainContent.add(createCourseInfoCard());
        mainContent.add(Box.createVerticalStrut(15));

        // --- Card 3: Biểu đồ kết quả học tập ---
        mainContent.add(createChartCard());
        
     // --- Card 4: Lịch học cá nhân ---
        mainContent.add(createCalendarCard());
        mainContent.add(Box.createVerticalStrut(15));

        // --- Card 5: Thời khóa biểu dạng học kỳ ---
mainContent.add(createTimetableCard());

        // Bọc nội dung vào JScrollPane để cuộn được khi màn hình nhỏ
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Tăng tốc độ cuộn chuột
        add(scrollPane, BorderLayout.CENTER);
    }

    // ================= CÁC HÀM TẠO THÀNH PHẦN GIAO DIỆN =================

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 171, 226)); // Màu xanh dương nhạt của header
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(new EmptyBorder(0, 15, 0, 15));

        JLabel logo = new JLabel("🌐 Cổng thông tin"); // Placeholder logo
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.add(logo, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        userPanel.setOpaque(false);
        JLabel userInfo = new JLabel("Nguyễn Thị Kim Ngân - N22DCVT060");
        userInfo.setForeground(Color.WHITE);
        userInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
            "Trang chủ", "Thông báo từ ban quản trị", "Xem chương trình đào tạo", 
"Xem môn học tiên quyết", "Đăng ký môn học", "Đăng ký môn nguyện vọng", 
            "Xem học phí", "Hóa đơn điện tử", "Thời khóa biểu dạng tuần", 
            "Thời khóa biểu dạng học kỳ", "Xem lịch thi", "Xem điểm", 
            "Cập nhật thông tin thường trú", "Gửi ý kiến"
        };

        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btn.setForeground(BLUE_PRIMARY);
            btn.setBackground(Color.WHITE);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMaximumSize(new Dimension(260, 40));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setMargin(new Insets(0, 15, 0, 0));
            
            // Thêm đường kẻ dưới cho mỗi nút
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(Color.WHITE);
            wrapper.setBorder(new MatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
            wrapper.add(btn, BorderLayout.CENTER);
            wrapper.setMaximumSize(new Dimension(260, 45));
            
            sidebar.add(wrapper);
        }

        JScrollPane scrollSidebar = new JScrollPane(sidebar);
        scrollSidebar.setPreferredSize(new Dimension(260, 0));
        scrollSidebar.setBorder(new MatteBorder(0, 0, 0, 1, BLUE_BORDER)); // Viền phải
        scrollSidebar.getVerticalScrollBar().setUnitIncrement(16);
        return scrollSidebar;
    }

    private JPanel createStudentInfoCard() {
        JPanel card = createCardBase("👤 Thông tin sinh viên");

        JPanel body = new JPanel(new BorderLayout(20, 0));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Khung Avatar bên trái
        JPanel avatarPanel = new JPanel();
        avatarPanel.setPreferredSize(new Dimension(100, 130));
        avatarPanel.setBackground(new Color(230, 230, 230));
        avatarPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
        JLabel lblImg = new JLabel("Ảnh 3x4", SwingConstants.CENTER);
        avatarPanel.add(lblImg);
        body.add(avatarPanel, BorderLayout.WEST);

        // Khung chứa 3 cột thông tin
        JPanel infoGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        infoGrid.setBackground(Color.WHITE);

        String[][] col1Data = {
            {"Mã SV:","N22DCVT060"},
            {"Tên sinh viên:","Nguyễn Thị Kim Ngân"},
            {"Ngày sinh:","17/07/2004"},
            {"Giới tính:","Nữ"},
            {"Trạng thái:","Đang học"}
        };
        String[][] col2Data = {
            {"Số điện thoại","0373548949"},
            {"Số CMND/CCCD","089304001736"},
            {"Dân tộc","Kinh"},
            {"Tôn giáo","Phật Giáo"},
            {"Nơi sinh","An Giang"}
        };
String[][] col3Data = {
            {"Quốc tịch","Việt Nam"},
            {"Email 1:","n22dcvt060@student.ptithcm.edu.vn"},
            {"Email 2:","nguyenthikimngan010120199@gmail.com"},
            {"Địa chỉ:","83/3, Hẻm 83, Đường Trần Hưng Đạo..."},
            {"",""} // Dòng trống để cân bằng lưới
        };

        infoGrid.add(createDataColumn(col1Data));
        infoGrid.add(createDataColumn(col2Data));
        infoGrid.add(createDataColumn(col3Data));

        body.add(infoGrid, BorderLayout.CENTER);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel createCourseInfoCard() {
        JPanel card = createCardBase("📘 Thông tin khóa học"); 

        JPanel body = new JPanel(new BorderLayout(20, 0));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(100, 0));
        spacer.setOpaque(false);
        body.add(spacer, BorderLayout.WEST);

        JPanel infoGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        infoGrid.setBackground(Color.WHITE);

        String[][] col1Data = {
            {"Lớp:","D22CQVTH101-N"},
            {"Ngành:","Kỹ thuật điện tử - viễn thông"},
            {"Chuyên ngành:","Hệ thống IoT"},
            {"",""},
            {"",""} 
        };
        
        String[][] col2Data = {
            {"Niên khóa:","2022-2027"},
            {"",""},
            {"",""},
            {"",""},
            {"",""}
        };
        
        String[][] col3Data = {
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""}
        };

        infoGrid.add(createDataColumn(col1Data));
        infoGrid.add(createDataColumn(col2Data));
        infoGrid.add(createDataColumn(col3Data));

        body.add(infoGrid, BorderLayout.CENTER);
        card.add(body, BorderLayout.CENTER);
        return card;
    }
  

    private JPanel createChartCard() {
        JPanel card = createCardBase("📈 Biểu đồ kết quả học tập");

        // Thêm ComboBox chọn học kỳ vào góc phải của Header card
        JPanel headerPanel = (JPanel) card.getComponent(0);
        JComboBox<String> comboTerm = new JComboBox<>(new String[]{"Học kỳ 1 Năm học 2025-2026"});
        comboTerm.setBackground(Color.WHITE);
        headerPanel.add(comboTerm, BorderLayout.EAST);

        // Vùng vẽ biểu đồ
        ChartPanel chartPanel = new ChartPanel();
        chartPanel.setPreferredSize(new Dimension(800, 300));
        card.add(chartPanel, BorderLayout.CENTER);

        return card;
    }
    
 // ================= TẠO CARD LỊCH HỌC CÁ NHÂN =================
    private JPanel createCalendarCard() {
        JPanel card = createCardBase("🗓️ Lịch học cá nhân");

        JPanel body = new JPanel(new BorderLayout(0, 15));
        body.setBackground(Color.WHITE);
body.setBorder(new EmptyBorder(15, 20, 20, 20));

        // 1. Thanh tiến trình (Progress Bar)
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(75); // Giá trị thanh màu xanh
        progressBar.setForeground(BLUE_PRIMARY);
        progressBar.setBackground(new Color(230, 230, 230));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(800, 8));
        
        JPanel progressPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        progressPanel.setBackground(Color.WHITE);
        progressPanel.add(progressBar);
        body.add(progressPanel, BorderLayout.NORTH);

        // 2. Khu vực Lịch (Giữa)
        JPanel calendarPanel = new JPanel(new BorderLayout(0, 10));
        calendarPanel.setBackground(Color.WHITE);

        // 2.1 Chọn tháng
        JPanel monthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        monthPanel.setBackground(Color.WHITE);
        JButton btnPrev = new JButton("<");
        btnPrev.setBackground(Color.WHITE);
        btnPrev.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        btnPrev.setFocusPainted(false);
        
        JLabel lblMonth = new JLabel("Tháng 5    2026");
        lblMonth.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton btnNext = new JButton(">");
        btnNext.setBackground(Color.WHITE);
        btnNext.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        btnNext.setFocusPainted(false);

        monthPanel.add(btnPrev);
        monthPanel.add(lblMonth);
        monthPanel.add(btnNext);
        calendarPanel.add(monthPanel, BorderLayout.NORTH);

        // 2.2 Lưới ngày tháng
        JPanel gridPanel = new JPanel(new GridLayout(6, 7, 5, 5));
        gridPanel.setBackground(Color.WHITE);

        // Header các thứ
        String[] daysOfWeek = {"T2", "T3", "T4", "T5", "T6", "T7", "CN"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            JLabel lblDay = new JLabel(daysOfWeek[i], SwingConstants.CENTER);
            lblDay.setFont(new Font("Segoe UI", Font.BOLD, 12));
            if (i == 6) lblDay.setForeground(Color.RED); // Chủ nhật màu đỏ
            gridPanel.add(lblDay);
        }

        // Render các ngày (Ví dụ mẫu cho tháng 5/2026)
        // Ngày xám (tháng trước)
        gridPanel.add(new DayCell("27", false, true, false));
        gridPanel.add(new DayCell("28", false, false, false));
        gridPanel.add(new DayCell("29", false, false, false));
        gridPanel.add(new DayCell("30", false, false, false));

        // Ngày trong tháng
        for (int i = 1; i <= 31; i++) {
            boolean isToday = (i == 7); // Ngày 7 hiện tại
            boolean hasEvent = (i == 1 || i == 4 || i == 8 || i == 11 || i == 15); // Các ngày có dấu chấm đỏ
            boolean isWeekend = ((i + 3) % 7 == 6 || (i + 3) % 7 == 0); // Xác định T7, CN
gridPanel.add(new DayCell(String.valueOf(i), isToday, hasEvent, true, isWeekend));
        }

        // Ngày xám (tháng sau)
        for (int i = 1; i <= 7; i++) {
             gridPanel.add(new DayCell(String.valueOf(i), false, false, false));
        }

        calendarPanel.add(gridPanel, BorderLayout.CENTER);
        body.add(calendarPanel, BorderLayout.CENTER);

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    // ================= TẠO CARD THỜI KHÓA BIỂU =================
    private JPanel createTimetableCard() {
        JPanel card = createCardBase("📅 Thời khóa biểu dạng học kỳ");

        // Combo box ở góc phải
        JPanel headerPanel = (JPanel) card.getComponent(0);
        JComboBox<String> comboTerm = new JComboBox<>(new String[]{"Học kỳ 2 - Năm học 2025 - 2026"});
        comboTerm.setBackground(Color.WHITE);
        headerPanel.add(comboTerm, BorderLayout.EAST);

        // Dữ liệu bảng
        String[] columns = {"STT", "Tên môn học", "Thứ", "Tiết bắt đầu", "Số tiết", "Phòng", "Giảng viên"};
        Object[][] data = {
            {"1", "Lập trình hướng đối tượng", "3", "1", "4", "2D15", ""},
            {"", "", "7", "1", "4", "2E27", ""},
            {"", "", "7", "1", "4", "2E27", ""},
            {"2", "Kiến trúc và giao thức IoT", "2", "7", "4", "2E16", ""},
            {"", "", "4", "7", "4", "2E2122", ""}
        };

        // Custom Table không cho phép edit
        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Styling cho Table
        table.setRowHeight(40);
        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);
        table.setGridColor(BLUE_BORDER);
        table.setShowVerticalLines(false); // Ẩn cột dọc giống ảnh
        table.setShowHorizontalLines(true); // Chỉ hiện dòng ngang
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(TEXT_MAIN);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, BLUE_BORDER));

        // Căn giữa nội dung các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columns.length; i++) {
            if(i != 1) { // Trừ cột Tên môn học
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(table);
scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(800, 250));

        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    // --- HÀM TIỆN ÍCH TẠO KHUNG CARD ---
    private JPanel createCardBase(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(BLUE_BORDER, 1, true));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, BLUE_BORDER),
                new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(BLUE_PRIMARY);
        header.add(lblTitle, BorderLayout.WEST);

        card.add(header, BorderLayout.NORTH);
        return card;
    }

    // --- HÀM TIỆN ÍCH TẠO CỘT DỮ LIỆU ---
    private JPanel createDataColumn(String[][] data) {
        JPanel panel = new JPanel(new GridLayout(data.length, 2, 5, 8));
        panel.setBackground(Color.WHITE);
        for (String[] row : data) {
            JLabel lbl = new JLabel(row[0]);
            lbl.setForeground(TEXT_LABEL);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            JLabel val = new JLabel(row[1]);
            val.setForeground(TEXT_MAIN);
            val.setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            panel.add(lbl);
            panel.add(val);
        }
        return panel;
    }

    // ================= LỚP VẼ BIỂU ĐỒ CỘT (CUSTOM COMPONENT) =================
    class ChartPanel extends JPanel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final String[] labels = {"SKD1108", "TEL1343", "TEL1348", "TEL1388", "TEL1401", "TEL1415", "TEL1447", "TEL1489"};
        private final double[] values = {8.1, 7.7, 7.8, 6.1, 6.8, 8.2, 7.2, 7.3};

        public ChartPanel() {
            setBackground(Color.WHITE);
            setBorder(new EmptyBorder(20, 20, 40, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 40;
            int paddingRight = 40;
            int chartWidth = width - padding - paddingRight;
            int chartHeight = height - padding * 2;
            double maxScore = 10.0;

            // Vẽ các đường lưới ngang (Grid lines)
            g2.setColor(new Color(230, 230, 230));
g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            for (int i = 0; i <= 5; i++) {
                double score = i * 2.0;
                int y = height - padding - (int) ((score / maxScore) * chartHeight);
                g2.drawLine(padding, y, width - paddingRight, y);
                g2.setColor(TEXT_LABEL);
                g2.drawString(String.valueOf(score), 15, y + 4);
                g2.setColor(new Color(230, 230, 230));
            }

            // Vẽ các cột
            int barWidth = 25;
            int spacing = chartWidth / values.length;

            for (int i = 0; i < values.length; i++) {
                int x = padding + (i * spacing) + (spacing / 2) - (barWidth / 2);
                int barHeight = (int) ((values[i] / maxScore) * chartHeight);
                int y = height - padding - barHeight;

                // Vẽ cột màu xanh lá
                g2.setColor(GREEN_CHART);
                g2.fill(new RoundRectangle2D.Double(x, y, barWidth, barHeight, 5, 5));

                // Chữ số điểm trên đỉnh cột
                g2.setColor(TEXT_MAIN);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                String scoreTxt = String.valueOf(values[i]);
                int txtWidth = fm.stringWidth(scoreTxt);
                g2.drawString(scoreTxt, x + (barWidth - txtWidth) / 2, y - 5);

                // Tên môn học dưới trục X
                g2.setColor(TEXT_LABEL);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                String label = labels[i];
                int lblWidth = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, x + (barWidth - lblWidth) / 2, height - padding + 15);
            }
            
            // Vẽ chú thích (Legend) ở dưới cùng
            g2.setColor(GREEN_CHART);
            g2.fillRect(width / 2 - 40, height - 15, 10, 10);
            g2.setColor(TEXT_MAIN);
            g2.drawString("Đạt", width / 2 - 25, height - 6);
            
            g2.setColor(Color.RED);
            g2.fillRect(width / 2 + 10, height - 15, 10, 10);
            g2.setColor(TEXT_MAIN);
            g2.drawString("Không đạt", width / 2 + 25, height - 6);
        }
        
     // ================= CLASS VẼ Ô NGÀY TRONG LỊCH =================
        class DayCell extends JPanel {
            /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
			private String day;
            private boolean isToday;
            private boolean hasEvent;
            private boolean isCurrentMonth;
            private boolean isWeekend;

            // Constructor đầy đủ
            public DayCell(String day, boolean isToday, boolean hasEvent, boolean isCurrentMonth, boolean isWeekend) {
                this.day = day;
                this.isToday = isToday;
                this.hasEvent = hasEvent;
                this.isCurrentMonth = isCurrentMonth;
this.isWeekend = isWeekend;
                setBackground(Color.WHITE);
                setPreferredSize(new Dimension(40, 50));
            }

            // Constructor rút gọn
            public DayCell(String day, boolean isToday, boolean hasEvent, boolean isCurrentMonth) {
                this(day, isToday, hasEvent, isCurrentMonth, false);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Vẽ vòng tròn xanh lá nếu là ngày hôm nay
                if (isToday) {
                    g2.setColor(GREEN_CHART);
                    int size = 30;
                    g2.fillOval((w - size) / 2, (h - size) / 2 - 5, size, size);
                    g2.setColor(Color.WHITE);
                } 
                // Màu chữ ngày tháng
                else if (!isCurrentMonth) {
                    g2.setColor(new Color(200, 200, 200)); // Ngày tháng khác (màu xám)
                } else {
                    g2.setColor(isWeekend ? Color.RED : TEXT_MAIN); // T7, CN màu đỏ theo ảnh
                }

                // Vẽ số ngày
                g2.setFont(new Font("Segoe UI", isToday ? Font.BOLD : Font.PLAIN, 13));
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(day);
                g2.drawString(day, (w - tw) / 2, (h + fm.getAscent()) / 2 - 8);

                // Vẽ dấu chấm đỏ báo hiệu có sự kiện/lịch học
                if (hasEvent) {
                    g2.setColor(Color.RED);
                    g2.fillOval(w / 2 - 2, h / 2 + 10, 4, 4);
                }
            }
        }
    }
 // ================= CLASS VẼ Ô NGÀY TRONG LỊCH =================
    // Dán đoạn code này ngang hàng với các hàm createCard, nằm gọn trong class testHocVienUI
    class DayCell extends JPanel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String day;
        private boolean isToday;
        private boolean hasEvent;
        private boolean isCurrentMonth;
        private boolean isWeekend;

        // Constructor đầy đủ
        public DayCell(String day, boolean isToday, boolean hasEvent, boolean isCurrentMonth, boolean isWeekend) {
            this.day = day;
            this.isToday = isToday;
            this.hasEvent = hasEvent;
            this.isCurrentMonth = isCurrentMonth;
            this.isWeekend = isWeekend;
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(40, 50));
        }

        // Constructor rút gọn
        public DayCell(String day, boolean isToday, boolean hasEvent, boolean isCurrentMonth) {
            this(day, isToday, hasEvent, isCurrentMonth, false);
        }
@Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Vẽ vòng tròn xanh lá nếu là ngày hôm nay
            if (isToday) {
                g2.setColor(GREEN_CHART); // Lấy màu GREEN_CHART từ class cha
                int size = 30;
                g2.fillOval((w - size) / 2, (h - size) / 2 - 5, size, size);
                g2.setColor(Color.WHITE);
            } 
            // Màu chữ ngày tháng
            else if (!isCurrentMonth) {
                g2.setColor(new Color(200, 200, 200)); // Ngày tháng khác (màu xám)
            } else {
                g2.setColor(isWeekend ? Color.RED : TEXT_MAIN); // T7, CN màu đỏ
            }

            // Vẽ số ngày
            g2.setFont(new Font("Segoe UI", isToday ? Font.BOLD : Font.PLAIN, 13));
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(day);
            g2.drawString(day, (w - tw) / 2, (h + fm.getAscent()) / 2 - 8);

            // Vẽ dấu chấm đỏ báo hiệu có sự kiện/lịch học
            if (hasEvent) {
                g2.setColor(Color.RED);
                g2.fillOval(w / 2 - 2, h / 2 + 10, 4, 4);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TestHocVienUI().setVisible(true);
        });
    }
}
