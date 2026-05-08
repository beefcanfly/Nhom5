package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class AdminUI extends JFrame {

    public AdminUI() {
        setTitle("Admin Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 153, 204));
        header.setPreferredSize(new Dimension(0, 60));

        JLabel title = new JLabel("  ADMIN DASHBOARD");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel user = new JLabel("Admin   ");
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
                "Dashboard",
                "Quản lý sinh viên",
                "Quản lý giảng viên",
                "Quản lý khóa học",
                "Quản lý lớp",
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

        // ===== MAIN1 =====
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);

        JLabel welcome = new JLabel("👋 Chào Admin");
        welcome.setFont(new Font("Arial", Font.BOLD, 22));
        welcome.setBorder(new EmptyBorder(10, 10, 10, 10));

        main.add(welcome, BorderLayout.NORTH);

        // ===== THỐNG KÊ =====
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        statsPanel.add(createStatCard("Sinh viên", "500"));
        statsPanel.add(createStatCard("Giảng viên", "50"));
        statsPanel.add(createStatCard("Khóa học", "30"));

        // ===== TABLE =====
        String[] columns = {"ID", "Tên", "Vai trò"};
        Object[][] data = {
                {"SV001", "Nguyễn Văn B", "Sinh viên"},
                {"GV001", "Trần Văn C", "Giảng viên"},
                {"AD001", "Admin", "Quản trị"}
        };

        JTable table = new JTable(data, columns);
        JScrollPane scroll = new JScrollPane(table);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel tableTitle = new JLabel("Danh sách người dùng");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 16));

        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        // ===== WRAP =====
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        wrapper.add(statsPanel);
        wrapper.add(tableCard);

        main.add(wrapper, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    // ===== CARD THỐNG KÊ =====
    private JPanel createStatCard(String title, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(
                new LineBorder(Color.GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel lblValue = new JLabel(value, JLabel.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 24));
        lblValue.setForeground(Color.BLUE);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminUI().setVisible(true);
        });
    }
}
