package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LecturerUI extends JFrame {

    public LecturerUI() {
        setTitle("Giảng viên Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 153, 204));
        header.setPreferredSize(new Dimension(0, 60));

        JLabel title = new JLabel("  Lecturer Dashboard");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel user = new JLabel("Nguyễn Văn A   ");
        user.setForeground(Color.WHITE);

        header.add(title, BorderLayout.WEST);
        header.add(user, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(245, 245, 245));
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] menu = {
                "Trang chủ",
                "Danh sách lớp",
                "Nhập điểm",
                "Quản lý sinh viên",
                "Thống kê"
        };

        for (String m : menu) {
            JButton btn = new JButton(m);
            btn.setMaximumSize(new Dimension(200, 40));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(10));
        }

        add(sidebar, BorderLayout.WEST);

        // ===== MAIN CONTENT =====
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);

        JLabel welcome = new JLabel("👋 Chào giảng viên Nguyễn Văn A");
        welcome.setFont(new Font("Arial", Font.BOLD, 22));
        welcome.setBorder(new EmptyBorder(10, 10, 10, 10));

        main.add(welcome, BorderLayout.NORTH);

        // ===== CARD THÔNG TIN GIẢNG VIÊN =====
        JPanel cardInfo = new JPanel(new BorderLayout());
        cardInfo.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleInfo = new JLabel("Thông tin giảng viên");
        titleInfo.setFont(new Font("Arial", Font.BOLD, 16));
        cardInfo.add(titleInfo, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        infoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel col1 = new JPanel();
        col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));
        col1.add(new JLabel("Mã GV: GV001"));
        col1.add(new JLabel("Tên: Nguyễn Văn A"));
        col1.add(new JLabel("Khoa: Viễn thông"));
        col1.add(new JLabel("Chuyên môn: Mạng máy tính"));

        JPanel col2 = new JPanel();
        col2.setLayout(new BoxLayout(col2, BoxLayout.Y_AXIS));
        col2.add(new JLabel("SĐT: 0909123456"));
        col2.add(new JLabel("Email: nguyenvana@ptit.edu.vn"));
        col2.add(new JLabel("Học vị: Thạc sĩ"));

        infoPanel.add(col1);
        infoPanel.add(col2);

        cardInfo.add(infoPanel, BorderLayout.CENTER);

        // ===== CARD DANH SÁCH LỚP =====
        JPanel cardClass = new JPanel(new BorderLayout());
        cardClass.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleClass = new JLabel("Danh sách lớp đang giảng dạy");
        titleClass.setFont(new Font("Arial", Font.BOLD, 16));
        cardClass.add(titleClass, BorderLayout.NORTH);

        String[] columns = {"Mã lớp", "Tên môn", "Số SV"};
        Object[][] data = {
                {"INT101", "Mạng máy tính", 45},
                {"INT202", "Lập trình Java", 50},
                {"INT303", "An toàn thông tin", 40}
        };

        JTable table = new JTable(data, columns);
        JScrollPane scroll = new JScrollPane(table);

        cardClass.add(scroll, BorderLayout.CENTER);

        // ===== WRAP =====
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(new EmptyBorder(10, 10, 10, 10));

        wrapper.add(cardInfo);
        wrapper.add(Box.createVerticalStrut(15));
        wrapper.add(cardClass);

        main.add(wrapper, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LecturerUI().setVisible(true);
        });
    }
}