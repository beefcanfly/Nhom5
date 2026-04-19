package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LoginUI extends JFrame {

    public LoginUI() {
        setTitle("Sinh viên Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 153, 204));
        header.setPreferredSize(new Dimension(0, 60));

        JLabel title = new JLabel("  UIS CDN Mien Nam");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel user = new JLabel("Trần Quang Khải   ");
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
                "Thông báo",
                "Xem chương trình đào tạo",
                "Đăng ký môn học",           
                "Xem học phí",
                "Thời khóa biểu",
                "Xem điểm",
                "Xem lịch thi"
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

        JLabel welcome = new JLabel("👋 Chào mừng Trần Quang Khải");
        welcome.setFont(new Font("Arial", Font.BOLD, 22));
        welcome.setBorder(new EmptyBorder(10, 10, 10, 10));

        main.add(welcome, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel cardTitle = new JLabel("Thông tin sinh viên");
        cardTitle.setFont(new Font("Arial", Font.BOLD, 16));

        card.add(cardTitle, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        infoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel col1 = new JPanel();
        col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));

        JLabel avatar = new JLabel();
        avatar.setPreferredSize(new Dimension(120, 140));
        avatar.setOpaque(true);
        avatar.setBackground(Color.BLUE);

        col1.add(avatar);
        col1.add(Box.createVerticalStrut(10));
        col1.add(new JLabel("Mã SV: "));
        col1.add(new JLabel("Tên: Trần Quang Khải"));
        col1.add(new JLabel("Ngày sinh: "));
        col1.add(new JLabel("Giới tính: Nam"));

        JPanel col2 = new JPanel();
        col2.setLayout(new BoxLayout(col2, BoxLayout.Y_AXIS));

        col2.add(new JLabel("SĐT: "));
        col2.add(new JLabel("CMND: "));
        col2.add(new JLabel("Dân tộc: "));
        col2.add(new JLabel("Nơi sinh: "));

        JPanel col3 = new JPanel();
        col3.setLayout(new BoxLayout(col3, BoxLayout.Y_AXIS));

        col3.add(new JLabel("Quốc tịch: Việt Nam"));
        col3.add(new JLabel("Email: n22dcvt044@student.ptit"));
        col3.add(new JLabel("Email 2: khaitran@gmail.com"));
        col3.add(new JLabel("Địa chỉ: Quảng Trị"));

        infoPanel.add(col1);
        infoPanel.add(col2);
        infoPanel.add(col3);

        card.add(infoPanel, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        wrapper.add(card, BorderLayout.NORTH);

        main.add(wrapper, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}